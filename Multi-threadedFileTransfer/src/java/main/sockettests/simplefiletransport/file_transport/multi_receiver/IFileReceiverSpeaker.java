package sockettests.simplefiletransport.file_transport.multi_receiver;

public interface IFileReceiverSpeaker {
	void addFileReceiverListener(IFileReceiverListener listener);
	void removeFileReceiverListener(IFileReceiverListener listener);
}
