package database;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Copy;
import model.CopyState;
import model.Shift;
import utility.DBMessages;
import utility.DataAccessException;

public class ShiftDB implements ShiftDBIF {
	
	private static final String FIND_SHIFT_ON_FROM_AND_TO = ("SELECT *\r\n"
			+ "FROM Shift s\r\n"
			+ "WHERE s.FromHour = ?\r\n"
			+ "and s.ToHour = ?");
	private PreparedStatement findShiftOnFromAndTo;
	
	private static final String INSERT_WORKSHIFT_COPY = ("INSERT INTO Copy(ShiftID, Date, State)\r\n"
			+ "VALUES (?, ?, ?)");
	private PreparedStatement insertWorkShiftCopy;
	
	private static final String CHANGE_STATE_ON_COPY = ("");
	private PreparedStatement changeStateOnCopy;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public ShiftDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 */
	private void init() throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		try {
			findShiftOnFromAndTo = con.prepareStatement(FIND_SHIFT_ON_FROM_AND_TO);
			insertWorkShiftCopy = con.prepareStatement(INSERT_WORKSHIFT_COPY);
			changeStateOnCopy = con.prepareStatement(CHANGE_STATE_ON_COPY);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	public Shift findShiftOnFromAndTo(int fromHour, int toHour) throws DataAccessException {
		ResultSet rs;
		Shift shift = null;
		try {
			findShiftOnFromAndTo.setInt(1, fromHour);
			findShiftOnFromAndTo.setInt(2, toHour);
			rs = findShiftOnFromAndTo.executeQuery();
			if(rs.next()) {
				shift = buildShiftObject(rs);
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return shift;
	}
	
	public boolean completeReleaseNewShifts(ArrayList<Copy> copies) throws DataAccessException {
		boolean completed = false;
		int rowsAffected = -1;
		try {
			DBConnection.getInstance().startTransaction();
			for(Copy element : copies) {
				int fromHour = element.getShift().getFromHour();
				int toHour = element.getShift().getToHour();
				Shift shift = findShiftOnFromAndTo(fromHour, toHour);
				int id = shift.getID();
				LocalDate localDate = element.getDate();
				java.sql.Date date = java.sql.Date.valueOf(localDate);
				insertWorkShiftCopy.setInt(1, id);
				insertWorkShiftCopy.setDate(2, date);
				insertWorkShiftCopy.setString(3, CopyState.RELEASED.getState());
				rowsAffected += insertWorkShiftCopy.executeUpdate();
			}
			DBConnection.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnection.getInstance().rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_INSERT, e);
		}
		if(rowsAffected >= 0) {
			completed = true;
		}
		return completed;
	}
	
	public boolean setStateToOccupied(Copy copy) throws DataAccessException {
		boolean success = false;
		int rowsAffected = -1;
		try {
			changeStateOnCopy.setString(1, CopyState.OCCUPIED.getState());
			rowsAffected = changeStateOnCopy.executeUpdate();
			if(rowsAffected > 0) {
				success = true;
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return success;
	}
	
	private Shift buildShiftObject(ResultSet rs) throws DataAccessException {
		Shift shift;
		try {
			int fromHour = rs.getInt("FromHour");
			int toHour = rs.getInt("ToHour");
			int id = rs.getInt("ID");
			shift = new Shift(fromHour, toHour, id);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shift;
	}

}
