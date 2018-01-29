package sockettests.mulvsmul.server;

//import sockettests.mulvsmul.common.CommonThread;

import sockettests.mulvsmul.common.CommonThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dela on 1/20/18.
 */
public class MyServer {
    // 定义保存所有Socket的ArrayList, 并将其包装
    public static List<Socket> socketList =
            Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        // 接收其它通信实体连接请求的ServerSocket
        // 33000为有效端口号
        ServerSocket serverSocket = new ServerSocket(33000);

        while (true) {
            // serverSocket一直处于监听状态, 等待客户端的连接,
            // 如果没有连接, 那么该方法会一直处于等待状态, 当前线程处于阻塞状态
            // 每连接上一个客户端, 就返回一个与该客户端的Socket对应的Socket
            // 对于每一个TCP连接, 有两个Socket, 一个在服务端, 另一个在客户端
            Socket socket = serverSocket.accept();
            // 将连接上的客户端的Socket保存在socketList中
            socketList.add(socket);
            // 然后给这个客户端分配一个线程维持该客户端与服务端的通信
            new Thread(new CommonThread(socket)).start();
        }
    }

}