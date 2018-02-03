package multhreadfiletransport.observer;


import multhreadfiletransport.model.RecieverSectionInfo;

import java.util.List;

/**
 * Created by dela on 1/25/18.
 */
public interface ISectionInfoListener {
    void getSectionInfoList(List<RecieverSectionInfo> sectionInfoList);
    void getSectionInfo(RecieverSectionInfo sectionInfo);
    void getSectionSaveOK(RecieverSectionInfo sectionInfo);
}
