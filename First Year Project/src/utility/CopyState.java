package utility;

public enum CopyState {
	
	RELEASED("Released"), OCCUPIED("Occupied"), DELEGATED("Delegated");
	
	private final String stringState;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @param stringState
	 */
	CopyState(String stringState) {
		this.stringState = stringState;
	}
	
	/**
	 * Gets string state
	 * @return stringState
	 */
	public String getState() {
		return stringState;
	}

}
