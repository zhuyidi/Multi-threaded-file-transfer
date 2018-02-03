package sockettests.onevsonecs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dela on 1/19/18.
 */
public class MyServer {
    private static ServerSocket serverSocket;

    public MyServer() {
        try {
            serverSocket = new ServerSocket(54188);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start(){
        Socket socket = null;
        try {
            socket = serverSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }

        DataOutputStream dos;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while(true){
                String r = dis.readUTF();
                dos.writeUTF("["+r+"]");
            }
        } catch (IOException e) {
        }
    }
}
