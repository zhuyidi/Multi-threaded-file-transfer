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

    public void startReceive() {
        // RS持续监听, 当有一个sender连接, 就分发一个线程RT
        // 接收上一个sender之后, 就new出一个RT, 但是此处并不start这个RT,
        // 而是由haveNewReceiverThread()通知view, 在view中启动RT
        int time = 0;
        while (goon) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                close();
            }
            RecieverThread recieverThread = new RecieverThread(recieverCenter, socket);
            recieverThread.setTargetFileCount(targetFileCount);
            // 通知view, 在view中启动RT
            haveNewReceiverThread(recieverThread);

            // 检测当前接收的sender是否已达center从server接收的sender的数量,
            // 达到之后就关闭RS
            time++;
            if(time == senderCount) {
                goon = false;
            }
        }
        close();
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        if (serverListeners.size() <= 0) {
            return;
        }

        // 判断是否是接收到的第一个线程, 如果是第一个线程, 在view中要展示出总进度条, 之后不再显示
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
