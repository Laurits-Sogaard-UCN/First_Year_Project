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
import model.Shift;
import model.ShiftCopy;
import model.WorkSchedule;
import utility.CopyState;
import utility.DBMessages;
import utility.DataAccessException;
import utility.DatabaseType;;

public class ShiftDB implements ShiftDBIF {
	
	private static final String FIND_SHIFT_ON_FROM_AND_TO = ("SELECT *\r\n"
			+ "FROM Shift s\r\n"
			+ "WHERE s.FromHour = ?\r\n"
			+ "and s.ToHour = ?");
	private PreparedStatement findShiftOnFromAndTo;
	
	private static final String INSERT_SHIFT_COPY = ("INSERT INTO Copy(ShiftID, Date, State, ReleasedAt)\r\n"
			+ "VALUES (?, ?, ?, ?)");
	private PreparedStatement insertShiftCopy;
	
	private static final String CHECK_REST_PERIOD = ("SELECT s.FromHour, s.ToHour, c.Date\r\n"
			+ "FROM Shift s, Copy c\r\n"
			+ "WHERE c.WorkScheduleID = ?\r\n"
			+ "and c.ShiftID = s.ID");
	private PreparedStatement checkRestPeriod;
	
	private static final String CHANGE_STATE_ON_COPY = ("UPDATE Copy\r\n"
			+ "SET State = ?\r\n"
			+ "WHERE ID = ?");
	private PreparedStatement changeStateOnCopy;

	private static final String FIND_SHIFT_COPIES_ON_STATE = ("SELECT *\r\n"
			+ "FROM Copy c\r\n"
			+ "WHERE c.State = ?");
	private PreparedStatement findShiftCopiesOnState;
	
	private static final String FIND_SHIFTS_ON_SHIFT_ID = ("SELECT *\r\n"
			+ "FROM Shift s, Copy c\r\n"
			+ "WHERE s.ID = ?");
	private PreparedStatement findShiftsOnShiftID;
	
	private static final String SET_WORK_SCHEDULE_ID_ON_COPY = ("UPDATE Copy\r\n"
			+ "SET WorkScheduleID = ?\r\n"
			+ "WHERE ID = ?");
	private PreparedStatement setWorkScheduleIDOnCopy;
	
	private static final String FIND_COPY_STATE_ON_ID = "SELECT c.State\r\n"
			+ "FROM Copy c\r\n"
			+ "WHERE c.ID = ?";
	private PreparedStatement findCopyStateOnID;
	
	private Connection con;
	private DBConnection dbConnection;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException 
	 */
	public ShiftDB(DatabaseType databaseType) throws DataAccessException {
		init(databaseType);
	}
	
