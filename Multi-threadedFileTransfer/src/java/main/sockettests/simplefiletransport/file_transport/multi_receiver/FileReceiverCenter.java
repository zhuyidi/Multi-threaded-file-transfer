package sockettests.simplefiletransport.file_transport.multi_receiver;

import sockettests.simplefiletransport.file_transport.multi_receiver.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.*;


/**
 * �ļ��������ģ�����
 * <ol>
 * 		<li>������Ҫ�����ա����ļ��б�(����)��һ������������ļ�����������</li>
 * 		<li>�����͹رս��ն˷��������������Է������˵�NEW_RECEIVER��Ϣ</li>
 * 		<li>�����ļ����ս��ȸ����ļ����ս����ܿ���������</li>
 * 		<li>�����ܿ����������ġ�ֹͣ���ա������ִ�С�ֹͣ���ա�����</li>
 * </ol>
 * ��Ҫ���������fileReceiver����Ϣ��
 * <ol>
 * 		<li>���յ��ļ��б���Ϣ��
 * 			<ul>
 * 				<li>����ǡ���Ҫ���յ��ļ��б�</li>
 * 				<li>�������ļ�ȫ������ǣ���ֹͣ��������������</li>
 * 			</ul>
 * 		<li>���һ���ļ��Ľ�����Ϣ��
 * 			<ul>
 * 				<li>����ļ������գ�</li>
 * 				<li>֪ͨ�ļ����ս����ܿ�������</li>
 * 			</ul>
 * 		</li>
 * 		<li>�ļ����ն����ȫ���ļ��Ľ���
 * 			<ul>
 * 				<li>֪ͨ�ļ����ս����ܿ���������Ҫ����ļ����ս�����</li>
 * 				<li>�����ļ��ϲ�����֪ͨ�ļ����ս����ܿ�����������Ϣ��
 * 					<ol>
 * 						<li>��ʼ�ļ��ϲ�</li>
 * 						<li>�����ļ��ϲ�����</li>
 * 						<li>���һ���ļ��ĺϲ�</li>
 * 						<li>�ļ��ϲ����</li>
 * 					</ol>
 * 				</li>
 * 			</ul>
 * 		</li>
 * </ol>
 * 
 * @author ��Ѫ����
 */
