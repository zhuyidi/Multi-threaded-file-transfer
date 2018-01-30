package sockettests.simplefiletransport.file_transport.multi_receiver;

public interface IFileReceiveControllerSpeaker {
	void setFileReceiveControllerListener(IFileReceiveControllerListener listner);
	void removeFileReceiveControllerListener(IFileReceiveControllerListener listner);
	void stopReceive();
}
