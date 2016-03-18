package after.acco.paypal.solution;

public class InvalidStateException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7101964170407639049L;

	public InvalidStateException(String errorMessage) {
		super(errorMessage);
	}
}
