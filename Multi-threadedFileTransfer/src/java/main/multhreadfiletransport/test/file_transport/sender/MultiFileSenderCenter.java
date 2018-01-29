package multhreadfiletransport.test.file_transport.sender;

import multhreadfiletransport.test.file_transport.multi_receiver.model.FileSectionModel;
import multhreadfiletransport.test.file_transport.multi_receiver.model.RequestFileModel;

import java.util.ArrayList;
import java.util.List;


public class MultiFileSenderCenter {
	private List<SendFileModel> sendFileList;
	private String ip;
	private FileSender fileSender;
	
	public static final int DEFAULT_PORT = 54199;
	public static final int DEFAULT_BUFFER_SIZE = 1 << 15;
	
	public MultiFileSenderCenter(String ip, int port, int bufferSize) {
		this.sendFileList = new ArrayList<>();
		this.ip = ip;
		this.fileSender = new FileSender(ip, port, bufferSize);
	}
	
	public MultiFileSenderCenter(String ip, int port) {
		this(ip, port, DEFAULT_BUFFER_SIZE);
	}
	
	public MultiFileSenderCenter(String ip) {
		this(ip, DEFAULT_PORT, DEFAULT_BUFFER_SIZE);
	}
	
	public boolean addSendFile(String orgName, String targetName) {
		return addSendFile(orgName, targetName, null, 0, 0);
	}
	
	public boolean addSendFile(String orgName, String targetName
			, String tempName, long startPos, long fileLength) {
		if(tempName == null) {
			tempName = targetName;
		}
		SendFileModel sendFile = new SendFileModel();
		boolean ok = sendFile.setFileName(orgName, targetName,
				tempName, startPos, fileLength);
		if(ok) {
			sendFileList.add(sendFile);
		}
		
		return ok;
	}
	
	public void addSendTaskListener(ISendTaskListener listener) {
		if(fileSender == null) {
			return;
		}
		fileSender.addListener(listener);
	}

	public void send() {
		if(ip == null) {
			return;
		}
		fileSender.createSender()
				.setSendFileList(sendFileList)
				.send();
	}
	
	/**
	 * ���������ļ�(Ⱥ)�ָ��count�飻<br>
	 * ����һ�ļ�<font size="2"; color="red";>����</font>minSectionLength���򣬽��ļ�
	 * �ָ��ÿ��minSectionLength�ֽ�(���һ��Ϊʣ�೤��)
	 * 
	 * @param requestResourceList ���ָ���ļ��б�
	 * @param count �ָ����
	 * @param minSectionLength �ļ��ָ����С����
	 * @return
	 */
	public static List<List<FileSectionModel>> dispatchSenddingMission(
			List<RequestFileModel> requestResourceList,
			int count, long minSectionLength) {
		List<List<FileSectionModel>> fileSectionsList = new ArrayList<>();
		for(int i = 0; i < count; i++) {
			List<FileSectionModel> fileSectionList = new ArrayList<>();
			fileSectionsList.add(fileSectionList);
		}
		
		List<FileSectionModel> fileSectionList = null;
		int i = 0;
		for(RequestFileModel requestFile : requestResourceList) {
			String fileName = requestFile.getFilePath();
			long fileSize = requestFile.getFileSize();
			if(fileSize < minSectionLength) {
				fileSectionList = fileSectionsList.get(i);
				fileSectionList.add(createFileSection(fileName, fileSize));
				i = (i+1) % count;
			} else {
				long restLength = fileSize;
				long startPos = 0;
				long sectionLen;
				int sectionNo = 1;
				while(restLength > 0) {
					fileSectionList = fileSectionsList.get(i);
					sectionLen = restLength > minSectionLength 
							? minSectionLength : restLength;
					fileSectionList.add(createFileSection(fileName,
							startPos == 0 ? fileName : fileName + "." + sectionNo,
							startPos, sectionLen));
					startPos += sectionLen;
					
					sectionNo++;
					restLength -= sectionLen;
					i = (i+1) % count;
				}
			}
		}
		
		return fileSectionsList;
	}
	
	private static FileSectionModel createFileSection(String targetFileName,
			String tempFileName, long startPos, long fileLen) {
		FileSectionModel fileSection = new FileSectionModel();
		fileSection.setTargetFileName(targetFileName);
		fileSection.setTempFileName(tempFileName);
		fileSection.setStartPos(startPos);
		fileSection.setFileLen(fileLen);
		fileSection.setReceived(false);
		
		return fileSection;
	}
	
	private static FileSectionModel createFileSection(String targetFileName, long fileLen) {
		return createFileSection(targetFileName, targetFileName, 0, fileLen);
	}
}
