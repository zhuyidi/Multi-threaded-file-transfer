package multhreadfiletransport.observer;

import multhreadfiletransport.model.RecieverSectionInfo;

/**
 * Created by dela on 1/25/18.
 */

// RT与RC通信的一组Listener/Speaker
// 用于RT发送section或sectionList, RC进行mark, 计算len等工作
public interface ISectionInfoSpeaker {
    void registerListener(ISectionInfoListener listener);
    void removeListener(ISectionInfoListener listener);
    void sendSectionInfoList();
    void sendSectionInfo(RecieverSectionInfo sectionInfo);
    void sendSectionSaveOK(RecieverSectionInfo sectionInfo);
    void sendAllSectionReceiveOk();
}
