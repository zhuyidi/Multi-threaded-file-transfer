package sockettests.simplefiletransport.file_transport.sender;

public interface ISendTaskSpeaker {
	void addListener(ISendTaskListener listener);
	void removeListener(ISendTaskListener listener);
	void sendAction(ESendAction action);
}
