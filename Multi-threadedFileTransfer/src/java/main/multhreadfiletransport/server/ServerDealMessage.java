package multhreadfiletransport.server;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.util.ParseUtil;

/**
 * Created by dela on 2/5/18.
 */

// message处理类: 主要是将serverThread传递过来的消息进行解析, 解析成message格式, 然后进行处理
public class ServerDealMessage implements IMessageListener {
    private String message;
    private ServerThread serverThread;

    public ServerDealMessage(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void getMessage(String strMessage) {
        this.message = strMessage;
        // 将String格式的消息转换成Message格式
        Message msgMessage = ParseUtil.parseStringToMessage(message);

        // 根据message的action的类型进行分类处理
        if (msgMessage.getAction() == Message.REQUEST_FILE) {
            // TODO 如果消息的类型是请求文件, 就....
        } else if() {

        }
    }

}
