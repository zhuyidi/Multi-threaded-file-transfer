package multhreadfiletransport.client.core;

import multhreadfiletransport.observer.serverandclient.IMessageListener;

/**
 * Created by dela on 2/5/18.
 */
public class ClientDealMessage implements IMessageListener {
    private ClientThread clientThread;

    public ClientDealMessage(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    @Override
    public void getMessage(String strMessage) {
        // 对ClientThread传送过来的消息进行处理
    }
}
