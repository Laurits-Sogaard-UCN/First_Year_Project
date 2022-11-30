package database;

import utility.DataAccessException;
import utility.DatabaseType;

public class ConnectionFactory {

	public static DBConnection createDatabase(DatabaseType databaseType) throws DataAccessException {
		DBConnection newConnection = null;
		
		if(databaseType.equals(DatabaseType.REALDATABASE)) {
			newConnection = DBConnectionSystem.getInstance();
		}
		else {
			newConnection = DBConnectionMock.getInstance();
		}
		
		return newConnection;
	}
}
