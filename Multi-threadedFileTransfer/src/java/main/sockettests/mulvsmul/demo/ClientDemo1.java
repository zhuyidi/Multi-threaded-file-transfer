package sockettests.mulvsmul.demo;

import sockettests.mulvsmul.client.MyClient;

import java.io.IOException;

/**
 * Created by dela on 1/20/18.
 */
public class ClientDemo1 {
    public static void main(String[] args) {
        MyClient client = new MyClient();
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}