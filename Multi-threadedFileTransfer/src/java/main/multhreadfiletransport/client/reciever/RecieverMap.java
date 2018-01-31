package multhreadfiletransport.client.reciever;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.model.RecieverSimpleInfo;
import multhreadfiletransport.observer.ISectionInfoListener;
import multhreadfiletransport.observer.ISectionInfoSpeaker;
import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.util.*;


/**
 * Created by dela on 1/25/18.
 */
public class RecieverMap implements ISectionInfoSpeaker {
    private Map<String, RecieverSimpleInfo> fileMap;
    private int targetFileCount;    //所有的targetfile
    private int markTragetFileCount; // 已经标记完的targetFile
    private List<ISectionInfoListener> sectionInfoListeners;

    {
        sectionInfoListeners = new ArrayList<>();
    }

    public RecieverMap(RecieverCenter recieverCenter) {
        markTragetFileCount = 0;
        fileMap = new HashMap<>();
        // 用集合操作类对map进行线程安全处理
        Collections.synchronizedMap(fileMap);
        sectionInfoListeners.add(recieverCenter);
    }

    public RecieverMap(Map<String, RecieverSimpleInfo> fileMap) {
        markTragetFileCount = 0;
        this.fileMap = fileMap;
    }

    public Map<String, RecieverSimpleInfo> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, RecieverSimpleInfo> fileMap) {
        this.fileMap = fileMap;
    }

    public int getTargetFileCount() {
        return fileMap.size();
    }

    public void setTargetFileCount(int targetFileCount) {
        this.targetFileCount = targetFileCount;
    }

    // 在filemap中查询已经mark完的所有section的targetfile数量
    public int getMarkTragetFileCount() {
        // TODO 应该在map中查询已经mark完所有的section的targetFile的数量
//        int count = 0;
        for (String filename : fileMap.keySet()) {
            RecieverSimpleInfo simpleInfo = fileMap.get(filename);
            System.out.println("getmarktarget中的simple" + simpleInfo);
            if (simpleInfo.isSaveMark()) {
                markTragetFileCount++;
            }
        }
//        System.out.println("markTargetFile的数量:" + markTragetFileCount);

        System.out.println("已经全部标记的targetFile数量:" + markTragetFileCount);
        System.out.println("map里全部的targetFile数量:" + fileMap.size());
        if (markTragetFileCount == fileMap.size()) {
            sendAllSectionReceiveOk();
        }

        return markTragetFileCount;
    }

    public void initRecieveMap(List<FileInfo> fileInfoList) {
        for (FileInfo fileInfo : fileInfoList) {
            String fileName = ParseUtil.parseFileName(fileInfo.getFileName());
            fileMap.put(fileInfo.getFileName(), new RecieverSimpleInfo(fileInfo.getFileName(), fileInfo.getFileLen()));
        }
        System.out.println("fileMap:" + fileMap);
    }

    public void setSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        for (RecieverSectionInfo sectionInfo : sectionInfoList) {
            // 找到这个片段所对应的simpleFileInfo, 然后设置它的recievemark标志,
            // 再设置它的recievelen,
            // 最后将这个section插入到它的List中, 标记已经接收过

            // 1. 得到simpleFileInfo
            RecieverSimpleInfo simpleInfo = getSimpleInfoBySection(sectionInfo);

            System.out.println(simpleInfo);

            // 2. 设置recieveMark和recieveLen
            simpleInfo.setRecieveMark(true);
            simpleInfo.setReciveLen(simpleInfo.getRecieveLen() + sectionInfo.getSectionLen());

            // 3. 将这个section插入到List中
            int index = 0;
            LinkedList<RecieverSectionInfo> sectionInfos = simpleInfo.getSectionInfoList();
            for (RecieverSectionInfo sectionInfo1 : sectionInfos) {
                if(sectionInfo1.getOffset() > sectionInfo.getOffset()) {
                    index = sectionInfos.indexOf(sectionInfo1);
                }
            }
//            System.out.println("此section插入之前");
            sectionInfos.add(index, sectionInfo);
//            System.out.println("此section插入之后");
        }
    }

    public void setSectionRecieveMark(RecieverSectionInfo sectionInfo) {
        RecieverSimpleInfo simpleInfo = getSimpleInfoBySection(sectionInfo);

//        System.out.println("setreceivemark中的section信息: " + sectionInfo);
        System.out.println("setreceivemark中的simpleInfo:" + simpleInfo);

        for (RecieverSectionInfo sectionInfo1 : simpleInfo.getSectionInfoList()) {
            if(sectionInfo.getTempFileName().equals(sectionInfo1.getTempFileName())) {
//                System.out.println("section1信息: " + sectionInfo1);

                sectionInfo1.setRecieveMark(true);
            }
        }
    }

    public RecieverSimpleInfo getSimpleInfoBySection(RecieverSectionInfo sectionInfo) {
        String targetFileName = sectionInfo.getTargetFileName();
        return fileMap.get(targetFileName);
    }

    public void setSaveMarkAndLen(RecieverSectionInfo sectionInfo) {
        // 接收成功一个分片文件, 先将这个分片文件的长度类加到saveLen上,
        // 然后判断这个targetFile所saveLen的长度是否等于它本身的len,
        // 如果等于, 就将saveMark(是否保存完的标记)设置为true
        RecieverSimpleInfo simpleInfo = getSimpleInfoBySection(sectionInfo);
        simpleInfo.setSaveLen(simpleInfo.getSaveLen() + sectionInfo.getSectionLen());
        System.out.println("setsaveandmark中的simpleInfo:" + simpleInfo);
        System.out.println(simpleInfo.getSaveLen() == simpleInfo.getFileLen());
        if(simpleInfo.getSaveLen() == simpleInfo.getFileLen()) {
            simpleInfo.setSaveMark(true);
        }

        for (RecieverSectionInfo sectionInfo1 : simpleInfo.getSectionInfoList()) {
//            System.out.println("setlenandmark的section: " + sectionInfo);
            if (sectionInfo.getTempFileName().equals(sectionInfo1.getTempFileName())) {
//                System.out.println("setlenandmark的section1: " + sectionInfo1);
                sectionInfo1.setSaveMark(true);
                sectionInfo1.setSaveLen(sectionInfo.getSectionLen());
            }
        }
    }

    @Override
    public void registerListener(ISectionInfoListener listener) {
        sectionInfoListeners.add(listener);
    }

    @Override
    public void removeListener(ISectionInfoListener listener) {
        if (sectionInfoListeners.contains(listener)) {
            sectionInfoListeners.remove(listener);
        }
    }

    @Override
    public void sendSectionInfoList() {

    }

    @Override
    public void sendSectionInfo(RecieverSectionInfo sectionInfo) {

    }

    @Override
    public void sendSectionSaveOK(RecieverSectionInfo sectionInfo) {

    }

    @Override
    public void sendAllSectionReceiveOk() {
        for (ISectionInfoListener listener : sectionInfoListeners) {
            listener.getAllSectionSaveOk();
        }
    }
}
