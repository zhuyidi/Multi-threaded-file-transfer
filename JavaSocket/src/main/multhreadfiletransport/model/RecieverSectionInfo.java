package multhreadfiletransport.model;

/**
 * Created by dela on 1/23/18.
 */
public class RecieverSectionInfo implements Comparable<RecieverSectionInfo> {
    // 目标总文件名
    private String targetFileName;
    // 临时文件名
    private String tempFileName;
    // 起始偏移量
    private long offset;
    // 该分片长度
    private long sectionLen;
    // 是否接收过
    private boolean recieveMark;
    // 接收的大小
    private long recieveLen;
    // 是否保存完
    private boolean saveMark;
    // 保存了多少
    private long saveLen;

    public RecieverSectionInfo() {
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
        this.saveLen = 0;
    }

    public RecieverSectionInfo(String targetFileName) {
        this.targetFileName = targetFileName;
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
        this.saveLen = 0;
    }

    public RecieverSectionInfo(String targetFileName, String tempFileName, long offset, long sectionLen) {
        this.targetFileName = targetFileName;
        this.tempFileName = tempFileName;
        this.offset = offset;
        this.sectionLen = sectionLen;
        this.recieveMark = false;
        this.recieveLen = 0;
        this.saveMark = false;
        this.saveLen = 0;
    }

    public RecieverSectionInfo(String targetFileName, String tempFileName, long offset, long sectionLen,
                               boolean recieveMark, long recieveLen, boolean saveMark, long saveLen) {
        this.targetFileName = targetFileName;
        this.tempFileName = tempFileName;
        this.offset = offset;
        this.sectionLen = sectionLen;
        this.recieveMark = recieveMark;
        this.recieveLen = recieveLen;
        this.saveMark = saveMark;
        this.saveLen = saveLen;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSectionLen() {
        return sectionLen;
    }

    public void setSectionLen(long sectionLen) {
        this.sectionLen = sectionLen;
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

    public void setRecieveLen(long recieveLen) {
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

    @Override
    public int compareTo(RecieverSectionInfo o) {
        return this.getOffset() > o.getOffset() ? 1 : -1;
    }
}
