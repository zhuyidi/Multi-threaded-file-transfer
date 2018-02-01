package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;
import multhreadfiletransport.observer.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

/**
 * Created by dela on 1/23/18.
 */
// 接收中心
// 接收中心首先收到文件的信息, 然后开启接收服务器, 在接收服务器中开启接收线程
public class RecieverCenter implements ISectionInfoListener, IFileJoinSpeaker,
        IFileReceiverCenterListener, Runnable{

    private Socket socket;    // 与server连接的socket
    private BufferedInputStream inputStream;    // 与server连接的socket的输入流
    private List<FileInfo> fileInfoList;    // 从server那里接收到的文件列表
    private RecieverMap recieverMap;    // 存储文件所有信息的map类
    private int senderCount;    // 服务器分配的sender的数量
    private RecieverServer recieverServer;  // RT持续accept, 连接sender, 一旦连接完璧, 立即关闭
    private List<IFileJoinListener> fileJoinListenerList;
    private String targetPath;
    private byte[] buffer;
    private int bufferSize;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        targetPath = resourceBundle.getString("targetPath");
        bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        buffer = new byte[bufferSize];
        fileJoinListenerList = new ArrayList<>();
        fileInfoList = new ArrayList<>();
        recieverMap = new RecieverMap(this);
    }

    public RecieverCenter() {
        try {
            socket = new Socket("127.0.0.1", 33000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RecieverCenter(Socket socket) {
        this.socket = socket;
    }

    public RecieverCenter(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RecieverMap getRecieverMap() {
        return recieverMap;
    }

    public RecieverServer getRecieverServer() {
        return recieverServer;
    }

    public void start() throws IOException {
        // 连接服务器, 然后接收服务器发来的目标文件列表和服务器分配的sender数量.
        // 但是此时是模拟服务器发送数据, 所有数据从键盘输入,
        // 所以留出recieve接口, 暂时在里面实现从键盘输入, 之后要改为真正从服务器接收数据
        if (null == socket) {
            socket = new Socket("127.0.0.1", 33000);
        }

        // 从服务器接收将要接收的targetfileList, 并启动RS
        recieveFromServer();
        new Thread(this).start();

        // 将从服务器接收的文件列表信息保存一份List在自己的实例中, 并进行map初始化
        recieverMap.initRecieveMap(fileInfoList);

        // 启动服务器之后, 就开始展现面板
    }

    public void recieveFromServer() {
        inputTargetFileInfo();
    }

    public void inputTargetFileInfo() {
        Scanner scanner = new Scanner(System.in);
        senderCount = scanner.nextInt();
        for (int i = 0; i < 1; i++) {
            FileInfo fileInfo = new FileInfo(scanner.next(), scanner.nextLong());
            System.out.println(fileInfo.toString());
            fileInfoList.add(fileInfo);
        }
    }

    // 接收完所有的文件, 开始文件合并
    public void joinFile() throws IOException {
        // 合并文件要先拿到map里面所有的simpleFile, 然后拿到每一个simple的sectionList, 然后再分别合并.
        Map<String, RecieverSimpleInfo> fileMap = recieverMap.getFileMap();

        int joinCount = 0;
        for (String targetFileName : fileMap.keySet()) {
            RecieverSimpleInfo simpleInfo = fileMap.get(targetFileName);
            joinOneSimpleFile(simpleInfo);
            joinCount++;
            // 合并完一个文件, 将当前的合并文件的数量发送给view, 然后通知view已经合并完一个targetFile了
            for (IFileJoinListener listener : fileJoinListenerList) {
                listener.onGetJoinCount(joinCount);
                listener.onJoinOne();
            }
        }

        // 所有的文件全部合并完, 通知监听者们
        for (IFileJoinListener listener : fileJoinListenerList) {
            listener.onAllDone();
        }
    }

    // 合并一个targetFile
    public void joinOneSimpleFile(RecieverSimpleInfo simpleInfo) throws IOException {
        String targetFileName = targetPath + simpleInfo.getTargetFileName();
        RandomAccessFile targetFile = new RandomAccessFile(targetFileName, "rw");

        List<RecieverSectionInfo> sectionInfos = simpleInfo.getSectionInfoList();
        for (RecieverSectionInfo sectionInfo : sectionInfos) {
            String sectionName = targetPath + sectionInfo.getTempFileName();
            RandomAccessFile sectionFile = new RandomAccessFile(sectionName, "rw");
            int readlen = 0;
            int temp = 0;
            while (readlen != sectionInfo.getSectionLen()) {
                temp = sectionFile.read(buffer, 0, bufferSize);
                targetFile.write(buffer, 0, temp);
                readlen += temp;
            }
            sectionFile.close();
            File sectionTempFile = new File(sectionName);
            sectionTempFile.delete();
        }
        targetFile.close();
    }

    @Override
    public void run() {
        // new出RecieverServer类, 进行持续监听
        recieverServer = new RecieverServer(this, senderCount);
        recieverServer.setTargetFileCount(recieverMap.getTargetFileCount());
        recieverServer.startReceive();
    }

    // 对于listener里面这一系列方法对map进行一系列的操作的时候, 应该是对方法加锁的,
    // 但是由于现在在map类里面对fileMap这个hashmap进行了集合类的线程安全操作, 保证map的线程安全性
    // 所以在这里先去掉synchronized关键字进行测试, 看会不会出现线程不安全的问题. 出现问题再解决问题

    // 将这个sectionlist进行解析, 填充到map类里面
    @Override
    public void getSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        recieverMap.setSectionInfoList(sectionInfoList);
    }

    // 这个section进行解析, 填充map类(主要是进行recievemark的标记)
    @Override
    public  void getSectionInfo(RecieverSectionInfo sectionInfo) {
        recieverMap.setSectionRecieveMark(sectionInfo);
    }

    // 接收分片文件信息完成, 这只targetFile的len
    // 将这个section里面的所有savemark和savelen等信息都保存到map中
    @Override
    public void getSectionSaveOK(RecieverSectionInfo sectionInfo) {
        recieverMap.setSaveMarkAndLen(sectionInfo);
    }

    // 当所有的section都发送完毕时, RC就开始合并文件
    @Override
    public void getAllSectionSaveOk() {
        // 通知view已经开始合并
        for (IFileJoinListener listener : fileJoinListenerList) {
            listener.onBeginJoin();
        }

        try {
            joinFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFileJoinListener(IFileJoinListener listener) {
        fileJoinListenerList.add(listener);
    }

    @Override
    public void removeFileJoinListener(IFileJoinListener listener) {
        fileJoinListenerList.remove(listener);
    }

    @Override
    public void dealStopReceive() {

    }
}
