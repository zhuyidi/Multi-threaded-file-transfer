package multhreadfiletransport.test.file_transport.sender;

import java.io.File;

/**
 * ���Ͷ������͵��ļ���Ϣ��<br>
 * <ul>
 * 		<li>orgName��Դ�ļ����ƣ������͵��ļ��ڷ��Ͷ˵����ƺ�·���п��ܲ�ͬ�ڽ��նˣ�</li>
 * 		<li>targetName��Ŀ���ļ��������ƣ����л���·����</li>
 * 		<li>tempName����Ƭ�ļ����ƣ���δ��Ƭ����������������ͬ��</li>
 * 		<li>startPos����Ƭ�ļ���Ϣ�������ļ��е�ƫ������</li>
 * 		<li>fileLength���ļ���С��</li>
 * </ul>
 * 
 * @author ��Ѫ����
 *
 */
public class SendFileModel {
	private String orgName;
	private String targetName;
	private String tempName;
	private long startPos;
	private long fileLength;
	
	public SendFileModel() {
	}
	
	public boolean setFileName(String orgName, String targetName,
			String tempName, long startPos, long fileLength) {
		File file = new File(orgName);
		if(!file.exists()) {
			return false;
		}
		long fileSize = file.length();
		this.orgName = orgName;
		this.targetName = targetName;
		this.tempName = tempName;
		this.startPos = startPos;
		if(fileLength == 0) {
			this.fileLength = fileSize; 
		} else {
			this.fileLength = fileLength;
		}
		
		return true;
	}

	public String getTempName() {
		return tempName;
	}

	public long getStartPos() {
		return startPos;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getTargetName() {
		return targetName;
	}

	public long getFileLength() {
		return fileLength;
	}
}
