package utility;

public enum CopyState {
	
	RELEASED("Released"), OCCUPIED("Occupied"), DELEGATED("Delegated");
	
	private final String stringState;
	
	CopyState(String stringState) {
		this.stringState = stringState;
	}
	
	public String getState() {
		return stringState;
	}

}
