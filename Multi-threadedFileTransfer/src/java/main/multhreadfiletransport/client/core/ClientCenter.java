package multhreadfiletransport.client.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by dela on 2/5/18.
 */
public class ClientCenter {
    private Socket socket;
    private DataInputStream inputStream;
    private int clientId;   // 该客户端的ID

    // 为每个客户端分配的ID
    private static int CLIENT_ID = 1;

    public ClientCenter() {
        // 为客户端分配ID
        this.clientId = CLIENT_ID;
        CLIENT_ID++;
        // 连接服务端
        try {
            socket = new Socket("127.0.0.1", 33000);
            inputStream = new DataInputStream(socket.getInputStream());
            // 每个客户端登录上线之后, 就启动监听线程
            new Thread(new ClientThread(socket, clientId)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

    }

}
