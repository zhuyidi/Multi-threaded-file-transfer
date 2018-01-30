import multhreadfiletransport.client.sender.SenderCenter;

import java.io.IOException;

/**
 * Created by dela on 1/29/18.
 */
public class senderTest {
    public static void main(String[] args) {
        SenderCenter senderCenter = new SenderCenter();
        try {
            senderCenter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
