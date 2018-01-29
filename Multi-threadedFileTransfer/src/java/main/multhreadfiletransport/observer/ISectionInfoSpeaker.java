package multhreadfiletransport.observer;

import multhreadfiletransport.model.RecieverSectionInfo;

/**
 * Created by dela on 1/25/18.
 */
public interface ISectionInfoSpeaker {
    void registerListener(ISectionInfoListener listener);
    void removeListener(ISectionInfoListener listener);
    void sendSectionInfoList();
    void sendSectionInfo(RecieverSectionInfo sectionInfo);
    void sendSectionSaveOK(RecieverSectionInfo sectionInfo);
}
