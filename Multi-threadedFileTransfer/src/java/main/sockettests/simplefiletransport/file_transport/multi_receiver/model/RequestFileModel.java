package sockettests.simplefiletransport.file_transport.multi_receiver.model;

/**
 * �����ļ���Դ��Ϣ��������������ļ��Ļ�����Ϣ���ļ����ƺ��ļ�����<br>
 * �ļ����ư�������·�������磺��Ƭ�ļ�����·����photo\����ťͼ���ļ�����·����lib\pic\�ȡ�<br>
 * ��Ҫ����������Ҫ���ļ��Ļ�����Ϣ��δ������Ƭ��������
 * 
 * @author ��Ѫ����
 *
 */
public class RequestFileModel {
	private String filePath;
	private long fileSize;
	
	public RequestFileModel() {
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		return "[" + filePath + "]:" + fileSize;
	}
}
