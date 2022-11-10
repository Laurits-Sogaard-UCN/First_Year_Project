package database;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;

import model.Copy;
import model.Shift;
import utility.CopyState;
import utility.DBMessages;
import utility.DataAccessException;

import static java.time.temporal.ChronoUnit.HOURS;;

public class ShiftDB implements ShiftDBIF {
	
	private static final String FIND_SHIFT_ON_FROM_AND_TO = ("SELECT *\r\n"
			+ "FROM Shift s\r\n"
			+ "WHERE s.FromHour = ?\r\n"
			+ "and s.ToHour = ?");
	private PreparedStatement findShiftOnFromAndTo;
	
	private static final String INSERT_WORKSHIFT_COPY = ("INSERT INTO Copy(ShiftID, Date, State, ReleasedAt)\r\n"
			+ "VALUES (?, ?, ?, ?)");
	private PreparedStatement insertWorkShiftCopy;
	
	private static final String CHECK_REST_PERIOD = ("SELECT s.FromHour, s.ToHour, c.Date\r\n"
			+ "FROM Shift s, Copy c\r\n"
			+ "WHERE c.WorkScheduleID = ?\r\n"
			+ "and c.ShiftID = s.ID");
	private PreparedStatement checkRestPeriod;
	
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
	
	private static final String SET_WORK_SCHEDULE_ID_ON_COPY = ("UPDATE Copy\r\n"
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
			checkRestPeriod = con.prepareStatement(CHECK_REST_PERIOD);
			changeStateOnCopy = con.prepareStatement(CHANGE_STATE_ON_COPY);
			findReleasedShiftCopies = con.prepareStatement(FIND_RELEASED_SHIFT_COPIES);
			findShiftsOnShiftID = con.prepareStatement(FIND_SHIFTS_ON_SHIFT_ID);
			findCopyVersionNumberOnID = con.prepareStatement(FIND_COPY_VERSIONNUMBER_ON_ID);
			setWorkScheduleIDOnCopy = con.prepareStatement(SET_WORK_SCHEDULE_ID_ON_COPY);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	public Shift findShiftOnFromAndTo(LocalTime fromHour, LocalTime toHour) throws DataAccessException {
		ResultSet rs;
		Shift shift = null;
		try {
			String from = fromHour.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME);
			String to = toHour.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME);
			findShiftOnFromAndTo.setString(1, from);
			findShiftOnFromAndTo.setString(2, to);
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
				LocalTime fromHour = element.getShift().getFromHour();
				LocalTime toHour = element.getShift().getToHour();
				Shift shift = findShiftOnFromAndTo(fromHour, toHour);
				int id = shift.getID();
				LocalDate localDate = element.getDate();
				java.sql.Date date = java.sql.Date.valueOf(localDate);
				insertWorkShiftCopy.setInt(1, id);
				insertWorkShiftCopy.setDate(2, date);
				insertWorkShiftCopy.setString(3, CopyState.RELEASED.getState());
				Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
				insertWorkShiftCopy.setTimestamp(4, timestamp);
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
	
	public boolean takeNewShift(Copy copy, int workScheduleID) throws DataAccessException {
		boolean taken = false;
		if(checkRestPeriod(copy, workScheduleID)) {
			try {
				DBConnection.getInstance().startTransaction();
				setStateToOccupied(copy);
				setWorkScheduleIDOnCopy(copy, workScheduleID);
				DBConnection.getInstance().commitTransaction();
				taken = true;
			} catch(Exception e) {
				DBConnection.getInstance().rollbackTransaction();
				throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
			}
		}
		return taken;
	}
	
	private boolean checkRestPeriod(Copy copy, int workScheduleID) throws DataAccessException {
		boolean sufficientRest = false;
		ResultSet rs;
		try {
			checkRestPeriod.setInt(1, workScheduleID);
			rs = checkRestPeriod.executeQuery();
			if(rs.next()) {
				do {
					LocalDate date = rs.getDate("Date").toLocalDate();
					String fromHourString = rs.getString("FromHour");
					LocalTime fromHour = LocalTime.parse(fromHourString);
					String toHourString = rs.getString("ToHour");
					LocalTime toHour = LocalTime.parse(toHourString);
					LocalDateTime dateTimeFrom = LocalDateTime.of(date, fromHour);
					LocalDateTime dateTimeTo = LocalDateTime.of(date, toHour);
					
					LocalDate copyDate = copy.getDate();
					LocalTime copyFromHour = copy.getShift().getFromHour();
					LocalTime copyToHour = copy.getShift().getToHour();
					LocalDateTime copyDateTimeFrom = LocalDateTime.of(copyDate, copyFromHour);
					LocalDateTime copyDateTimeTo = LocalDateTime.of(copyDate, copyToHour);
					
					if(!copyDate.isEqual(date) && HOURS.between(dateTimeFrom, copyDateTimeTo) >= 11 && HOURS.between(dateTimeTo, copyDateTimeFrom) >= 11) {
						sufficientRest = true;
					}
				} while(rs.next());
			}
			else {
				sufficientRest = true;
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return sufficientRest;
	}
	
	private void setStateToOccupied(Copy copy) throws DataAccessException {
		int id = copy.getId();
		try {
			changeStateOnCopy.setString(1, CopyState.OCCUPIED.getState());
			changeStateOnCopy.setInt(2, id);
			changeStateOnCopy.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		
	}
	
	private void setWorkScheduleIDOnCopy(Copy copy, int workScheduleID) throws DataAccessException {
		int copyID = copy.getId();
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
			LocalTime fromHour = rs.getTime("FromHour").toLocalTime();
			LocalTime toHour = rs.getTime("ToHour").toLocalTime();
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
			Date releasedAtTimestamp = rs.getTimestamp("ReleasedAt");
			LocalDateTime releasedAt = releasedAtTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			copy = new Copy(id, shift, null, version, localDate, state, releasedAt);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return copy;
	}

}
