package multhreadfiletransport.observer;

import multhreadfiletransport.client.reciever.RecieverThread;
import multhreadfiletransport.model.RecieverSectionInfo;

/**
 * Created by dela on 1/29/18.
 */

// 与RT相关联的一组listener和speaker, 用于监听文件接收情况, 以便反映给view
public interface ISectionReceiverListener {
    // 已经接收到了sender要发送的section列表(在接收列表结束后调用)
    void onGetSectionList(RecieverThread recieverThread);
    // 准备接收一个分片文件(在接收一个分片文件头之hou调用)
    void onBeginReceiveOneSection(RecieverSectionInfo sectionInfo);
    // 正在接收分片文件(每接收成功一小段就调用一次)
    void onReceiving(int receiveLen);
    // 一个分片文件接收完毕
    void endReceiveOneSection(RecieverThread recieverThread);
    // 这个sender所要发送的所有分片文件接收完毕
    void onReceiveOver(RecieverThread recieverThread);
    // 接收失败(出现异常)
    void onReceiveFailure(RecieverThread recieverThread);
}
