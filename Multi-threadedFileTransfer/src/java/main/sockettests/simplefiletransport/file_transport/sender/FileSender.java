package sockettests.simplefiletransport.file_transport.sender;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class FileSender implements Runnable, ISendTaskSpeaker {
	private Socket socket;
	private String ip;
	private int port;
	
	private byte[] buffer;
	private int bufferSize;
	private BufferedOutputStream netOutputStream;
	private List<SendFileModel> sendFileList;
	private Object sign;

	private ISendTaskListener listener;
	
	protected FileSender(String ip, int port, int bufferSize) {
		sign = new Object();
		this.ip = ip;
		this.port = port;
		this.buffer = new byte[bufferSize];
		this.bufferSize = bufferSize;
	}
	
	protected void closeSender() {
		if(netOutputStream != null) {
			try {
				netOutputStream.close();
				netOutputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected FileSender createSender() {
		int count = 0;
		do {
			try {
				socket = new Socket(ip, port);
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			if(socket == null) {
				synchronized (sign) {
					try {
						sign.wait(1000 * ++count);
					} catch (InterruptedException e) {}
				}
			}
		} while (socket == null && count < 3);
		try {
			netOutputStream = new BufferedOutputStream(
					socket.getOutputStream(), bufferSize);
		} catch (IOException e) {
		}
		
		return this;
	}
	
	protected FileSender setSendFileList(List<SendFileModel> sendFileList) {
		this.sendFileList = sendFileList;
		
		return this;
	}
	
	protected void send() {
		if(socket == null || netOutputStream == null || sendFileList == null) {
			return;
		}
		new Thread(this).start();
	}
	
	private void sendFileHead(List<SendFileModel> sendFileList) 
			throws IOException {
		StringBuilder str = new StringBuilder();
		byte[] writeBuffer;
		int writeLength;
		
		for(SendFileModel sendFile : sendFileList) {
			str.append(sendFile.getTargetName());
			str.append("\t");
			str.append(sendFile.getTempName());
			str.append("\t");
			str.append(sendFile.getStartPos());
			str.append("\t");
			str.append(sendFile.getFileLength());
			str.append("\n");
		}
		writeBuffer = str.toString().getBytes();
		writeLength = writeBuffer.length;
		netOutputStream.write(String.valueOf(writeLength + 100000000)
				.substring(1).getBytes());
		netOutputStream.flush();
		netOutputStream.write(writeBuffer);
		netOutputStream.flush();
	}
	
	private void sendOneFile(String orgFileName, long startPos, long fileSize) 
			throws Exception {
		int readLen;
		long restLen = fileSize;
		BufferedInputStream bis = null;
		
		try {
			bis = new BufferedInputStream(
					new FileInputStream(orgFileName), bufferSize);
			bis.skip(startPos);
			while(restLen > 0) {
				readLen = (int) (restLen > bufferSize ? bufferSize : restLen);
				bis.read(buffer, 0, readLen);
				restLen -= readLen;
				
				netOutputStream.write(buffer, 0, readLen);
				netOutputStream.flush();
			}
			if(bis != null) {
				bis.close();
			}
		} catch (FileNotFoundException e) {
			throw new SendFileNotExistException("�������ļ�[" 
					+ orgFileName + "]δ�ҵ���");
		} catch (IOException e) {
			throw new FileSendFailureException("�ļ�[" 
					+ orgFileName + "]����ʧ�ܣ�");
		}
	}
	
	@Override
	public void run() {
		sendAction(ESendAction.BEGIN_SEND);
		try {
			sendAction(ESendAction.SENDDING_FILE_HEAD);
			sendFileHead(sendFileList);
			sendAction(ESendAction.SENDDING_FILE);
			for(SendFileModel sendFile : sendFileList) {
				String file = sendFile.getOrgName();
				long start = sendFile.getStartPos();
				long len = sendFile.getFileLength();
				sendOneFile(file, start, len);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				netOutputStream.close();
				socket.close();
			} catch (IOException e) {}
			sendAction(ESendAction.SEND_OVER);
		}
	}

	@Override
	public void addListener(ISendTaskListener listener) {
		if(this.listener == null || this.listener != listener) {
			this.listener = listener;
		}
	}

	@Override
	public void removeListener(ISendTaskListener listener) {
		if(this.listener == null || this.listener != listener) {
			return;
		}
		this.listener = null;
	}

	@Override
	public void sendAction(ESendAction action) {
		if(listener == null) {
			return;
		}
		listener.dealSendAction(action);
	}
}
