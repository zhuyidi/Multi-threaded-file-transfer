package multhreadfiletransport.server.distribution;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.serverandclient.IMessageListener;
import multhreadfiletransport.observer.serverandclient.IMessageSpeaker;
import multhreadfiletransport.server.core.ServerCenter;
import multhreadfiletransport.server.core.ServerSender;
import multhreadfiletransport.server.model.ClientDefinition;
import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.io.File;
import java.util.*;

/**
 * Created by dela on 2/7/18.
 */

// 分配策略
public class DistributionFile implements IMessageSpeaker{
    private String sendPath;
    private long sectionSize;
    private List<IMessageListener> listenerList;
    private ServerSender serverSendCenter;

    {
        serverSendCenter = ServerCenter.serverSendCenter;
        listenerList = new ArrayList<>();
        listenerList.add(serverSendCenter);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        sendPath = resourceBundle.getString("sendPath");
        sectionSize = Long.parseLong(resourceBundle.getString("sectionSize"));
    }

    public void distributionFile(Message message) {
        // 首先先解析该客户端所请求的文件列表, 得到所需的客户端的集合
        String[] fileList = ParseUtil.parseStringToFileNameStringhArray(message.getMessage());
        Set<String> needClientSet = ResourceTable.getNeedClient(fileList);
        // 然后和在线的客户端的集合求并集
        Set<String> onlineClientSet = ServerCenter.useableClientIDMap.keySet();
        onlineClientSet.retainAll(needClientSet);
        List<Map<String, List<RecieverSectionInfo>>> clientSectionMaps = new ArrayList<>();

        for (String clientID : onlineClientSet) {
            Map<String, List<RecieverSectionInfo>> sectionMap = new HashMap<>();
            sectionMap.put(clientID, new ArrayList<RecieverSectionInfo>());
            clientSectionMaps.add(sectionMap);
        }

        // 得到每一个1 --> A + B, 然后进行细节处理
        for (String fileName : fileList) {
            Set<String> clients = ResourceTable.getClientsByFileName(fileName);
            distributionSection(clients, fileName);
        }

    }

    public void distributionSection(Set<String> clients, String fileName) {
        // 打开该文件, 判断这个文件的大小, 如果这个文件小于分片大小, 那就选择让发送次数少的端发送,
        // 如果大于, 则根据发送次数从小到大进行分配

        // 根据clientIDSet返回clientDefinitionSet
        Set<ClientDefinition> clientDefinitions = new TreeSet<>();
        for (String clientID : clients) {
            clientDefinitions.add(ServerCenter.clientIDMap.get(clientID));
        }
        String filePath = sendPath + fileName;
        File file = new File(filePath);
        Iterator<ClientDefinition> iterator = clientDefinitions.iterator();


        // TODO 这里有问题, 不应该是最后调用sendMessage方法, 而且, 在最后构成任务分配消息的时候, 消息内容应该是一个sectionInfoList
        // todo 还有问题没有处理完, 当前只处理完了sender端有的文件, 还有的文件没有客户端有, 要由服务器来发送, 这部分没有处理

        if (file.length() <= sectionSize) {
            ClientDefinition first = iterator.next();
            packageMessage(first, fileName, 0, file.length(), 1);
            return;
        }

        long tempLen = file.length();
        int count = 1;
        while (tempLen <= 0) {
            if (!iterator.hasNext()) {
                iterator = clientDefinitions.iterator();
            }
            ClientDefinition client = iterator.next();

            long size;
            if (tempLen >= sectionSize) {
                size = sectionSize;
            } else {
                size = tempLen;
            }
            packageMessage(client, fileName, (count-1)*sectionSize, size, count);
            tempLen -= sectionSize;
            count++;
        }
    }

    public void packageMessage(ClientDefinition client, String fileName, long offSet, long sectionLen, int count) {
        RecieverSectionInfo sectionInfo = new RecieverSectionInfo(fileName, fileName + "." + count, offSet, sectionLen);
        String strSectionInfo = PackageUtil.packageSectionInfo(sectionInfo);
        strSectionInfo = PackageUtil.packageToAndSectionInfo(strSectionInfo, String.valueOf(client.getClientID()));

        Message message = new Message(-1, client.getClientID(), Message.DISTRIBUTION_SECTION, strSectionInfo);
        sendMessage(PackageUtil.packageMessage(message));
    }

    @Override
    public void addListener(IMessageListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(IMessageListener listener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }

    @Override
    public void sendMessage(String strMessage) {
        for (IMessageListener listener : listenerList) {
            listener.getMessage(strMessage);
        }
    }
}
