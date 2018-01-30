package sockettests.simplefiletransport.file_transport.sender;

public class SendFileNotExistException extends Exception {
	private static final long serialVersionUID = -7016129986002811856L;

	public SendFileNotExistException() {
	}

	public SendFileNotExistException(String message) {
		super(message);
	}

	public SendFileNotExistException(Throwable cause) {
		super(cause);
	}

	public SendFileNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendFileNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
