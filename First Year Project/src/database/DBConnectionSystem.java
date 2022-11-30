package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import utility.DataAccessException;

public class DBConnectionSystem extends DBConnection {


		private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		private static final String dbName = "DMA-CSD-V222_10434664";
		private static final String serverAddress = "hildur.ucn.dk";
		private static final int serverPort = 1433;
		private static final String userName = "DMA-CSD-V222_10434664";
		private static final String password = "Password1!";
		private static DBConnectionSystem dbConnectionSystem;

		/**
		 * Constructor to create database access.
		 * @throws DataAccessException
		 */
		private DBConnectionSystem() throws DataAccessException {
			String connectionString = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;encrypt=false",
					serverAddress, serverPort, dbName, userName, password);
			
			try {
				Class.forName(driverClass);
				connection = DriverManager.getConnection(connectionString);
			} catch (ClassNotFoundException e) {
				throw new DataAccessException("Missing JDBC driver", e);

			} catch (SQLException e) {
				throw new DataAccessException(String.format("Could not connect to database %s@%s:%d user %s", dbName,
						serverAddress, serverPort, userName), e);
			}
		}

		/**
		 * Returns instance of DBConnection. If it does not already exist, it is created. 
		 * Implemented as a Singleton pattern.
		 * @return DBConnection
		 * @throws DataAccessException
		 */
		public static synchronized DBConnection getInstance() throws DataAccessException {
			if (dbConnectionSystem == null) {
				dbConnectionSystem = new DBConnectionSystem();
			}
			return dbConnectionSystem;
		}
}

