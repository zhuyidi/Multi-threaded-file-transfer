package multhreadfiletransport.server.core;

import multhreadfiletransport.server.distribution.ResourceTable;

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
            Collections.synchronizedList(new ArrayList<Socket>());
    private ServerSocket serverSocket;
    private ResourceTable resourceTable;

    public ServerCenter() {
        try {
            // 创建Serverocket
            serverSocket = new ServerSocket(33000);
            // 启动redis服务
            resourceTable = new ResourceTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Socket> getSocketList() {
        return socketList;
    }

    public static void setSocketList(List<Socket> socketList) {
        ServerCenter.socketList = socketList;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ResourceTable getResourceTable() {
        return resourceTable;
    }

    public void setResourceTable(ResourceTable resourceTable) {
        this.resourceTable = resourceTable;
    }

    // 启动服务器的main()方法
    public static void main(String[] args) throws IOException {
        ServerCenter serverCenter = new ServerCenter();

        while (true) {
            // 等待客户端的连接
            Socket socket = serverCenter.getServerSocket().accept();
            socketList.add(socket);
            // 然后给这个客户端分配一个线程维持该客户端与服务端的通信
            new Thread(new ServerThread(socket)).start();
        }
    }
}
