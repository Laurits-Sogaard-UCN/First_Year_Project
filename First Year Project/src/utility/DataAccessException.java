package utility;

public class DataAccessException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message
	 * @param e
	 */
	public DataAccessException(String message, Throwable e) {
		super(message, e);
	}
	
}