	/**
	 * Initialization of Connection and PreparedStatements.
	 * @throws DataAccessException
	 */
	private void init(DatabaseType databaseType) throws DataAccessException {
		dbConnection = ConnectionFactory.createDatabase(databaseType);
		con = dbConnection.getConnection();
		
		try {
			findShiftOnFromAndTo = con.prepareStatement(FIND_SHIFT_ON_FROM_AND_TO);
			insertShiftCopy = con.prepareStatement(INSERT_SHIFT_COPY);
			checkRestPeriod = con.prepareStatement(CHECK_REST_PERIOD);
			changeStateOnCopy = con.prepareStatement(CHANGE_STATE_ON_COPY);
			findShiftCopiesOnState = con.prepareStatement(FIND_SHIFT_COPIES_ON_STATE);
			findShiftsOnShiftID = con.prepareStatement(FIND_SHIFTS_ON_SHIFT_ID);
			setWorkScheduleIDOnCopy = con.prepareStatement(SET_WORK_SCHEDULE_ID_ON_COPY);
			findCopyStateOnID = con.prepareStatement(FIND_COPY_STATE_ON_ID);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	/**
	 * Sets state and work schedule ID on a copy in the database. 
	 * @param copy
	 * @param workScheduleID
	 * @param state
	 * @return taken
	 * @throws DataAccessException
	 */
	public boolean takeShift(ShiftCopy shiftCopy, int workScheduleID, String state) throws DataAccessException {
		boolean taken = false;
		boolean sufficientRest = checkRestPeriod(shiftCopy, workScheduleID);
		int copyID = shiftCopy.getID();
		boolean equalsReleased = false;
		boolean equalsTradeable = false;
		ResultSet rs = null;
		
		if(sufficientRest) {
			try {
				// TODO: måske vi skal være helt sikre på isolationsniveauerne?
				con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); 	// Sets isolation level on transaction.
				dbConnection.startTransaction();
				findCopyStateOnID.setInt(1, copyID);
				rs = findCopyStateOnID.executeQuery();
				rs.next();
				// TODO Skal vi tjekke at det virker?
				equalsReleased = rs.getString("State").equals(CopyState.RELEASED.getState());
				equalsTradeable = rs.getString("State").equals(CopyState.TRADEABLE.getState());
				
				if(equalsReleased || equalsTradeable) { 	// Checks if State is 'Released' or 'Tradeable'.
					setState(shiftCopy, state);
					setWorkScheduleIDOnCopy(shiftCopy, workScheduleID);
				}
				
				dbConnection.commitTransaction();
				taken = true;
				
			} catch(Exception e) {
				dbConnection.rollbackTransaction();
				throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
			}
		}
		return taken;
	}
	
	/**
	 * Checks if a given copy violates the business rules of 11 hour rest period, 
	 * and a maximum of one shift a day, for a given work schedule.
	 * @param copy
	 * @param workScheduleID
	 * @return sufficientRest
	 * @throws DataAccessException
	 */
	private boolean checkRestPeriod(ShiftCopy shiftCopy, int workScheduleID) throws DataAccessException {
		boolean sufficientRest = false;
		ResultSet rs;
		
		LocalDate date;
		LocalTime fromHour;
		String resultSetFromHour;
		LocalTime toHour;
		String resultSetToHour;
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
			
			/* Loops through all rows in ResultSet, until last row has been reached.*/
			
			if(rs.next()) {
				do {
					/* Converts values from ResultSet into LocalDateTime object.*/
					date = rs.getDate("Date").toLocalDate();
					resultSetFromHour = rs.getString("FromHour");
					fromHour = LocalTime.parse(resultSetFromHour);
					resultSetToHour = rs.getString("ToHour");
					toHour = LocalTime.parse(resultSetToHour);
					dateTimeFrom = LocalDateTime.of(date, fromHour);
					dateTimeTo = LocalDateTime.of(date, toHour);
					
					/* Converts values from copy object into LocalDateTime object.*/
					copyDate = shiftCopy.getDate();
					copyFromHour = shiftCopy.getShift().getFromHour();
					copyToHour = shiftCopy.getShift().getToHour();
					copyDateTimeFrom = LocalDateTime.of(copyDate, copyFromHour);
					copyDateTimeTo = LocalDateTime.of(copyDate, copyToHour);
					
					/* Finds duration between LocalDateTimeObjects.*/
					durationBetween1 = Duration.between(dateTimeFrom, copyDateTimeTo).toHours();
					if(durationBetween1 < 0) {
						durationBetween1 = durationBetween1 * -1;
					}
					durationBetween2 = Duration.between(dateTimeTo, copyDateTimeFrom).toHours();
					if(durationBetween2 < 0) {
						durationBetween2 = durationBetween2 * -1;
					}
					
					if(!date.isEqual(copyDate) && durationBetween1 >= 11 && durationBetween2 >= 11) { 	// Checks if dates are not equal, and 11 hours has passed.
						sufficientRest = true;
					}
					else {
						sufficientRest = false;
						break;
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
	
	/**
	 * Sets new state on a copy in database by executing update.
	 * @param copy
	 * @param state
	 * @throws DataAccessException
	 */
	private void setState(ShiftCopy shiftCopy, String state) throws DataAccessException {
		int id = shiftCopy.getID();
		
		try {
			changeStateOnCopy.setString(1, state);
			changeStateOnCopy.setInt(2, id);
			changeStateOnCopy.executeUpdate();
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		
	}
	
	/**
	 * Sets a new work schedule ID on copy by executing update.
	 * @param copy
	 * @param workScheduleID
	 * @throws DataAccessException
	 */
	private void setWorkScheduleIDOnCopy(ShiftCopy shiftCopy, int workScheduleID) throws DataAccessException {
		int copyID = shiftCopy.getID();
		
		try {
			setWorkScheduleIDOnCopy.setInt(1, workScheduleID);
			setWorkScheduleIDOnCopy.setInt(2, copyID);
			setWorkScheduleIDOnCopy.executeUpdate();
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		
	}
	
	/**
	 * Finds a shift by executing query and building shift object. 
	 * @param fromHour
	 * @param toHour
	 * @return shift
	 * @throws DataAccessException
	 */
	public Shift findShiftOnFromAndTo(LocalTime fromHour, LocalTime toHour) throws DataAccessException {
		ResultSet rs;
		Shift shift = null;
		String from;
		String to;
		
		try {
			/* Truncates LocalTime objects to seconds, and parses them to strings.*/
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
	
	/**
	 * Inserts a list of shift copies to database by executing update. 
	 * @param shiftCopies
	 * @return completed
	 * @throws DataAccessException
	 */
	public boolean completeReleaseNewShifts(ArrayList<ShiftCopy> shiftCopies) throws DataAccessException {
		boolean completed = false;
		int rowsAffected = -1;
		Shift shift;
		int id;
		LocalDate localDate;
		java.sql.Date date;
		Timestamp timestamp;
		
		try {
			dbConnection.startTransaction();
			
			/* Loops through list of copies and inserts one by one to the database.*/
			
			for(ShiftCopy element : shiftCopies) {
				shift = element.getShift();
				id = shift.getID();
				localDate = element.getDate();
				date = java.sql.Date.valueOf(localDate); 	// Converts LocalDate object to SQL Date object.
				timestamp = Timestamp.valueOf(LocalDateTime.now()); 	// Converts LocalDateTime object to Timestamp object.
				
				insertShiftCopy.setInt(1, id);
				insertShiftCopy.setDate(2, date);
				insertShiftCopy.setString(3, CopyState.RELEASED.getState());
				insertShiftCopy.setTimestamp(4, timestamp);
				rowsAffected += insertShiftCopy.executeUpdate();
			}
			dbConnection.commitTransaction();
			
		} catch(SQLException e) {
			dbConnection.rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_INSERT, e);
		}
		if(rowsAffected >= 0) {
			completed = true;
		}
		return completed;
	}
	
	/**
	 * Finds all copies in database marked as 'Released' by executing query.
	 * @return releasedShiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<ShiftCopy> findShiftCopiesOnState(String state) throws DataAccessException {
		ArrayList<ShiftCopy> releasedShiftCopies;
		ResultSet rs = null;
		
		try {
			findShiftCopiesOnState.setString(1, state);
			rs = findShiftCopiesOnState.executeQuery();
			releasedShiftCopies = buildShiftCopyObjects(rs);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return releasedShiftCopies;
	}
	
	/**
	 * Builds shift object from ResultSet.
	 * @param rs
	 * @return shift
	 * @throws DataAccessException
	 */
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
	
	/**
	 * Builds a collection of copies by building one or more copy objects.
	 * @param rs
	 * @return shiftCopies
	 * @throws DataAccessException
	 */
	private ArrayList<ShiftCopy> buildShiftCopyObjects(ResultSet rs) throws DataAccessException {
		ArrayList<ShiftCopy> shiftCopies = new ArrayList<>();
		ShiftCopy shiftCopy;
		
		try {
			while(rs.next()) {
				shiftCopy = buildShiftCopyObject(rs);
				shiftCopies.add(shiftCopy);
			} 
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shiftCopies;

	}
	
	/**
	 * Builds a full copy object, including a shift object, from ResultSet.
	 * @param rs
	 * @return copy
	 * @throws DataAccessException
	 */
	private ShiftCopy buildShiftCopyObject(ResultSet rs) throws DataAccessException {
		ResultSet rs2;
		Shift shift = null;
		int id;
		int shiftID;
		WorkSchedule workschedule;
		int workScheduleID;
		
		ShiftCopy shiftCopy = null;
		java.sql.Date date;
		LocalDate localDate;
		String state;
		Date releasedAtTimestamp;
		LocalDateTime releasedAt;
		
		try {
			/* Builds shift object.*/
			id = rs.getInt("ID");
			shiftID = rs.getInt("ShiftID");
			findShiftsOnShiftID.setInt(1, shiftID);
			rs2 = findShiftsOnShiftID.executeQuery();
			if(rs2.next()) {
				shift = buildShiftObject(rs2);
			}
			
			/* Creates full copy object.*/
			date = rs.getDate("Date");
			localDate = date.toLocalDate();
			state = rs.getString("State");
			releasedAtTimestamp = rs.getTimestamp("ReleasedAt");
			releasedAt = releasedAtTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			workScheduleID = rs.getInt("WorkScheduleID");
			workschedule = new WorkSchedule(workScheduleID);
			shiftCopy = shift.createFullCopy(id, shift, workschedule, localDate, state, releasedAt);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return shiftCopy;
	}
}
