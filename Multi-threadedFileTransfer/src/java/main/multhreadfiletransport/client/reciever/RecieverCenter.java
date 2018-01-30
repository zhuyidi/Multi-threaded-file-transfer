package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.IFileJoinListener;
import multhreadfiletransport.observer.IFileJoinSpeaker;
import multhreadfiletransport.observer.IFileReceiverCenterListener;
import multhreadfiletransport.observer.ISectionInfoListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    {
        fileJoinListenerList = new ArrayList<>();
        fileInfoList = new ArrayList<>();
        recieverMap = new RecieverMap();
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
        recieveFromServer();
        new Thread(this).start();
        // 将从服务器接收的文件列表信息保存一份List在自己的实例中, 并进行map初始化
        recieverMap.initRecieveMap(fileInfoList);

        // 启动服务器之后, 就开始展现面板

        System.out.println("center从server接收信息并填充map成功!");

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

    @Override
    public void run() {
        // new出RecieverServer类, 进行持续监听
        try {
            recieverServer = new RecieverServer(this);
            recieverServer.setTargetFileCount(recieverMap.getTargetFileCount());
            recieverServer.startReceive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 对于listener里面这一系列方法对map进行一系列的操作的时候, 应该是对方法加锁的,
    // 但是由于现在在map类里面对fileMap这个hashmap进行了集合类的线程安全操作, 保证map的线程安全性
    // 所以在这里先去掉synchronized关键字及你想嗯测试, 看会不会出现线程不安全的问题. 出现问题再解决问题
    @Override
    public void getSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        // 将这个sectionlist进行解析, 填充到map类里面
        recieverMap.setSectionInfoList(sectionInfoList);
    }

    @Override
    public  void getSectionInfo(RecieverSectionInfo sectionInfo) {
        // 这个section进行解析, 填充map类(主要是进行recievemark的标记)
        recieverMap.setSectionRecieveMark(sectionInfo);
    }

    @Override
    public synchronized void getSectionSaveOK(RecieverSectionInfo sectionInfo) {
        // 接收分片文件信息完成, 这只targetFile的len
        // 将这个section里面的所有savemark和savelen等信息都保存到map中
        recieverMap.setSaveMarkAndLen(sectionInfo);
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