package sockettests.mulvsmul.client;

//import sockettests.mulvsmul.common.CommonThread;

import java.io.*;
import java.net.Socket;

/**
 * Created by dela on 1/20/18.
 */
public class MyClient {
    Socket socket;  // 与服务器端通信的套接字
    String message; // 服务器端发送过来的消息
    DataOutputStream outputStream; // 服务器端套接字对应的输出流
    DataInputStream inputStream; // 当前客户端从控制台输入的输入流

    public void start() throws IOException {
        // 连接服务器
        socket = new Socket("127.0.0.1", 33000);
        // 为客户端实例化一个处理线程, 持续监听从服务端发过来的消息
//        new Thread(new CommonThread(socket)).start();
        // 获取该Socket对应的输出流
        outputStream = new DataOutputStream(socket.getOutputStream());
        // 实例化控制台输入流
        inputStream = new DataInputStream(System.in);

        // 当前客户端不断读取从控制台的输入, 然后发送给服务端
        while ((message = inputStream.readLine()) != null) {
            // 将从控制台读到的信息通过服务端的输出流写给服务端
            outputStream.writeUTF(message);
        }
    }
}
