package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShiftCopy {
	
	private int ID;
	private Shift shift;
	private WorkSchedule workSchedule;
	private LocalDate date;
	private String state;
	private LocalDateTime releasedAt;
	
	/**
	 * Constructor to initialize instance variables.
	 * @param id
	 * @param shift
	 * @param date
	 * @param state
	 * @param releasedAt
	 */
	public ShiftCopy(int ID, Shift shift, WorkSchedule workSchedule, LocalDate date, String state, LocalDateTime releasedAt) {
		this.ID = ID;
		this.shift = shift;
		this.workSchedule = workSchedule;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}
	
	/**
	 * Constructor to initialize instance variables.
	 * @param shift
	 * @param date
	 * @param state
	 * @param releasedAt
	 */
	public ShiftCopy(Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		this.shift = shift;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}
	

	/**
	 * Gets id.
	 * @return the id
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Sets new ID.
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * Gets shift.
	 * @return the shift
	 */
	public Shift getShift() {
		return shift;
	}
	
	/**
	 * Gets work schedule.
	 * @return workSchedule
	 */
	public WorkSchedule getWorkSchedule() {
		return workSchedule;
	}

	/**
	 * Gets date.
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/**
	 * Gets state.
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * Gets released at.
	 * @return the releasedAt
	 */
	public LocalDateTime getReleasedAt() {
		return releasedAt;
	}

}

