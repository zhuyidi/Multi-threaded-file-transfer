package multhreadfiletransport.server.model;

import java.net.Socket;

/**
 * Created by dela on 2/21/18.
 */
public class ClientDefinition implements Comparable<ClientDefinition>{
    private Socket socket;
    private int clientID;
    private int state = 0;     // 状态为0为空闲, 为1为忙碌
    private int sendCount = 0; // 记录该客户端发送的总次数

    public ClientDefinition() { }

    public ClientDefinition(int clientID) {
        this.clientID = clientID;
    }

    public ClientDefinition(String clientID, Socket socket) {
        this.clientID = Integer.parseInt(clientID);
        this.socket = socket;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    @Override
    public int compareTo(ClientDefinition o) {
        return this.getSendCount() > o.getSendCount() ? -1 : 1;
    }
}
