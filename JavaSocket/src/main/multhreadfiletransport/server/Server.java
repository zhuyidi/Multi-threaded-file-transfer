package multhreadfiletransport.server;

import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by dela on 1/23/18.
 */

// 多线程传输总控服务器
// 决定发送多少个文件, 文件分片等    
public class Server {
    // 接收其它通信实体 的连接请求的ServerSocket
    private ServerSocket serverSocket;
    // 输出流
    private BufferedOutputStream outputStream;
    // 与接收端连接的Socket
    private Socket socket;

    public Server() {
        try {
            serverSocket = new ServerSocket(33000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() throws IOException {
        socket = serverSocket.accept();
        outputStream = new BufferedOutputStream(socket.getOutputStream());

        System.out.println("请输入要传输的文件");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();

        // 打开这个文件, 对这个文件进行分片处理, 将分片信息存储
        // 然后将这个文件的所有首部信息转换成json, 计算出json串的大小, 将json串发送接收端

        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
        long fileLen = file.length();
        RecieverSimpleInfo fileInfo = getFileInfo(fileName, fileLen);

    }

    private RecieverSimpleInfo getFileInfo(String fileName, long fileLen) {
        RecieverSimpleInfo fileInfo = new RecieverSimpleInfo(fileName, fileLen);
        List<RecieverSectionInfo> recieverSectionInfos = new LinkedList<>();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        long everySize = Long.parseLong(resourceBundle.getString("sectionSize"));
        int sectionCount = (int) (fileLen / everySize) + 1;


        long tempLen = fileLen;
        for (int i = 1; i <= sectionCount; i++) {
            RecieverSectionInfo section = new RecieverSectionInfo(fileName);
            section.setTempFileName(fileName + "." + i);
            section.setOffset(everySize * i);

            section.setSectionLen(tempLen > everySize ? everySize : tempLen);
            tempLen -= everySize;
            recieverSectionInfos.add(section);
        }

//        fileInfo.setSectionInfoList(recieverSectionInfos);
        return fileInfo;
    }
}
