import multhreadfiletransport.client.sender.SenderCenter;

import java.io.IOException;

/**
 * Created by dela on 1/30/18.
 */
public class sendTTest {
    public static void main(String[] args) {
        SenderCenter senderCenter = new SenderCenter();
        try {
            senderCenter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
