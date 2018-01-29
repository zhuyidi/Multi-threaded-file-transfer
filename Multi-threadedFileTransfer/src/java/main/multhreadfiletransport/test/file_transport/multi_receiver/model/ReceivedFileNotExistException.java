package multhreadfiletransport.test.file_transport.multi_receiver.model;

public class ReceivedFileNotExistException extends Exception {
	private static final long serialVersionUID = -8794665623714853794L;

	public ReceivedFileNotExistException() {
	}

	public ReceivedFileNotExistException(String message) {
		super(message);
	}

	public ReceivedFileNotExistException(Throwable cause) {
		super(cause);
	}

	public ReceivedFileNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReceivedFileNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
