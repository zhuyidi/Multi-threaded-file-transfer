package multhreadfiletransport.test.file_transport.multi_receiver;

public interface IFileReceiveServerSpeaker {
	void addFileReceiverListener(IFileReceiveServerListener serverListener);
	void removeFileReceiverListener(IFileReceiveServerListener serverListener);
	void catchNewReceiver(FileReceiver receiver);
}
