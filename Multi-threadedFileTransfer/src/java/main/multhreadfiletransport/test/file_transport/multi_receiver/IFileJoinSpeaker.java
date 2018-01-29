package multhreadfiletransport.test.file_transport.multi_receiver;

public interface IFileJoinSpeaker {
	void addFileJoinListener(IFileJoinListener listener);
	void removeFileJoinListener(IFileJoinListener listener);
}
