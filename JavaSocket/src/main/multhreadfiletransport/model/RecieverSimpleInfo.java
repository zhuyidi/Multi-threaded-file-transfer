package multhreadfiletransport.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by dela on 1/23/18.
 */
public class RecieverSimpleInfo {
    // 目标文件名
    private String targetFileName;
    // 目标文件长度
    private long fileLen;
    // 文件是否接收过
    private boolean recieveMark;
    // 文件接收了多长
    private long recieveLen;
    // 文件是否保存完
    private boolean saveMark;
    // 文件保存了多少(依据随机读写流写入文件的长度来算)
//    private long saveLen;
    // 子文件的全部信息
    TreeSet<RecieverSectionInfo> sectionInfoSet;

    {
        sectionInfoSet = new TreeSet<>();
    }

    public RecieverSimpleInfo() {
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
//        this.saveLen = 0;
    }

    public RecieverSimpleInfo(String targetFileName, long fileLen) {
        this.targetFileName = targetFileName;
        this.fileLen = fileLen;
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
//        this.saveLen = 0;
    }

    public RecieverSimpleInfo(String targetFileName, long fileLen, Set<RecieverSectionInfo> sectionInfoList) {
        this(targetFileName, fileLen);
        this.sectionInfoSet = sectionInfoSet;
    }

    public RecieverSimpleInfo(String targetFileName, long fileLen, boolean recieveMark, long recieveLen, boolean saveMark, TreeSet<RecieverSectionInfo> sectionInfoSet) {
        this.targetFileName = targetFileName;
        this.fileLen = fileLen;
        this.recieveMark = recieveMark;
        this.recieveLen = recieveLen;
        this.saveMark = saveMark;
//        this.saveLen = saveLen;
        this.sectionInfoSet = sectionInfoSet;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public long getFileLen() {
        return fileLen;
    }

    public void setFileLen(long fileLen) {
        this.fileLen = fileLen;
    }

    public boolean isRecieveMark() {
        return recieveMark;
    }

    public void setRecieveMark(boolean recieveMark) {
        this.recieveMark = recieveMark;
    }

    public long getRecieveLen() {
        return recieveLen;
    }

    public void setReciveLen(long recieveLen) {
        this.recieveLen = recieveLen;
    }

    public boolean isSaveMark() {
        return saveMark;
    }

    public void setSaveMark(boolean saveMark) {
        this.saveMark = saveMark;
    }

//    public long getSaveLen() {
//        return saveLen;
//    }
//
//    public void setSaveLen(long saveLen) {
//        this.saveLen = saveLen;
//    }

    public TreeSet<RecieverSectionInfo> getSectionInfoSet() {
        return sectionInfoSet;
    }

    public void setSectionInfoSet(TreeSet<RecieverSectionInfo> sectionInfoList) {
        this.sectionInfoSet = sectionInfoList;
    }
}
