package model;

public class WorkShift extends Shift {
	
	private String state;

	/**
	 * @param fromHour
	 * @param toHour
	 * @param shiftType
	 * @param state
	 */
	public WorkShift(int fromHour, int toHour, String shiftType, String state) {
		super(fromHour, toHour, shiftType);
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	

}
