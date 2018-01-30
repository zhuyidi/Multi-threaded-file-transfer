package multhreadfiletransport.model;

import java.util.LinkedList;
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
    private long saveLen;
    // 子文件的全部信息
    LinkedList<RecieverSectionInfo> sectionInfoList;

    {
        sectionInfoList = new LinkedList<>();
    }

    public RecieverSimpleInfo() {
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
        this.saveLen = 0;
    }

    public RecieverSimpleInfo(String targetFileName, long fileLen) {
        this.targetFileName = targetFileName;
        this.fileLen = fileLen;
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
        this.saveLen = 0;
    }

    public RecieverSimpleInfo(String targetFileName, long fileLen, LinkedList<RecieverSectionInfo> sectionInfoList) {
        this(targetFileName, fileLen);
        this.sectionInfoList = sectionInfoList;
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

    public long getSaveLen() {
        return saveLen;
    }

    public void setSaveLen(long saveLen) {
        this.saveLen = saveLen;
    }

    public LinkedList<RecieverSectionInfo> getSectionInfoList() {
        return sectionInfoList;
    }

    public void setSectionInfoList(LinkedList<RecieverSectionInfo> sectionInfoList) {
        this.sectionInfoList = sectionInfoList;
    }

    @Override
    public String toString() {
        return "RecieverSimpleInfo{" +
                "targetFileName='" + targetFileName + '\'' +
                ", fileLen=" + fileLen +
                ", recieveMark=" + recieveMark +
                ", recieveLen=" + recieveLen +
                ", saveMark=" + saveMark +
                ", saveLen=" + saveLen +
                ", sectionInfoList=" + sectionInfoList +
                '}';
    }
}
