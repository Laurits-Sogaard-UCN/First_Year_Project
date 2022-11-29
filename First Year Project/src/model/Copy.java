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
	 * Gets date.
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Gets released at.
	 * @return the releasedAt
	 */
	public LocalDateTime getReleasedAt() {
		return releasedAt;
	}



}
