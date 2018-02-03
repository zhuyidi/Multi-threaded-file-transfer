package multhreadfiletransport.client.sender;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by dela on 1/23/18.
 */

// 多例的发送端, 由总控Server决定哪些客户机作为Sender
public class SenderCenter {
    private Socket socket;

    public SenderCenter() {
        try {
            socket = new Socket("127.0.0.1", 33001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SenderCenter(Socket socket) {
        this.socket = socket;
    }

}
