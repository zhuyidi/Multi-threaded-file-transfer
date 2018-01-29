package multhreadfiletransport.test.file_transport.multi_receiver;

import multhreadfiletransport.test.file_transport.multi_receiver.model.FileSectionModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class FileReceiver implements IFileReceiverSpeaker, Runnable {
	private Socket socket;
	private BufferedInputStream bis;
	
	private List<FileSectionModel> receiveFileList;
	private List<IFileReceiverListener> listenerList;
	private FileSectionModel receiveFile;
	
	private String fileRoot;
	
	public static final int DEFAULT_BUFFER_SIZE = 1 << 15;
	
	public FileReceiver(Socket socket) {
		try {
			this.socket = socket;
			this.bis = new BufferedInputStream(socket.getInputStream());
			this.receiveFileList = new ArrayList<>();
			this.listenerList = new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<FileSectionModel> getReceiveFileList() {
		return receiveFileList;
	}

	public void setFileRoot(String fileRoot) {
		this.fileRoot = fileRoot;
	}

	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public String getHostName() {
		return socket.getInetAddress().getHostName();
	}
	
	public void startReceive() {
		new Thread(this).start();
	}

	private void receiveFileListInfo() throws IOException {
		StringBuffer str = new StringBuffer();
		byte[] buffer = new byte[8];
		
		bis.read(buffer);
		int restLen = Integer.valueOf(new String(buffer, 0, 8));
		int bufferSize = 0;
		int receiveLen = 0;
		
		while(restLen > 0) {
			bufferSize = restLen > DEFAULT_BUFFER_SIZE 
					? DEFAULT_BUFFER_SIZE
					: restLen;
			buffer = new byte[bufferSize];
			receiveLen = bis.read(buffer);
			str.append(new String(buffer, 0, receiveLen));
			restLen -= receiveLen;
		}
		
		String[] fileListInfos = str.toString().split("\n");
		for(String fileInfoString : fileListInfos) {
			String[] fileInfos = fileInfoString.split("\t");
			long fileLen = Long.valueOf(fileInfos[3]);
			FileSectionModel receiveFile = new FileSectionModel();
			receiveFile.setTargetFileName(fileInfos[0]);
			receiveFile.setTempFileName(fileInfos[1]);
			receiveFile.setStartPos(Long.valueOf(fileInfos[2]));
			receiveFile.setFileLen(fileLen);
			receiveFile.setReceived(false);
			receiveFileList.add(receiveFile);
		}
		
		for(IFileReceiverListener listener : listenerList) {
			listener.onGetFileList(this);
		}
	}
	
	private void sendOneFile(FileSectionModel receiveFile) 
			throws IOException {
		byte[] buffer;
		int bufferSize;
		int receivedLength;
		
		this.receiveFile = receiveFile;
		for(IFileReceiverListener listener : listenerList) {
			listener.onBeginReceiveOneFile(this);
		}
		
		String tempFileName = fileRoot + receiveFile.getTempFileName();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(tempFileName));
		int restSize = (int) receiveFile.getFileLen();
		while(restSize > 0) {
			bufferSize = restSize > DEFAULT_BUFFER_SIZE
					? DEFAULT_BUFFER_SIZE
					: restSize;
			buffer = new byte[bufferSize];
			receivedLength = bis.read(buffer);
			bos.write(buffer, 0, receivedLength);
			
			for(IFileReceiverListener listener : listenerList) {
				listener.onReceiving(receivedLength);
			}
			restSize -= receivedLength;
		}
		bos.close();
		for(IFileReceiverListener listener : listenerList) {
			listener.endReceiveOneFile(this);
		}
	}

	public FileSectionModel getReceiveFile() {
		return receiveFile;
	}

	@Override
	public void run() {
		try {
			receiveFileListInfo();
			for(FileSectionModel receiveFile : receiveFileList) {
				sendOneFile(receiveFile);
			}
			
			for(IFileReceiverListener listener : listenerList) {
				listener.onReceiveOver(this);
			}
		} catch (IOException e) {
			System.out.println("����ʧ�ܣ���");
			for(IFileReceiverListener listener : listenerList) {
				listener.onReceiveFailure(this);
			}
		}
		close();
	}

	@Override
	public void addFileReceiverListener(IFileReceiverListener listener) {
		if(listenerList.contains(listener)) {
			return;
		}
		listenerList.add(listener);
	}

	@Override
	public void removeFileReceiverListener(IFileReceiverListener listener) {
		if(!listenerList.contains(listener)) {
			return;
		}
		listenerList.remove(listener);
	}

	public void close() {
		try {
			if(bis != null) {
				bis.close();
				bis = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
