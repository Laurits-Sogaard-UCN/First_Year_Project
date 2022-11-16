package database;

import java.sql.Connection;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import model.Copy;
import model.Shift;
import utility.CopyState;
import utility.DBMessages;
import utility.DataAccessException;;

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
		String from;
		String to;
		
		try {
			from = fromHour.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME);
			to = toHour.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME);
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
	
	public boolean completeReleaseNewShifts(ArrayList<Copy> shiftCopies) throws DataAccessException {
		boolean completed = false;
		int rowsAffected = -1;
		Shift shift;
		int id;
		LocalDate localDate;
		java.sql.Date date;
		Timestamp timestamp;
		
		try {
			DBConnection.getInstance().startTransaction(); // TODO Spørg Lars om transaktion i et loop?
			
			for(Copy element : shiftCopies) {
				shift = element.getShift();
				id = shift.getID();
				localDate = element.getDate();
				date = java.sql.Date.valueOf(localDate);
				timestamp = Timestamp.valueOf(LocalDateTime.now());
				
				insertWorkShiftCopy.setInt(1, id);
				insertWorkShiftCopy.setDate(2, date);
				insertWorkShiftCopy.setString(3, CopyState.RELEASED.getState());
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
	
	public boolean takeNewShift(Copy copy, int workScheduleID, String state) throws DataAccessException {
		boolean taken = false;
		boolean sufficientRest = checkRestPeriod(copy, workScheduleID);
		
		if(sufficientRest) {
			try {
				DBConnection.getInstance().startTransaction(); // TODO spørg Lars om interne metodekald i en transaktion.
				setState(copy, state);
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
		
		// TODO måske vi skal finde nogle mindre forvirrende navne for flere af de her variable?
		LocalDate date;
		String fromHourString;
		LocalTime fromHour;
		String toHourString;
		LocalTime toHour;
		LocalDateTime dateTimeFrom;
		LocalDateTime dateTimeTo;
		
		LocalDate copyDate;
		LocalTime copyFromHour;
		LocalTime copyToHour;
		LocalDateTime copyDateTimeFrom;
		LocalDateTime copyDateTimeTo;
		
		long durationBetween1;
		long durationBetween2;
		
		try {
			checkRestPeriod.setInt(1, workScheduleID);
			rs = checkRestPeriod.executeQuery();
			
			if(rs.next()) {
				do {
					date = rs.getDate("Date").toLocalDate();
					fromHourString = rs.getString("FromHour");
					fromHour = LocalTime.parse(fromHourString);
					toHourString = rs.getString("ToHour");
					toHour = LocalTime.parse(toHourString);
					dateTimeFrom = LocalDateTime.of(date, fromHour);
					dateTimeTo = LocalDateTime.of(date, toHour);
					
					copyDate = copy.getDate();
					copyFromHour = copy.getShift().getFromHour();
					copyToHour = copy.getShift().getToHour();
					copyDateTimeFrom = LocalDateTime.of(copyDate, copyFromHour);
					copyDateTimeTo = LocalDateTime.of(copyDate, copyToHour);
					
					durationBetween1 = Duration.between(dateTimeFrom, copyDateTimeTo).toHours();
					durationBetween2 = Duration.between(dateTimeTo, copyDateTimeFrom).toHours();
					
					if(!date.isEqual(copyDate) && durationBetween1 >= 11 && durationBetween2 >= 11) {
						sufficientRest = true;
					}
					else {
						sufficientRest = false;
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
	
	private void setState(Copy copy, String state) throws DataAccessException {
		int id = copy.getId();
		
		try {
			changeStateOnCopy.setString(1, state);
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
		LocalTime fromHour;
		LocalTime toHour;
		int id;
		
		try {
			fromHour = rs.getTime("FromHour").toLocalTime();
			toHour = rs.getTime("ToHour").toLocalTime();
			id = rs.getInt("ID");
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
		Copy copy;
		
		try {
			while(rs.next()) {
				copy = buildCopyObject(rs);
				shiftCopies.add(copy);
			} 
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shiftCopies;

	}
	

	private Copy buildCopyObject(ResultSet rs) throws DataAccessException {
		ResultSet rs2;
		Shift shift = null;
		int id;
		int shiftID;
		
		Copy copy = null;
		byte[] version;
		java.sql.Date date;
		LocalDate localDate;
		String state;
		Date releasedAtTimestamp;
		LocalDateTime releasedAt;
		
		try {
			id = rs.getInt("ID");
			shiftID = rs.getInt("ShiftID");
			findShiftsOnShiftID.setInt(1, shiftID);
			rs2 = findShiftsOnShiftID.executeQuery();
			if(rs2.next()) {
				shift = buildShiftObject(rs2);
			}
			
			version = rs.getBytes("VersionNumber");
			date = rs.getDate("Date");
			localDate = date.toLocalDate();
			state = rs.getString("State");
			releasedAtTimestamp = rs.getTimestamp("ReleasedAt");
			releasedAt = releasedAtTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			copy = new Copy(id, shift, null, version, localDate, state, releasedAt); // TODO ny constructor
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return copy;
	}

}
