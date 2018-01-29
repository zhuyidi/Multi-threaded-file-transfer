package multhreadfiletransport.test.file_transport.multi_receiver;

public interface IFileJoinListener {
	void onBeginJoin();
	void onGetJoinCount(int count);
	void onJoinOne();
	void onAllDone();
}
