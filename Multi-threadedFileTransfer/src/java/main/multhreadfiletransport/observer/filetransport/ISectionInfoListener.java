package multhreadfiletransport.observer.filetransport;


import multhreadfiletransport.model.RecieverSectionInfo;

import java.util.List;

/**
 * Created by dela on 1/25/18.
 */
// RT与RC通信的一组Listener/Speaker
// 用于RT发送section或sectionList, RC进行mark, 计算len等工作
public interface ISectionInfoListener {
    void getSectionInfoList(List<RecieverSectionInfo> sectionInfoList);
    void getSectionInfo(RecieverSectionInfo sectionInfo);
    void getSectionSaveOK(RecieverSectionInfo sectionInfo);
    void getAllSectionSaveOk();
}
