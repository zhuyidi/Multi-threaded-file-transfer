package multhreadfiletransport.client.sender;

import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by dela on 1/23/18.
 */

// 多例的发送端, 由总控Server决定哪些客户机作为Sender
public class SenderCenter {
    private Socket serverSocket;  // 与服务器相连的socket
    private Socket recieverSocket;  // 与接收端相连的socket
    private BufferedInputStream serverInputStream;
    private BufferedOutputStream recieveOutputStream;
    private String sectionInfoString;  // server端发送过来的本sender需要发送的sectionInfo列表
    private List<RecieverSectionInfo> sectionInfoList;  // 将上面的字符串解成section对象
    private byte[] buffer;
    private int headerSize;
    private int bufferSize;
    private String sendPath;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        headerSize = Integer.parseInt(resourceBundle.getString("headerSize"));
        bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        sendPath = resourceBundle.getString("sendPath");

        buffer = new byte[bufferSize];
        sectionInfoList = new ArrayList<>();
    }

    public SenderCenter() {
//        try {
//            serverSocket = new Socket("127.0.0.1", 33000);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public SenderCenter(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() throws IOException {
        // 1. 读取来自服务器端的sectionlist信息
//        recieverFormServer();
        testInput();

        // 2. 连接RS, 并发送总体的sectionlist
        connectRecieverServer();

        // 3. 发送文件数据
        sendSection();
    }

    // 临时测试方法
    public void testInput() {
        Scanner scanner = new Scanner(System.in);
        int sectionCount = scanner.nextInt();
        for (int i = 0; i < sectionCount; i++) {
            RecieverSectionInfo sectionInfo = new RecieverSectionInfo(scanner.next(),
                    scanner.next(), scanner.nextLong(), scanner.nextLong());
            sectionInfoList.add(sectionInfo);
        }
        sectionInfoString = PackageUtil.packageSectionInfoList(sectionInfoList);
    }


    public void sendSection() throws IOException {
        for (RecieverSectionInfo sectionInfo : sectionInfoList) {
            // 发送每一个section的头部信息
            String sectionInfoStr = PackageUtil.packageSectionInfo(sectionInfo);
            byte[] sendHeader = PackageUtil.addHeader(sectionInfoStr);
            recieveOutputStream.write(sendHeader);

            // 发送文件
            // 这里的文件名应该是targetFileName, 应该按照server发送过来的信息打开目标文件,
            // 移动文件指针到offset那个地方, 然后读取sectionlen个字节发送给RT
            // 传输文件头部
            String fileName = sendPath + sectionInfo.getTargetFileName();
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
            randomAccessFile.seek(sectionInfo.getOffset());

            // 传输文件内容
            long overLen = sectionInfo.getSectionLen();
            while (overLen > 0) {
                int size = overLen > bufferSize ? bufferSize : (int) overLen;
                int temp = randomAccessFile.read(buffer, 0, size);
                recieveOutputStream.write(buffer, 0, size);
                recieveOutputStream.flush();
                overLen -= size;
            }
        }
    }

    public void recieverFormServer() throws IOException {
        // 接收sectionList信息, 先接收size个字节, 然后求出后面的len, 再接收后面的信息
        // 先接收headerSize求出后面info的len, 再接收len个byte, 最后转换成字符串返回

        serverInputStream = new BufferedInputStream(serverSocket.getInputStream());

        //1. 先求出len
        byte[] tempInfoLen = new byte[headerSize];
        int temp = 0;
        while (temp != headerSize) {
            temp = serverInputStream.read(tempInfoLen, temp, headerSize - temp);
        }
        long infoLen = ParseUtil.getByteStrLen(tempInfoLen);

        // 2. 用buffer(属性成员byte[])读取len个字节, 再将这个len个字节转换成字符串返回
        int readLen = 0;
        temp = 0;
        while (temp != infoLen) {
            readLen = serverInputStream.read(buffer, 0, bufferSize);
            temp += readLen;
            sectionInfoString += new String(buffer, 0, readLen);
        }

        // 通过解析工具, 将字符串解析成sectionList
        sectionInfoList = ParseUtil.parseStringToSectionInfoList(sectionInfoString);

        // 然后与server端的通信借书, 关闭资源
        if (serverInputStream != null) {
            serverInputStream.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public void connectRecieverServer() throws IOException {
        // 1. 连接RS
        recieverSocket = new Socket("127.0.0.1", 33001);
        recieveOutputStream = new BufferedOutputStream(recieverSocket.getOutputStream());

        // 2. 发送总体的sectionList
        byte[] sectionList = PackageUtil.addHeader(sectionInfoString);
        recieveOutputStream.write(sectionList, 0, sectionList.length);
    }
}
