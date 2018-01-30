package sockettests.simplefiletransport.file_transport.multi_receiver.model;

public class MarkedFileNotExistException extends Exception {
	private static final long serialVersionUID = -8388137907608594174L;

	public MarkedFileNotExistException() {
	}

	public MarkedFileNotExistException(String message) {
		super(message);
	}

	public MarkedFileNotExistException(Throwable cause) {
		super(cause);
	}

	public MarkedFileNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarkedFileNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
