package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.ISectionInfoListener;
import multhreadfiletransport.observer.ISectionInfoSpeaker;
import multhreadfiletransport.observer.ISectionReceiverListener;
import multhreadfiletransport.observer.ISectionReceiverSpeaker;
import multhreadfiletransport.util.ParseUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by dela on 1/23/18.
 */
public class RecieverThread implements Runnable, ISectionInfoSpeaker,
        ISectionReceiverSpeaker {
    private Socket socket;
    private BufferedInputStream inputStream;
    private int headerSize;
    private int bufferSize;
    private byte[] buffer;
    private List<RecieverSectionInfo> sectionFileInfoList;
    private List<ISectionInfoListener> sectionInfoListeners;
    private List<ISectionReceiverListener> sectionReceiverListeners;
    private String targetPath;
    private int targetFileCount;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        this.headerSize = Integer.parseInt(resourceBundle.getString("headerSize"));
        this.bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        buffer = new byte[bufferSize];
        targetPath = resourceBundle.getString("targetPath");

        sectionFileInfoList = new ArrayList<>();
        sectionInfoListeners = new ArrayList<>();
        sectionReceiverListeners = new ArrayList<>();
    }

    public RecieverThread(ISectionInfoListener listener, Socket socket) {
        this.sectionInfoListeners.add(listener);
        this.socket = socket;
        try {
            inputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public List<RecieverSectionInfo> getSectionFileInfoList() {
        return sectionFileInfoList;
    }

    public void setSectionFileInfoList(List<RecieverSectionInfo> sectionFileInfoList) {
        this.sectionFileInfoList = sectionFileInfoList;
    }

    public List<ISectionInfoListener> getSectionInfoListeners() {
        return sectionInfoListeners;
    }

    public void setSectionInfoListeners(List<ISectionInfoListener> sectionInfoListeners) {
        this.sectionInfoListeners = sectionInfoListeners;
    }

    public int getTargetFileCount() {
        return targetFileCount;
    }

    public void setTargetFileCount(int targetFileCount) {
        this.targetFileCount = targetFileCount;
    }

    @Override
    public void run() {
        // 先接收整个分片文件列表, 然后再接收每个分片
        try {

            System.out.println("当前线程:" + Thread.currentThread());

            // 1. 接收整个分片文件列表
            recieveSectionInfoList();

            // 2. 接收每一个分片文件
            int sectionCount = sectionFileInfoList.size();

            System.out.println("分片文件的个数有:" + sectionCount);

            while(sectionCount != 0) {
                recieveSection();
                sectionCount--;
            }
            //这个sender的全部section全部发送完毕, 通知view
            sendOnReceiveOver(this);

            // 3. 接收完毕, 关闭输入流和socket
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recieveSectionInfoList() throws IOException {

        System.out.println("开始接收分片文件列表信息");

        // 接收分片文件列表包头信息
        String sectionListInfo = getHeaderInfo();

        // 接下来将收到的分片信息列表进行解析并通过观察者模式传给RCenter, 然后整合到RC里的map中
        // 1. 解析
        sectionFileInfoList = ParseUtil.parseStringToSectionInfoList(sectionListInfo);

        // 2. 发送给centersectionList, 以便center进行填充
        sendSectionInfoList();

        // 3. 将sectionlist发送给view(其实在这里用两个关于section的观察者模式是不合理的, 后期再整合)
        // 拉模式
        sendOnGetSectionList(this);

        System.out.println("接收分片文件列表信息完毕");
    }

    public void recieveSection() throws IOException {

        System.out.println("开始接收每一个分片文件");

        // 先接收分片包头, 再解析, 然后通知RC, 再接收分片文件正式内容
        RecieverSectionInfo sectionInfo;

        // 1. 接收包头
        String sectionString = getHeaderInfo();
        // 2. 解析并通知RC和view
        sectionInfo = ParseUtil.parseStringToSectionInfo(sectionString);
        sectionInfo.setRecieveMark(true);
        // 通知RC
        sendSectionInfo(sectionInfo);
        // 通知view
        sendOnBeginReceiveOneSection(sectionInfo);

        // 3. 接收分片文件正式内容, 并传送一个信息告诉center, 已经接收完毕一个文件
        recieveFile(sectionInfo);
        sectionInfo.setSaveMark(true);
        sendSectionSaveOK(sectionInfo);
        // 4. 当这个分片文件全部接收完毕之后, 通知view
        sendEndReceiveOneSection(this);

        System.out.println("接收一个分片文件结束");
    }

    public void recieveFile(RecieverSectionInfo sectionInfo) throws IOException {
        String filePath = targetPath + sectionInfo.getTempFileName();
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");

        // 开始接收文件
        System.out.println("开始接收文件");
        int haveLen = 0;
        while (haveLen != sectionInfo.getSectionLen()) {
            int temp = inputStream.read(buffer, 0, bufferSize);
//            System.out.println("本次接收到的大小：" + temp);
            file.write(buffer, 0, temp);
            // 每读成功一次, 就通知view一次
            sendOnReceiving(temp);

            haveLen += temp;
        }

        file.close();
    }

    // 读取头部信息(包括大包头(section信息列表), 小包头(每个section信息))
    public String getHeaderInfo() throws IOException {
        // 先接收headerSize求出后面info的len, 再接收len个byte, 最后转换成字符串返回
        System.out.println("现在开始解析包头");

        //1. 先求出len
        byte[] tempInfoLen = new byte[headerSize];

        System.out.println("headersize: " + headerSize);
        System.out.println("tempinfolen byte : " + tempInfoLen.length);

        int temp = 0;
        int readlen = 0;


        while (temp != headerSize) {
            int len = headerSize - temp;
            readlen = inputStream.read(tempInfoLen, temp, len);

            System.out.println("readlen" + readlen);

            temp += readlen;
        }
        long infoLen = ParseUtil.getByteStrLen(tempInfoLen);

        // 2. 用buffer(属性成员byte[])读取len个字节, 再将这个len个字节转换成字符串返回
        StringBuffer info = new StringBuffer();
        int readLen = 0;
        while (infoLen > 0) {
            int size = infoLen > bufferSize ? bufferSize : (int) infoLen;
            readLen = inputStream.read(buffer, 0, size);
            info.append(new String(buffer, 0, readLen));
            infoLen -= readLen;
        }
        return info.toString();
    }

    // 1. 下面这些是ISectionInfoSpeaker的接口
    // 注册一个ISectionInfoSpeaker的listener
    @Override
    public void registerListener(ISectionInfoListener listener) {
        sectionInfoListeners.add(listener);
    }

    // 将自己从ISectionInfoSpeaker的ListenerList中删除
    @Override
    public void removeListener(ISectionInfoListener listener) {
        int index = sectionInfoListeners.indexOf(listener);
        if (index > 0) {
           sectionInfoListeners.remove(index);
        }
    }

    // 发送给RC接收到的sectionList
    @Override
    public void sendSectionInfoList() {
        System.out.println("将接收到的列表信息传送给center");
        System.out.println("RT中的sectionList:" + sectionFileInfoList);
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionInfoList(sectionFileInfoList);
        }
    }

    // 发送给RC关于这个section的信息
    @Override
    public void sendSectionInfo(RecieverSectionInfo sectionInfo) {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionInfo(sectionInfo);
        }
    }

    // 发送给RC这个section已经接收完毕
    @Override
    public void sendSectionSaveOK(RecieverSectionInfo sectionInfo) {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionSaveOK(sectionInfo);
        }
    }

    // 所有的sender发送的所有section都已经接收完毕
    @Override
    public void sendAllSectionReceiveOk() {

    }

    // 2. 下面这些是ISectionReceiverSpeaker接口的方法
    // 将这个对象注册成自己的监听者
    @Override
    public void addSectionReceiverListener(ISectionReceiverListener listener) {
        sectionReceiverListeners.add(listener);
    }

    // 将这个对象从自己的监听者列表中删除
    @Override
    public void removeSectionReceiverListener(ISectionReceiverListener listener) {
        sectionReceiverListeners.remove(listener);
    }

    // 将接收到的sectionList发送给Listener(拉模式)
    @Override
    public void sendOnGetSectionList(RecieverThread recieverThread) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.onGetSectionList(recieverThread);
        }
    }

    // 表明开始接收了一个文件(实质上是接收了一个文件头部即section),
    // 所以应该在接收每一个文件头部之后调用
    @Override
    public void sendOnBeginReceiveOneSection(RecieverSectionInfo sectionInfo) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.onBeginReceiveOneSection(sectionInfo);
        }
    }

    // 表明现在正在接收文件
    // 应该在每一次read之后调用
    @Override
    public void sendOnReceiving(int receiveLen) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.onReceiving(receiveLen);
        }
    }

    // 表明一个文件片段发送完毕
    // 应该在每一个片段文件接收完毕的时候调用
    @Override
    public void sendEndReceiveOneSection(RecieverThread recieverThread) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.endReceiveOneSection(recieverThread);
        }
    }

    // 表明这个sender要发送的全部section都已经发送完毕
    // 应该在本线程结束之后调用
    @Override
    public void sendOnReceiveOver(RecieverThread recieverThread) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.onReceiveOver(recieverThread);
        }
    }

    // 表明RT接收section出现异常
    // 应该在异常处理时调用
    @Override
    public void sendOnReceiveFailure(RecieverThread recieverThread) {
        for (ISectionReceiverListener listener : sectionReceiverListeners) {
            listener.onReceiveFailure(recieverThread);
        }
    }
}
