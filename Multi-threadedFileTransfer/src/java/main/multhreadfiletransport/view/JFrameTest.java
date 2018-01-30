package multhreadfiletransport.view;

import multhreadfiletransport.client.reciever.RecieverCenter;
import multhreadfiletransport.client.reciever.RecieverMap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by dela on 1/30/18.
 */
public class JFrameTest {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(400, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        RecieverCenter recieverCenter = new RecieverCenter();
        try {
            recieverCenter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RecieverMap recieverMap = recieverCenter.getRecieverMap();
        ReceiveProgressDialog receiveProgressDialog = new ReceiveProgressDialog(jFrame);
        receiveProgressDialog.initView(recieverCenter, recieverMap);

        receiveProgressDialog.setVisible(true);

        System.out.println("窗口已经展现出来了!");
    }
}
