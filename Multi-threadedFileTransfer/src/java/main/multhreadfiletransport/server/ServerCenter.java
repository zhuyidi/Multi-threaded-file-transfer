package multhreadfiletransport.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dela on 2/5/18.
 */

// 真正服务器的处理中心
public class ServerCenter {
    // 保存所有的客户端的socket的List(单例的), 并用集合操作类将其包装成线程安全的.
    public static List<Socket> socketList =
            Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;

    public ServerCenter() {
        try {
            serverSocket = new ServerSocket(33000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 启动服务器的main()方法
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(33000);

        while (true) {
            // 等待客户端的连接
            Socket socket = serverSocket.accept();
            socketList.add(socket);
            // 然后给这个客户端分配一个线程维持该客户端与服务端的通信
            new Thread(new ServerThread(socket)).start();
        }
    }
}
