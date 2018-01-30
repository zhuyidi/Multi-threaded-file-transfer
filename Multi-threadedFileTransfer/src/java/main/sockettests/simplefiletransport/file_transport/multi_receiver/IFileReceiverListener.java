package sockettests.simplefiletransport.file_transport.multi_receiver;

public interface IFileReceiverListener {
	void onGetFileList(FileReceiver receiver);
	void onBeginReceiveOneFile(FileReceiver receiver);
	void onReceiving(int receiveLen);
	void endReceiveOneFile(FileReceiver receiver);
	void onReceiveOver(FileReceiver receiver);
	void onReceiveFailure(FileReceiver receiver);
}
