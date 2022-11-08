package database;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

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
	
	private static final String CHANGE_STATE_ON_COPY = ("UPDATE Copy\r\n"
			+ "SET State = ?\r\n"
			+ "WHERE ID = ?");
	private PreparedStatement changeStateOnCopy;

	private static final String FIND_RELEASED_SHIFT_COPIES = ("SELECT *\r\n"
			+ "FROM Copy c\r\n"
			+ "WHERE c.State = ?");
	private PreparedStatement findReleasedShiftCopies;
	
	private static final String FIND_SHIFTS_ON_SHIFT_ID = ("SELECT *\r\n"
			+ "FROM Shift s, Copy c\r\n"
			+ "WHERE s.ID = ?");
	private PreparedStatement findShiftsOnShiftID;
	
	private static final String FIND_COPY_VERSIONNUMBER_ON_ID = ("SELECT c.VersionNumber\r\n"
			+ "FROM Copy c\r\n"
			+ "WHERE c.ID = ?");
	private PreparedStatement findCopyVersionNumberOnID;
	
	private static final String SET_WORK_SCHEDULE_ID_ON_COPY= ("UPDATE Copy\r\n"
			+ "SET WorkScheduleID = ?\r\n"
			+ "WHERE ID = ?");
	private PreparedStatement setWorkScheduleIDOnCopy;
	
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
			findReleasedShiftCopies = con.prepareStatement(FIND_RELEASED_SHIFT_COPIES);
			findShiftsOnShiftID = con.prepareStatement(FIND_SHIFTS_ON_SHIFT_ID);
			findCopyVersionNumberOnID = con.prepareStatement(FIND_COPY_VERSIONNUMBER_ON_ID);
			setWorkScheduleIDOnCopy = con.prepareStatement(SET_WORK_SCHEDULE_ID_ON_COPY);
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
	
	public byte[] findCopyVersionNumberOnID(int id) throws DataAccessException {
		byte[] versionNumber = null;
		ResultSet rs;
		try {
			findCopyVersionNumberOnID.setInt(1, id);
			rs = findCopyVersionNumberOnID.executeQuery();
			if(rs.next()) {
				versionNumber = rs.getBytes("VersionNumber");
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return versionNumber;
	}
	
	public boolean takeNewShift(int copyID, int workScheduleID) throws DataAccessException {
		boolean taken = false;
		try {
			DBConnection.getInstance().startTransaction();
			setStateToOccupied(copyID);
			setWorkScheduleID(copyID, workScheduleID);
			DBConnection.getInstance().commitTransaction();
			taken = true;
		} catch(Exception e) {
			DBConnection.getInstance().rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return taken;
	}
	
	private void setStateToOccupied(int id) throws DataAccessException {
		try {
			changeStateOnCopy.setString(1, CopyState.OCCUPIED.getState());
			changeStateOnCopy.setInt(2, id);
			changeStateOnCopy.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		
	}
	
	private void setWorkScheduleID(int copyID, int workScheduleID) throws DataAccessException {
		try {
			setWorkScheduleIDOnCopy.setInt(1, workScheduleID);
			setWorkScheduleIDOnCopy.setInt(2, copyID);
			setWorkScheduleIDOnCopy.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		
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
	
	public ArrayList<Copy> findReleasedShiftCopies() throws DataAccessException {
		ArrayList<Copy> releasedShiftCopies = new ArrayList<>();
		ResultSet rs = null;
		try {
			findReleasedShiftCopies.setString(1, CopyState.RELEASED.getState());
			rs = findReleasedShiftCopies.executeQuery();
			releasedShiftCopies = buildCopyObjects(rs);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return releasedShiftCopies;
	}
	
	private ArrayList<Copy> buildCopyObjects(ResultSet rs) throws DataAccessException {
		ArrayList<Copy> shiftCopies = new ArrayList<>();
		try {
			while(rs.next()) {
				Copy copy = buildCopyObject(rs);
				shiftCopies.add(copy);
			} 
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shiftCopies;

	}
	

	private Copy buildCopyObject(ResultSet rs) throws DataAccessException {
		Copy copy = null;
		ResultSet rs2;
		Shift shift = null;
		try {
			int id = rs.getInt("ID");
			int shiftID = rs.getInt("ShiftID");
			findShiftsOnShiftID.setInt(1, shiftID);
			rs2 = findShiftsOnShiftID.executeQuery();
			if(rs2.next()) {
				shift = buildShiftObject(rs2);
			}
			byte[] version = rs.getBytes("VersionNumber");
			java.sql.Date date = rs.getDate("Date");
			LocalDate localDate = date.toLocalDate();
			String state = rs.getString("State");
			copy = new Copy(id, shift, null, version, localDate, state);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return copy;
	}

}
