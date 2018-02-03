package sockettests.simplefiletransport;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dela on 1/22/18.
 */
public class reciever {
    public static void main(String[] args) throws IOException {
        // 连接服务器
        Socket socket = new Socket("192.168.1.85", 33000);
        // 获取输入流
        BufferedInputStream inputStream;
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        byte[] buffer = new byte[1<<15];

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要传输的文件：");
        String fileName = scanner.nextLine();
        outputStream.writeUTF(fileName);
        long fileLen = dataInputStream.readLong();

        System.out.println("请输入要存储的路径：");
        String targetName = scanner.nextLine();
        inputStream = new BufferedInputStream(socket.getInputStream());
        RandomAccessFile file = new RandomAccessFile(targetName, "rw");

        // 开始接收文件
        System.out.println("开始接收文件");
        int haveLen = 0;
        while (haveLen != fileLen) {
            int temp = inputStream.read(buffer, 0, 1<<15);
            System.out.println("本次接收到的大小：" + temp);
            file.write(buffer, 0, temp);
            haveLen += temp;
        }
        file.close();
        System.out.println("文件接收完毕");
        socket.close();
        inputStream.close();
    }
}
