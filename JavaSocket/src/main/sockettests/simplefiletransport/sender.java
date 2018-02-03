package sockettests.simplefiletransport;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dela on 1/22/18.
 */
public class sender {
    public static void main(String[] atgs) throws IOException {
        ServerSocket serverSocket = new ServerSocket(33000);
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        byte[] buffer = new byte[1<<15];

        socket = serverSocket.accept();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());

        String fileName = inputStream.readUTF();
        File file = new File(fileName);
        long fileLen = file.length();
        dataOutputStream.writeLong(fileLen);

        outputStream = new BufferedOutputStream(socket.getOutputStream());
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");

        System.out.println("开始传输文件");
        long overLen = fileLen;
        while (overLen > 0) {
            int size = overLen > 1<<15 ? 1<<15 : (int) overLen;
            int temp = randomAccessFile.read(buffer, 0, size);
            System.out.println("读到的文件大小：" + temp);
            outputStream.write(buffer, 0, size);
            overLen -= size;
        }
        System.out.println("传输完毕");
        outputStream.close();
        socket.close();
    }
}
