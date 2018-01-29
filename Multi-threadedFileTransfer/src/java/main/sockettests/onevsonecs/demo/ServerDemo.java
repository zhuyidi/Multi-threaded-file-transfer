package sockettests.onevsonecs.demo;

import sockettests.onevsonecs.MyServer;

/**
 * Created by dela on 1/20/18.
 */
public class ServerDemo {
    public static void main(String[] args) {
        MyServer server = new MyServer();
        server.start();
    }
}
