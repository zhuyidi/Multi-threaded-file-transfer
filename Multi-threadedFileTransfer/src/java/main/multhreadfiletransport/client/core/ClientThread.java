package multhreadfiletransport.client.core;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.observer.serverandclient.IMessageSpeaker;
import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by dela on 2/5/18.
 */
public class ClientThread implements Runnable, IMessageSpeaker {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private int clientID;

    private String message;
    private boolean goon;
    private List<IMessageListener> listeners;
    private ClientDealMessage clientDealMessage;

    private String targetPath;
    private byte[] buffer;
    private int bufferSize;
    private int headerSize;

    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        bufferSize = Integer.parseInt(resourceBundle.getString("bufferSize"));
        headerSize = Integer.parseInt(resourceBundle.getString("headerSize"));
        targetPath = resourceBundle.getString("targetPath");
        listeners = new ArrayList<>();
    }


    public ClientThread(Socket socket, int clientID) {
        this.socket = socket;
        this.clientID = clientID;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        goon = true;
        clientDealMessage = new ClientDealMessage(this);
        addListener(clientDealMessage);

        // 初始化完成之后, 就将自己的clientID发送过去
        // 检测自己所拥有的资源, 然后发送给服务端用于资源表更新

    }

    public void sendClientID() {
        Message message = new Message();
        message.setFrom(clientID);
        message.setTo(-1);
        message.setAction(Message.CLIENT_ID);
        message.setMessage(String.valueOf(clientID));

        try {
            outputStream.writeChars(PackageUtil.packageMessage(message));
        } catch (IOException e) {
            System.out.println("服务器已关闭");
            close();
        }

    }

    public void updateResourceTable() {
        // 检测当前所拥有的文件, 然后打包成message发送给server
        List<String> fileNames = new ArrayList<>();
        getFileNames(targetPath, fileNames);

        Message message = new Message();
        message.setFrom(clientID);
        message.setTo(-1);
        message.setAction(Message.UPDATE_RESOURCE);
        message.setMessage(PackageUtil.packageFileNameList(fileNames));

        try {
            outputStream.writeChars(PackageUtil.packageMessage(message));
        } catch (IOException e) {
            close();
        }
    }

    private void getFileNames(String path, List<String> fileNames) {
        File file = new File(path);
        File [] files = file.listFiles();

        for (File a : files) {
            if (a.isDirectory()) {
                getFileNames(a.getAbsolutePath(), fileNames);
            } else {
                fileNames.add(a.getName());
            }
        }
    }

    @Override
    public void run() {
        // serverThread里面持续监听来自客户端的消息
        while (goon) {
            try {
                message = readMessageFromClient();
            } catch (IOException e) {
                goon = false;
                close();
            }
        }
    }


    public void close() {
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
            e.printStackTrace();
        }
    }

    public String readMessageFromClient() throws IOException {
        byte[] tempInfoLen = new byte[headerSize];
        int temp = 0;
        int readlen;

        while (temp != headerSize) {
            readlen = inputStream.read(tempInfoLen, temp, headerSize - temp);
            temp += readlen;
        }

        long messageLen = ParseUtil.getByteStrLen(tempInfoLen);

        StringBuffer message = new StringBuffer();
        int readLen = 0;
        while (messageLen > 0) {
            int size = messageLen > bufferSize ? bufferSize : (int) messageLen;
            readLen = inputStream.read(buffer, 0, size);
            message.append(new String(buffer, 0, readLen));
            messageLen -= readLen;
        }
        return message.toString();
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
    public void sendMessage(String strMessage) {
        for (IMessageListener listener : listeners) {
            listener.getMessage(message);
        }
    }
}
