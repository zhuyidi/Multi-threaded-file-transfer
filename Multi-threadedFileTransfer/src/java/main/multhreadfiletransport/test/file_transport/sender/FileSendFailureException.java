package multhreadfiletransport.test.file_transport.sender;

public class FileSendFailureException extends Exception {
	private static final long serialVersionUID = 7831860467373024816L;

	public FileSendFailureException() {
	}

	public FileSendFailureException(String message) {
		super(message);
	}

	public FileSendFailureException(Throwable cause) {
		super(cause);
	}

	public FileSendFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileSendFailureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
