package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;

import java.util.*;


/**
 * Created by dela on 1/25/18.
 */
public class RecieverMap {
    private Map<String, RecieverSimpleInfo> fileMap;

    public RecieverMap() {
        fileMap = new HashMap<>();
    }

    public RecieverMap(Map<String, RecieverSimpleInfo> fileMap) {
        this.fileMap = fileMap;
    }

    public Map<String, RecieverSimpleInfo> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, RecieverSimpleInfo> fileMap) {
        this.fileMap = fileMap;
    }

    public void initRecieveMap(List<FileInfo> fileInfoList) {
        for (FileInfo fileInfo : fileInfoList) {
            fileMap.put(fileInfo.getFileName(), new RecieverSimpleInfo(fileInfo.getFileName(), fileInfo.getFileLen()));
        }
    }

    public void setSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        for (RecieverSectionInfo sectionInfo : sectionInfoList) {
            // 找到这个片段所对应的simpleFileInfo, 然后设置它的recievemark标志,
            // 再设置它的recievelen,
            // 最后将这个section插入到它的list中, 标记已经接收过

            // 1. 得到simpleFileInfo
            String targetFileName = sectionInfo.getTargetFileName();
            RecieverSimpleInfo simpleInfo = fileMap.get(targetFileName);
            // 2. 设置recieveMark和recieveLen
            simpleInfo.setRecieveMark(true);
            simpleInfo.setReciveLen(simpleInfo.getRecieveLen() + sectionInfo.getSectionLen());

            // 3. 将这个section插入到set中(在section类中已经实现了)
            TreeSet<RecieverSectionInfo> sectionInfos = simpleInfo.getSectionInfoSet();
            sectionInfos.add(sectionInfo);            
        }
    }
}
