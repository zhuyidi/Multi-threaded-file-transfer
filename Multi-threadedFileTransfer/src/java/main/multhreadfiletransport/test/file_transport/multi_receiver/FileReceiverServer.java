package multhreadfiletransport.test.file_transport.multi_receiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * �ļ����շ�����<br>
 * �����ļ����շ������������ļ����ͷ����������󣬲���FileReceiver
 * 
 * @author ��Ѫ����
 */
public class FileReceiverServer 
		implements Runnable, IFileReceiveServerSpeaker {
	private ServerSocket serverSocket;
	private List<FileReceiver> receiverList;
	
	private List<IFileReceiveServerListener> listenerList;
	private FileReceiver fileReceiver;
	
	public FileReceiverServer(int port) throws IOException {
		receiverList = new ArrayList<>();
		listenerList = new ArrayList<>();
		this.serverSocket = new ServerSocket(port);
	}
	
	public void startReceiverServer() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		FileReceiverCenterThreadState.isAlive = true;
		while(true) {
			try {
				// �������Ͷ���������
				Socket socket = serverSocket.accept();
				// һ���µķ��Ͷ������ӣ��������նˣ�
				fileReceiver = new FileReceiver(socket);
				// ���뵽���ն��б�
				receiverList.add(fileReceiver);
				// ֪ͨ��������
				catchNewReceiver(fileReceiver);
			} catch (IOException e) {
				// ������Ϊ���ߣ����ߴ�����ϣ���Ϊ�ر�serverSocket��
				// �Ӷ���ֹaccept()�������߳�
				break;
			}
		}
		closeServer();
		FileReceiverCenterThreadState.isAlive = false;
	}

	protected void closeReceiver() {
		if(receiverList == null || receiverList.size() <= 0) {
			return;
		}
		for(FileReceiver receiver : receiverList) {
			receiver.close();
		}
	}
	
	protected void closeServer() {
		try {
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addFileReceiverListener(IFileReceiveServerListener serverListener) {
		if(listenerList.contains(serverListener)) {
			return;
		}
		listenerList.add(serverListener);
	}

	@Override
	public void removeFileReceiverListener(IFileReceiveServerListener serverListener) {
		if(!listenerList.contains(serverListener)) {
			return;
		}
		listenerList.add(serverListener);
	}

	@Override
	public void catchNewReceiver(FileReceiver receiver) {
		if(listenerList.size() <= 0) {
			return;
		}
		for(IFileReceiveServerListener listener : listenerList) {
			listener.dealNewReceiver(receiver);
		}
	}
}
