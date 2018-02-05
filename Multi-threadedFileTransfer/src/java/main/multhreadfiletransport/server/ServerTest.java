package multhreadfiletransport.server;

import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by dela on 1/23/18.
 */

// 多线程传输总控服务器
// 决定发送多少个文件, 文件分片等    
public class ServerTest {
    // 接收其它通信实体 的连接请求的ServerSocket
    private ServerSocket serverSocket;
    // 输出流
    private BufferedOutputStream outputStream;
    // 与接收端连接的Socket
    private Socket socket;

    public ServerTest() {
        try {
            serverSocket = new ServerSocket(33000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() throws IOException {
        socket = serverSocket.accept();
//        outputStream = new BufferedOutputStream(socket.getOutputStream());
//
//        System.out.println("请输入要传输的文件");
//        Scanner scanner = new Scanner(System.in);
//        String filePath = scanner.nextLine();
//
//        // 打开这个文件, 对这个文件进行分片处理, 将分片信息存储
//
//        // 设置到simpleInfo里面的文件名是不带路径的, 仅含有文件名.
//        // 所以在sender段进行处理的时候, 先从配置文件中读取目标路径, 后面再进行更改
//        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
//        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
//        long fileLen = file.length();
//        RecieverSimpleInfo fileInfo = getFileInfo(fileName, fileLen);
//
//        // TODO
//        // 这里写到了一个文件的所有信息(simpleInfo)已经构造完成.
//        // (但是服务器端发送给sender的信息应该是分片列表信息,
//        // 而服务器端发送给recievercenter的信息应该是文件列表信息(fileInfo仅含文件名和文件大小)
    }

    // 给一个文件名和一个文件长度, 按照配置文件中的分片大小进行分片,
    // 将分好的分片全部放入simpleInfo里的linkedlist, 返回这个simpleInfo
    private RecieverSimpleInfo getFileInfo(String fileName, long fileLen) {
        RecieverSimpleInfo fileInfo = new RecieverSimpleInfo(fileName, fileLen);
        LinkedList<RecieverSectionInfo> recieverSectionInfos = new LinkedList<>();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        long everySize = Long.parseLong(resourceBundle.getString("sectionSize"));
        int sectionCount = (int) (fileLen / everySize) + 1;


        // 设置每个分片的信息(包括分片文件名, 分片文件大小等)
        long tempLen = fileLen;
        for (int i = 1; i <= sectionCount; i++) {
            RecieverSectionInfo section = new RecieverSectionInfo(fileName);
            section.setTempFileName(fileName + "." + i);
            section.setOffset(everySize * i);

            section.setSectionLen(tempLen > everySize ? everySize : tempLen);
            tempLen -= everySize;
            recieverSectionInfos.add(section);
        }

        fileInfo.setSectionInfoList(recieverSectionInfos);
        return fileInfo;
    }

    public static void main(String[] args) {
        try {
            new ServerTest().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
