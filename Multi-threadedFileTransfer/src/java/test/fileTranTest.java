import multhreadfiletransport.client.reciever.RecieverCenter;
import multhreadfiletransport.client.reciever.RecieverMap;
import multhreadfiletransport.view.ReceiveProgressDialog;

import java.io.IOException;

/**
 * Created by dela on 1/27/18.
 */
public class fileTranTest {
    public static void main(String[] args) {
        RecieverCenter recieverCenter = new RecieverCenter();
        try {
            recieverCenter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RecieverMap recieverMap = recieverCenter.getRecieverMap();
        ReceiveProgressDialog receiveProgressDialog = new ReceiveProgressDialog();
        receiveProgressDialog.initView(recieverCenter, recieverMap);

        System.out.println("窗口已经展现出来了!");
    }
}
