package sockettests.simplefiletransport.file_transport.multi_receiver.model;

/**
 * ��Ƭ�ļ���Ϣ�����С���Ƭ����Ϣ���ļ���Ϣ��<br>
 * <ul>
 * 		<li>targetFileName�������ļ����ƣ�</li>
 * 		<li>tempFileName����Ƭ�ļ����ƣ���δ��Ƭ�����������ļ�����ͬ��
 * ���Ƿ�Ƭ�ļ��ĵ�һ����Ҳ�������ļ�������ͬ��</li>
 * 		<li>startPos����Ƭ�ļ��������ļ��е�ƫ������</li>
 * 		<li>fileLen����Ƭ�ļ��ĳ��ȣ�</li>
 * 		<li>received���Ƿ���ա�</li>
 * </ul>
 * ��Ƭ�ļ���Ϣ�����ɷ�������������Ҫ���͵��ļ���С�ͷ�Ƭԭ�򣬽���һ�ļ����л򲻽��зָ��γɵ���Ϣ��<br>
 * ����Ϣ��Ҫ���͸����ļ����Ͷˡ����Ա㷢�Ͷ��и��ļ������з��͡�
 * 
 * @author ��Ѫ����
 *
 */
public class FileSectionModel {
	private String targetFileName;
	private String tempFileName;
	private long startPos;
	private long fileLen;
	private boolean received;

	public FileSectionModel() {
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

	public long getStartPos() {
		return startPos;
	}

	public void setStartPos(long startPos) {
		this.startPos = startPos;
	}

	public long getFileLen() {
		return fileLen;
	}

	public void setFileLen(long fileLen) {
		this.fileLen = fileLen;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	@Override
	public String toString() {
		return "[" + targetFileName + "]" + tempFileName 
				+ ":" + startPos 
				+ ":" + fileLen;
	}
}
