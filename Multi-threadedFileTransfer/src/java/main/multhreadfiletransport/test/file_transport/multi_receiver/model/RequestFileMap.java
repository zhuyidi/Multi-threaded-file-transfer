package multhreadfiletransport.test.file_transport.multi_receiver.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��������ļ��������Ǽ����ս����Ϣ<br>
 * ��¼��������ļ���������Ϣ��������<br>
 * <ol>
 * 		<li>��������ļ�����Map fileMap</li>
 * 		<li>��������ļ����ֽ���</li>
 * 		<li>��������ļ��ܸ���</li>
 * 		<li>�ѱ�ǽ����ļ�����</li>
 * 		<li>����ɽ����ļ�����</li>
 * 		<li>ȫ���ļ���ɱ�Ƿ�</li>
 * 		<li>ȫ���ļ���ɽ��շ�</li>
 * </ol>
 * 
 * @author ��Ѫ����
 */
public class RequestFileMap {
	private Map<String, ReceiveFileModel> fileMap;
	private long requestFileBytes;
	private int requestFileCount;
	private int markedFileCount;
	private int receivedFileCount;
	private boolean marked;
	private boolean received;
	
	public RequestFileMap() {
		this.requestFileCount = 0;
		this.receivedFileCount = 0;
		this.markedFileCount = 0;
		this.marked = false;
		this.received = false;
		this.fileMap = new HashMap<String, ReceiveFileModel>();
	}

	/**
	 * �õ���Ҫ���ϲ������ļ�����
	 * @return
	 */
	public int getNeededJoinFileCount() {
		int neededJoinFileCount = 0;
		
		for(String targetFileName : fileMap.keySet()) {
			ReceiveFileModel file = fileMap.get(targetFileName);
			if(file.getReceiveFileList() != null) {
				neededJoinFileCount++;
			}
		}
		
		return neededJoinFileCount;
	}
	
	/**
	 * ����һ����Ҫ���ܵ��ļ�
	 * @param targetFileName �����ļ�����
	 * @param fileSize �����ļ�����
	 */
	public void addFile(String targetFileName, long fileSize) {
		ReceiveFileModel file = fileMap.get(targetFileName);
		if(file == null) {
			file = new ReceiveFileModel(targetFileName, fileSize);
			fileMap.put(targetFileName, file);
			requestFileCount++;
			requestFileBytes += fileSize;
		}
	}
	
	public void setFileList(List<RequestFileModel> fileList) {
		for(RequestFileModel file : fileList) {
			String targetFileName = file.getFilePath();
			long fileSize = file.getFileSize();
			addFile(targetFileName, fileSize);
		}
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @return
	 */
	public Map<String, ReceiveFileModel> getFileMap() {
		return fileMap;
	}

	/**
	 * ��ȡ�����ļ����ֽ���
	 * @return
	 */
	public long getRequestFileBytes() {
		return requestFileBytes;
	}

	/**
	 * ����ļ�׼������<br>
	 * ReceiveFileModel receiveFile���������ļ����ơ��ļ���ʱ���ơ���ʼλ�ú��ļ�����<br>
	 * �����Ƿ�Ƭ�ַ����ļ�������targetFileNameѰ�Ҷ�Ӧ�ļ���
	 * @param receiveFile
	 * @throws MarkedFileNotExistException 
	 */
	public void markedFile(FileSectionModel receiveFile) throws MarkedFileNotExistException {
		String targetFileName = receiveFile.getTargetFileName();
		ReceiveFileModel file = fileMap.get(targetFileName);
		if(file == null) {
			throw new MarkedFileNotExistException("��ǵ�һ�������ڵ��ļ�["
					+ targetFileName + "]��");
		}
		if(file.markFile(receiveFile)) {
			markedFileCount++;
			if(markedFileCount == requestFileCount) {
				marked = true;
			}
		}
	}
	
	/**
	 * ����ļ��������<br>
	 * ReceiveFileModel receiveFile���������ļ����ơ��ļ���ʱ���ơ���ʼλ�ú��ļ�����<br>
	 * �����Ƿ�Ƭ�ַ����ļ�������targetFileNameѰ�Ҷ�Ӧ�ļ���
	 * @param receiveFile
	 * @throws ReceivedFileNotExistException 
	 */
	public void receivedFile(FileSectionModel receiveFile) throws ReceivedFileNotExistException {
		String targetFileName = receiveFile.getTargetFileName();
		ReceiveFileModel file = fileMap.get(targetFileName);
		if(file == null) {
			throw new ReceivedFileNotExistException("���յ�һ�������ڵ��ļ�["
					+ targetFileName + "]��");
		}
		if(file.receivedFile(receiveFile)) {
			receivedFileCount++;
			if(receivedFileCount == requestFileCount) {
				received = true;
			}
		}
	}

	public int getReceivedFileCount() {
		return receivedFileCount;
	}

	/**
	 * �õ����ļ�����
	 * @return
	 */
	public int getRequestFileCount() {
		return requestFileCount;
	}

	/**
	 * �Ƿ�����������ļ���ǹ���
	 * @return
	 */
	public boolean isMarked() {
		return marked;
	}

	/**
	 * �Ƿ�����������ļ����չ���
	 * @return
	 */
	public boolean isReceived() {
		return received;
	}
}
