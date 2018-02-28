package multhreadfiletransport.server.core;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.observer.serverandclient.IMessageSpeaker;
import multhreadfiletransport.server.distribution.DistributionFile;
import multhreadfiletransport.server.distribution.ResourceTable;
import multhreadfiletransport.server.model.ClientDefinition;
import multhreadfiletransport.util.ParseUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
            // 如果消息的类型是请求文件, 那么就进行任务分配
            DistributionFile distributionFile = new DistributionFile();
            distributionFile.distributionFile(msgMessage);
        } else if (msgMessage.getAction() == Message.UPDATE_RESOURCE) {
            // 如果消息的类型是更新资源表
            ResourceTable.updateClientResource(msgMessage);
        } else if (msgMessage.getAction() == Message.CLIENT_ID) {
            // 当客户端连接上之后, 会发送过来它的clientID, 服务端就将它的clientID存入map中
            String clientID = msgMessage.getMessage();
            ServerCenter.clientIDMap.put(clientID, new ClientDefinition(clientID, serverThread.getSocket()));
            ServerCenter.useableClientIDMap.put(clientID, new ClientDefinition(clientID, serverThread.getSocket()));
        } else if (msgMessage.getAction() == Message.NO_THIS_SECTION) {
            // 该sender没有这个资源
            // 如果没有, 那么服务端就要自己发送, 那么服务端应不应该构建一个sectionList, 还是检测到一个就发送一个?
            // 检测到如果没有这个文件, 那么就由服务端自己发送, 检测到一个就发送一个
            serverSendSection(msgMessage);
        }
    }

    public synchronized void serverSendSection(Message message) {
        String[] info = ParseUtil.parseStringToClientIDAndSectionInfo(message.getMessage());
        Socket socket = ServerCenter.clientIDMap.get(info[0]).getSocket();
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
