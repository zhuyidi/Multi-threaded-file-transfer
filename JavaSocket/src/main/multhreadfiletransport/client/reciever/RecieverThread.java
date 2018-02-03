package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.ISectionInfoListener;
import multhreadfiletransport.observer.ISectionInfoSpeaker;
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
public class RecieverThread implements Runnable, ISectionInfoSpeaker {
    private Socket socket;
    private BufferedInputStream inputStream;
    private int headerSize;
    private int bufferSize;
    private byte[] buffer;
    private List<RecieverSectionInfo> sectionFileInfoList;
    private List<ISectionInfoListener> sectionInfoListeners;
    private String targetPath;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        this.headerSize = Integer.parseInt(resourceBundle.getString("headerSize"));
        this.bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        buffer = new byte[bufferSize];
        sectionInfoListeners = new ArrayList<>();
        targetPath = resourceBundle.getString("targetPath");
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

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
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

    @Override
    public void run() {
        // 先接收整个分片文件列表, 然后再接收每个分片
        try {
            // 1. 接收整个分片文件列表
            recieveSectionInfoList();

            // 2. 接收每一个分片文件
            int sectionCount = sectionFileInfoList.size();
            while(sectionCount != 0) {
                recieveSection();
            }

            // 3. 接收完毕, 关闭输入流和socket
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recieveSectionInfoList() throws IOException {
        // 接收分片文件列表包头信息
        String sectionListInfo = getHeaderInfo();

        // 接下来将收到的分片信息列表进行解析并通过观察者模式传给RCenter, 然后整合到RC里的map中
        // 1. 解析
        sectionFileInfoList = ParseUtil.parseStringToSectionInfoList(sectionListInfo);
        // 2. 发送给centersectionList, 以便center进行填充
        sendSectionInfoList();
    }

    public void recieveSection() throws IOException {
        // 先接收分片包头, 再解析, 然后通知RC, 再接收分片文件正式内容
        RecieverSectionInfo sectionInfo;

        // 1. 接收包头
        String sectionString = getHeaderInfo();
        // 2. 解析并通知RC
        sectionInfo = ParseUtil.parseStringToSectionInfo(sectionString);
        sectionInfo.setRecieveMark(true);
        sendSectionInfo(sectionInfo);

        // 3. 接收分片文件正式内容, 并传送一个信息告诉center, 已经接收完毕一个文件
        recieveFile(sectionInfo);
        sectionInfo.setSaveMark(true);
        sendSectionSaveOK(sectionInfo);
    }

    public void recieveFile(RecieverSectionInfo sectionInfo) throws IOException {
        String filePath = targetPath + sectionInfo.getTargetFileName();
        RandomAccessFile file = new RandomAccessFile(sectionInfo.getTempFileName(), "rw");

        // 开始接收文件
        System.out.println("开始接收文件");
        int haveLen = 0;
        while (haveLen != sectionInfo.getSectionLen()) {
            int temp = inputStream.read(buffer, 0, bufferSize);
            System.out.println("本次接收到的大小：" + temp);
            file.write(buffer, 0, temp);
            haveLen += temp;
        }
        file.close();
    }

    // 读取头部信息(包括大包头(section信息列表), 小包头(每个section信息))
    public String getHeaderInfo() throws IOException {
        // 先接收headerSize求出后面info的len, 再接收len个byte, 最后转换成字符串返回
        String info = new String();

        //1. 先求出len
        byte[] tempInfoLen = new byte[headerSize];
        int temp = 0;
        while (temp != headerSize) {
            temp = inputStream.read(tempInfoLen, temp, headerSize - temp);
        }
        long infoLen = ParseUtil.getByteStrLen(tempInfoLen);

        // 2. 用buffer(属性成员byte[])读取len个字节, 再将这个len个字节转换成字符串返回
        int readLen = 0;
        temp = 0;
        while (temp != infoLen) {
            readLen = inputStream.read(buffer, 0, bufferSize);
            temp += readLen;
            info += new String(buffer, 0, readLen);
        }
        return info;
    }

    @Override
    public void registerListener(ISectionInfoListener listener) {
        sectionInfoListeners.add(listener);
    }

    @Override
    public void removeListener(ISectionInfoListener listener) {
        int index = sectionInfoListeners.indexOf(listener);
        if (index > 0) {
           sectionInfoListeners.remove(index);
        }
    }

    @Override
    public void sendSectionInfoList() {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionInfoList(sectionFileInfoList);
        }
    }

    @Override
    public void sendSectionInfo(RecieverSectionInfo sectionInfo) {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionInfo(sectionInfo);
        }
    }

    @Override
    public void sendSectionSaveOK(RecieverSectionInfo sectionInfo) {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getSectionSaveOK(sectionInfo);
        }
    }
}
