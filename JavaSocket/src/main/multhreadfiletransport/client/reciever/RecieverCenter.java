package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;
import multhreadfiletransport.observer.ISectionInfoListener;
import multhreadfiletransport.observer.ISectionInfoSpeaker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.Scanner;

/**
 * Created by dela on 1/23/18.
 */
// 接收中心
// 接收中心首先收到文件的信息, 然后开启接收服务器, 在接收服务器中开启接收线程
public class RecieverCenter implements ISectionInfoListener, Runnable{
    private Socket socket;    // 与server连接的socket
    private BufferedInputStream inputStream;    // 与server连接的socket的输入流
    private List<FileInfo> fileInfoList;    // 从server那里接收到的文件列表
    private RecieverMap recieverMap;    // 存储文件所有信息的map类
    private int senderCount;    // 服务器分配的sender的数量
    private RecieverServer recieverServer;  // RT持续accept, 连接sender, 一旦连接完璧, 立即关闭

    {
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

    public void start() throws IOException {
        // 连接服务器, 然后接收服务器发来的目标文件列表和服务器分配的sender数量.
        // 但是此时是模拟服务器发送数据, 所有数据从键盘输入,
        // 所以留出recieve接口, 暂时在里面实现从键盘输入, 之后要改为真正从服务器接收数据
        if (null == socket) {
            socket = new Socket("127.0.0.1", 33000);
        }
        recieveFromServer();

        // 将从服务器接收的文件列表信息保存一份List在自己的实例中, 并进行map初始化
        recieverMap.initRecieveMap(fileInfoList);

    }

    public void recieveFromServer() {
        inputTargetFileInfo();
    }

    public void inputTargetFileInfo() {
        Scanner scanner = new Scanner(System.in);

        senderCount = scanner.nextInt();
        for (int i = 0; i < 1; i++) {
            FileInfo fileInfo = new FileInfo(scanner.next(), scanner.nextLong());
            fileInfoList.add(fileInfo);
        }
    }

    @Override
    public void run() {
        // new出RecieverServer类, 进行持续监听
        recieverServer = new RecieverServer(this);
        try {
            recieverServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void getSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        // 将这个sectionlist进行解析, 填充到map类里面
        recieverMap.setSectionInfoList(sectionInfoList);
    }

    @Override
    public synchronized void getSectionInfo(RecieverSectionInfo sectionInfo) {
        // 这个section进行解析, 填充map类(主要是进行recievemark的标记)
    }

    @Override
    public synchronized void getSectionSaveOK(RecieverSectionInfo sectionInfo) {
        // 将这个section里面的所有mark和savelen等信息都保存到
    }

}
