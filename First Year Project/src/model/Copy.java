package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Copy {
	
	private int id;
	private Shift shift;
	private WorkSchedule workSchedule;
	private byte[] versionNumber;
	private LocalDate date;
	private String state;
	private LocalDateTime releasedAt;

	/**
	 * @param id
	 * @param shift
	 * @param workSchedule
	 * @param versionNumber
	 * @param date
	 * @param state
	 * @param releasedAt
	 */
	public Copy(int id, Shift shift, WorkSchedule workSchedule, byte[] versionNumber, LocalDate date, String state,
			LocalDateTime releasedAt) {
		this.id = id;
		this.shift = shift;
		this.workSchedule = workSchedule;
		this.versionNumber = versionNumber;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}
	
	public Copy(Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		this.shift = shift;
		this.date = date;
		this.state = state;
		this.releasedAt = releasedAt;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the shift
	 */
	public Shift getShift() {
		return shift;
	}

	/**
	 * @param shift the shift to set
	 */
	public void setShift(Shift shift) {
		this.shift = shift;
	}

	/**
	 * @return the workSchedule
	 */
	public WorkSchedule getWorkSchedule() {
		return workSchedule;
	}

	/**
	 * @param workSchedule the workSchedule to set
	 */
	public void setWorkSchedule(WorkSchedule workSchedule) {
		this.workSchedule = workSchedule;
	}

	/**
	 * @return the versionNumber
	 */
	public byte[] getVersionNumber() {
		return versionNumber;
	}
	
	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(byte[] versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
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

	/**
	 * @return the releasedAt
	 */
	public LocalDateTime getReleasedAt() {
		return releasedAt;
	}

	/**
	 * @param releasedAt the releasedAt to set
	 */
	public void setReleasedAt(LocalDateTime releasedAt) {
		this.releasedAt = releasedAt;
	}
	
	
	

}
