package model;

import java.time.LocalDate;

public class Copy {
	
	private Shift shift;
	private WorkSchedule workSchedule;
	private Byte[] versionNumber;
	private LocalDate date;
	private String state;
	

	/**
	 * @param shift
	 * @param workSchedule
	 * @param versionNumber
	 * @param date
	 * @param state
	 */
	public Copy(Shift shift, WorkSchedule workSchedule, Byte[] versionNumber, LocalDate date, String state) {
		super();
		this.shift = shift;
		this.workSchedule = workSchedule;
		this.versionNumber = versionNumber;
		this.date = date;
		this.state = state;
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
	public Byte[] getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(Byte[] versionNumber) {
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
	
	

}
