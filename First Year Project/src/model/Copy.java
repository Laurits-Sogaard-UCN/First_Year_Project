package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Copy {
	
	private int id;
	private Shift shift;
	private WorkSchedule workSchedule;
	private LocalDate date;
	private String state;
	private LocalDateTime releasedAt;

	/**
	 * Constructor to initialize instance variables.
	 * @param id
	 * @param shift
	 * @param workSchedule
	 * @param versionNumber
	 * @param date
	 * @param state
	 * @param releasedAt
	 */
	public Copy(int id, Shift shift, WorkSchedule workSchedule, LocalDate date, String state, LocalDateTime releasedAt) {
		this.id = id;
		this.shift = shift;
		this.workSchedule = workSchedule;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}
	
	/**
	 * Constructor to initialize instance variables.
	 * @param id
	 * @param shift
	 * @param date
	 * @param state
	 * @param releasedAt
	 */
	public Copy(int id, Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		this.id = id;
		this.shift = shift;
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
	public Copy(Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		this.shift = shift;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}

	/**
	 * Gets id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets a new id.
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets shift.
	 * @return the shift
	 */
	public Shift getShift() {
		return shift;
	}

	/**
	 * Sets a new shift.
	 * @param shift the shift to set
	 */
	public void setShift(Shift shift) {
		this.shift = shift;
	}

	/**
	 * Gets work schedule. 
	 * @return the workSchedule
	 */
	public WorkSchedule getWorkSchedule() {
		return workSchedule;
	}

	/**
	 * Sets a new work schedule.
	 * @param workSchedule the workSchedule to set
	 */
	public void setWorkSchedule(WorkSchedule workSchedule) {
		this.workSchedule = workSchedule;
	}

	/**
	 * Gets date.
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets a new date.
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Gets state.
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets a new state.
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets released at.
	 * @return the releasedAt
	 */
	public LocalDateTime getReleasedAt() {
		return releasedAt;
	}

	/**
	 * Sets a new released at.
	 * @param releasedAt the releasedAt to set
	 */
	public void setReleasedAt(LocalDateTime releasedAt) {
		this.releasedAt = releasedAt;
	}


}
