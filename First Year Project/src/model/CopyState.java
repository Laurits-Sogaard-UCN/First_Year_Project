package model;

public enum CopyState {
	RELEASED("Released"), OCCUPIED("Occupied");
	
	private final String stringState;
	
	CopyState(String stringState) {
		this.stringState = stringState;
	}
	
	public String getState() {
		return stringState;
	}

}