public class FileReceiverCenter implements 
		IFileReceiverSpeaker, IFileReceiverListener,
		IFileReceiveControllerListener, IFileReceiveServerListener,
		IFileJoinSpeaker {
	/**
	 * ��������ļ�����
	 */
	private RequestFileMap requestFileMap;
	/**
	 * �ļ����շ�����
	 */
	private FileReceiverServer fileReceiverServer;
	/**
	 * �ļ����ս����ܿ������߳�
	 */
	private IFileReceiverListener fileReceiverListener;	
	private List<IFileJoinListener> fileJoinListenerList;
	/**
	 * �洢�������ļ��ġ���Ŀ¼��
	 */
	private String fileRoot;

	public FileReceiverCenter(String fileRoot, int port) throws IOException {
		this.fileRoot = fileRoot;
		this.requestFileMap = new RequestFileMap();
		this.fileJoinListenerList = new ArrayList<>();
		
		this.fileReceiverServer = new FileReceiverServer(port);
		fileReceiverServer.addFileReceiverListener(this);
	}
	
	public RequestFileMap getRequestFileMap() {
		return requestFileMap;
	}

	public void addRequestFile(String targetFileName, long fileSize) {
		requestFileMap.addFile(targetFileName, fileSize);
	}
	
	public void setRequestFile(List<RequestFileModel> fileList) {
		requestFileMap.setFileList(fileList);
	}
	
	public int getRequestFileCount() {
		return requestFileMap.getRequestFileCount();
	}

	public FileReceiverServer getFileReceiverServer() {
		return fileReceiverServer;
	}

	@Override
	public synchronized void dealNewReceiver(FileReceiver receiver) {
		// ���������µ��ļ����Ͷ˽��룺
		// 1������������
		// 2�������ļ��洢��·����
		receiver.addFileReceiverListener(this);
		receiver.setFileRoot(fileRoot);
	}

	@Override
	public synchronized void endReceiveOneFile(FileReceiver receiver) {
		// ����ѽ��յ��ļ���
		// ֪ͨ�����ܿ������������һ���ļ��Ľ���
		FileSectionModel receiveFile = receiver.getReceiveFile();
		try {
			requestFileMap.receivedFile(receiveFile);
		} catch (ReceivedFileNotExistException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public synchronized void onReceiveOver(FileReceiver receiver) {
		if(requestFileMap.isReceived()) {
			for(IFileJoinListener listener : fileJoinListenerList) {
				listener.onBeginJoin();
			}
			System.out.println("��ʼ�ϲ��ļ�");
			joiningFile();
			for(IFileJoinListener listener : fileJoinListenerList) {
				listener.onAllDone();
			}
			System.out.println("�ϲ����������չ��̽�����");
		}
	}
	
	@Override
	public synchronized void onGetFileList(FileReceiver receiver) {
		List<FileSectionModel> receiveFileList =
				receiver.getReceiveFileList();
		dealReceiveFileList(receiveFileList);
	}
	
	private void joinFileList(List<FileSectionModel> fileList) {
		if(fileList == null) {
			return;
		}
		
		RandomAccessFile raf = null;
		FileSectionModel targetFileModel = fileList.get(0);
		String targetFileName = fileRoot + targetFileModel.getTargetFileName();
		String tempFileName = fileRoot + targetFileModel.getTempFileName();
		try {
			if(!tempFileName.equals(targetFileName)) {
				raf = new RandomAccessFile(targetFileName, "w");
					joinOneFile(raf, targetFileModel);
			} else {
				raf = new RandomAccessFile(tempFileName, "rw");
				raf.seek(targetFileModel.getFileLen());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 1; i < fileList.size(); i++) {
			FileSectionModel nextFile = fileList.get(i);
			try {
				joinOneFile(raf, nextFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(IFileJoinListener listener : fileJoinListenerList) {
			listener.onJoinOne();
		}
	}
	
	private void joinOneFile(RandomAccessFile target, FileSectionModel receiveFile) 
			throws FileNotFoundException {
		String fileName = fileRoot + receiveFile.getTempFileName();
		RandomAccessFile receive = new RandomAccessFile(fileName, "r");
		byte[] buffer = null;
		
		final int defaultSize = 1 << 15;
		int restLen = (int) receiveFile.getFileLen();
		int bufferSize;
		int readLen;
		try {
			while(restLen > 0) {
				bufferSize = restLen > defaultSize ? defaultSize : restLen;
				buffer = new byte[bufferSize];
				readLen = receive.read(buffer);
				
				target.write(buffer, 0, readLen);
				restLen -= readLen;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			receive.close();
			File file = new File(fileName);
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void joiningFile() {
		int neededJoinFileCount = requestFileMap.getNeededJoinFileCount();
		if(neededJoinFileCount == 0) {
			return;
		}

		for(IFileJoinListener listener : fileJoinListenerList) {
			listener.onGetJoinCount(neededJoinFileCount);
		}
		
		Map<String, ReceiveFileModel> fileMap = requestFileMap.getFileMap();
		for(String targetFileName : fileMap.keySet()) {
			ReceiveFileModel file = fileMap.get(targetFileName);
			List<FileSectionModel> fileList = file.getReceiveFileList();
			joinFileList(fileList);
		}
	}
	
	/**
	 * 
	 * @param receiveFileList
	 */
	private void dealReceiveFileList(List<FileSectionModel> receiveFileList) {
		for(int index = 0; index < receiveFileList.size(); index++) {
			FileSectionModel receiveFile = receiveFileList.get(index);
			try {
				requestFileMap.markedFile(receiveFile);
			} catch (MarkedFileNotExistException e) {
				System.out.println(e.getMessage());
			}
		}
		if(requestFileMap.isMarked()) {
			fileReceiverServer.closeServer();
		}
	}
	
	@Override
	public synchronized void dealStopReceive() {
		fileReceiverServer.closeReceiver();
		fileReceiverServer.closeServer();
	}

	@Override
	public void addFileReceiverListener(IFileReceiverListener listener) {
		fileReceiverListener = listener;
	}

	@Override
	public void removeFileReceiverListener(IFileReceiverListener listener) {
		if(fileReceiverListener != listener) {
			return;
		}
		fileReceiverListener = null;
	}

	@Override
	public void onReceiveFailure(FileReceiver receiver) {
		// TODO ����ʧ��
	}

	@Override
	public void onBeginReceiveOneFile(FileReceiver receiver) {}

	@Override
	public void onReceiving(int receiveLen) {}

	@Override
	public void addFileJoinListener(IFileJoinListener listener) {
		if(fileJoinListenerList.contains(listener)) {
			return;
		}
		fileJoinListenerList.add(listener);
	}

	@Override
	public void removeFileJoinListener(IFileJoinListener listener) {
		if(!fileJoinListenerList.contains(listener)) {
			return;
		}
		fileJoinListenerList.remove(listener);
	}
}
