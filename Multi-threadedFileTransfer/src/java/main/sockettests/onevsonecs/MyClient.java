package sockettests.onevsonecs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by dela on 1/20/18.
 */
public class MyClient {
    private Socket socket;

    public MyClient(){
        try {
            socket = new Socket("localhost", 54188);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);
            String comand = sc.nextLine();

            while(!comand.equals("q")){
                dos.writeUTF(comand);
                System.out.println("反馈消息："+dis.readUTF());
                comand = sc.nextLine();
            }
            dos.writeUTF(comand);
            System.out.println("反馈消息："+dis.readUTF());
            dis.close();
            dos.close();
            sc.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
