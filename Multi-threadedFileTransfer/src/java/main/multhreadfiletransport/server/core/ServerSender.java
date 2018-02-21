package multhreadfiletransport.server.core;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.server.model.ClientDefinition;
import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by dela on 2/21/18.
 */
public class ServerSender implements IMessageListener{
    @Override
    public void getMessage(String strMessage) {
        Message message = ParseUtil.parseStringToMessage(strMessage);
        Socket socket = ServerCenter.clientIDMap.get(message.getTo()).getSocket();
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeChars(strMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public synchronized void sendSection(RecieverSectionInfo sectionInfo, ClientDefinition client) {
//        // 发送片段
//    }
}
