package multhreadfiletransport.client.reciever;

import multhreadfiletransport.observer.IReceiverServerListener;
import multhreadfiletransport.observer.IReceiverServerSpeaker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dela on 1/23/18.
 */
public class RecieverServer implements IReceiverServerSpeaker {
    private ServerSocket serverSocket;
    private boolean goon;
    private int senderCount;
    private List<Socket> socketList;
    private RecieverCenter recieverCenter;
    private List<IReceiverServerListener> serverListeners;
    private int targetFileCount;
    private boolean initState;

    {
        try {
            serverSocket = new ServerSocket(33001);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverListeners = new ArrayList<>();
        initState = true;
    }

    public RecieverServer(RecieverCenter recieverCenter) {
        this.recieverCenter = recieverCenter;
        goon = true;
    }

    public RecieverServer(RecieverCenter recieverCenter, int senderCount) {
        this.recieverCenter = recieverCenter;
        this.senderCount = senderCount;
        goon = true;
    }

    public int getTargetFileCount() {
        return targetFileCount;
    }

    public void setTargetFileCount(int targetFileCount) {
        this.targetFileCount = targetFileCount;
    }

    public void startReceive() throws IOException {
        // RS持续监听, 当有一个sender连接, 就分发一个线程RT
        int time = 0;
        while (goon) {
            Socket socket = serverSocket.accept();
            System.out.println("已经连接上一个sender, 产生了一个线程");

            RecieverThread recieverThread = new RecieverThread(recieverCenter, socket);
            recieverThread.setTargetFileCount(targetFileCount);
//            Thread thread = new Thread(recieverThread);
//            thread.setName("hahaha");
//            thread.start();

            haveNewReceiverThread(recieverThread);
            time++;

            if(time == senderCount) {
                goon = false;
            }
        }
    }

//    @Override
//    public void run() {
//        try {
//            startReceive();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void addFileReceiverListener(IReceiverServerListener serverListener) {
        serverListeners.add(serverListener);
    }

    @Override
    public void removeFileReceiverListener(IReceiverServerListener serverListener) {
        serverListeners.remove(serverListener);
    }

    @Override
    public void haveNewReceiverThread(RecieverThread recieverThread) {
        System.out.println("将这个线程传递给view");

        if (serverListeners.size() <= 0) {
            return;
        }

        if (initState) {
            for (IReceiverServerListener listener : serverListeners) {
                listener.initTragetFileCount(targetFileCount);
            }
            initState = false;
        }

        for (IReceiverServerListener listener : serverListeners) {
            listener.dealNewReceiverThread(recieverThread);
        }
    }
}
