package multhreadfiletransport.server;

import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.observer.serverandclient.IMessageSpeaker;
import multhreadfiletransport.util.ParseUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by dela on 2/5/18.
 */
public class ServerThread implements Runnable, IMessageSpeaker {

    private Socket socket;
    private DataInputStream inputStream;

    private boolean goon;
    private boolean normal;
    private List<IMessageListener> listeners;
    private String message;
    private ServerDealMessage serverDealMessage;

    private byte[] buffer;
    private int bufferSize;
    private int headerSize;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        headerSize = Integer.parseInt(resourceBundle.getString("headerSize"));
        listeners = new ArrayList<>();
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        goon = true;
        normal = true;
        serverDealMessage = new ServerDealMessage(this);
        addListener(serverDealMessage);
    }

    @Override
    public void run() {
        // serverThread里面持续监听来自客户端的消息
        while (goon) {
            try {
                normal = false;
                message = readMessageFromClient();
                normal = true;
            } catch (IOException e) {
                // 如果捕获到异常, 要检查一下normal标志. 如果为true, 说明客户端为正常下线,
                // 如果为false, 说明客户端是异常下线.
                if (normal) {
                    // 正常下线的处理
                } else {
                    // 异常下线的处理
                }
                close();
            }
        }
    }


    public void close() {
        goon = false;
        ServerCenter.socketList.remove(socket);

        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
        }
    }

    public String readMessageFromClient() throws IOException {
        // 求出len
        byte[] tempInfoLen = new byte[headerSize];
        int temp = 0;
        int readlen = 0;

        while (temp != headerSize) {
            readlen = inputStream.read(tempInfoLen, temp, headerSize - temp);
            temp += readlen;
        }
        long infoLen = ParseUtil.getByteStrLen(tempInfoLen);

        // 读取len个字节
        StringBuffer info = new StringBuffer();
        int readLen = 0;
        while (infoLen > 0) {
            int size = infoLen > bufferSize ? bufferSize : (int) infoLen;
            readLen = inputStream.read(buffer, 0, size);
            info.append(new String(buffer, 0, readLen));
            infoLen -= readLen;
        }
        return info.toString();
    }

    @Override
    public void addListener(IMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IMessageListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @Override
    public void sendMessage() {
        for (IMessageListener listener : listeners) {
            listener.getMessage(message);
        }
    }
}
