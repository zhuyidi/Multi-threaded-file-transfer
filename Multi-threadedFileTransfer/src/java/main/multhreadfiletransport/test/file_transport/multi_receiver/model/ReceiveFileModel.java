package multhreadfiletransport.test.file_transport.multi_receiver.model;

import java.util.LinkedList;
import java.util.List;

public class ReceiveFileModel {
	private String targetFileName;
	private long fileSize;
	private boolean marked;
	private long markedSize;
	private List<FileSectionModel> receiveFileList;
	private boolean received;
	private long receivedSize;
	
	public ReceiveFileModel(String targetFileName, long fileSize) {
		this.targetFileName = targetFileName;
		this.fileSize = fileSize;
		this.marked = false;
		this.markedSize = 0;
		this.received = false;
		this.receivedSize = 0;
	}

	/**
	 * ���һ���ļ����������յ����ļ�����Ҫ�����ܡ�����Ϣ<br>
	 * ����δ��ֵ��ļ���ֱ�ӱ��Ϊ������ɱ�ǡ�����������£�����ļ��б�Ϊnull<br>
	 * ���ڲ���ļ���
	 * <ul>
	 * 		<li>��һ������ļ�����Ҫ��������ļ��б�</li>
	 * 		<li>��������ļ�����Ҫ���뵽����ļ��б�ĺ���λ�ã���֤�б�startPosֵ���򣬼���
	 * ��Ƭ�ļ���λ���Ⱥ�˳�����С�
	 * </ul>
	 * @param receiveFile
	 * @return ��Ǹ��ļ��󣬸��ļ��Ƿ���ɱ�ǣ��������з�Ƭ�ļ�ȫ�������
	 */
	public boolean markFile(FileSectionModel receiveFile) {
		long startPos = receiveFile.getStartPos();
		long fileLen = receiveFile.getFileLen();
		
		// ��һ�ļ�
		if(fileLen == fileSize) {
			markedSize = fileLen;
			marked = true;
			return marked;
		}
		
		// ����ļ������ǵ�һ������ļ�
		if(receiveFileList == null) {
			receiveFileList = new LinkedList<>();
			receiveFileList.add(receiveFile);
			markedSize = fileLen;
			marked = false;
			return marked;
		}
		
		// ����ļ���������ļ��׵�ַ������ǰ����ļ����뵽����λ��
		int index = 0;
		int receiveFileCount = receiveFileList.size();
		for(; index < receiveFileCount; index++) {
			FileSectionModel markedFile = receiveFileList.get(index);
			if(markedFile.getStartPos() > startPos) {
				break;
			}
		}
		receiveFileList.add(index, receiveFile);
		markedSize += fileLen;
		marked = markedSize == fileSize;
		return marked;
	}
	
	/**
	 * �����ļ������������յ����ļ������ۻ��͵������賤�ȣ��򣬱�Ǹ��ļ����ս�����<br>
	 * ������ʱ�������ϲ�������ļ���
	 * @param receiveFile
	 * @return δ����ļ����ս��������߲���ļ�ȫ�����ս���
	 */
	public boolean receivedFile(FileSectionModel receiveFile) {
		long fileLen = receiveFile.getFileLen();
		receivedSize += fileLen;
		received = receivedSize == fileSize;
		
		return received;
	}

	public List<FileSectionModel> getReceiveFileList() {
		return receiveFileList;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		str.append("[");
		str.append(targetFileName);
		str.append("]:");
		str.append(fileSize);
		str.append("\n\t�ѱ��");
		str.append(markedSize);
		str.append("�ֽڣ�");
		str.append(marked ? "��" : "δ");
		str.append("��ɱ��");
		str.append("\n\t�ѽ���:");
		str.append(receivedSize);
		str.append("�ֽڣ�");
		str.append(received ? "��" : "δ");
		str.append("��ɽ���");
		if(receiveFileList != null) {
			for(FileSectionModel receiveFile : receiveFileList) {
				str.append("\n\t\t");
				str.append(receiveFile.getTempFileName());
				str.append("(");
				str.append(receiveFile.getStartPos());
				str.append(":");
				str.append(receiveFile.getFileLen());
				str.append(")");
			}
		}
		
		return str.toString();
	}
}
