package multhreadfiletransport.client.core;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.util.ParseUtil;

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
        Message message = ParseUtil.parseStringToMessage(strMessage);

        if (message.getAction() == Message.DISTRIBUTION_SECTION) {
            // 如果是任务分派消息, 那么先检测自己有没有这个文件, 如果没有就返回一个错误信息

        }
    }
}
