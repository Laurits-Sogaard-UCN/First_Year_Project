package database;

import java.sql.Connection;

import java.sql.SQLException;

import utility.DataAccessException;

public abstract class DBConnection {
	
	protected Connection connection = null;

	/**
	 * Returns the connection to the database.
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Starts transaction by setting AutoCommit to false.
	 * @throws DataAccessException
	 */
	public void startTransaction() throws DataAccessException {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new DataAccessException("Could not start transaction.", e);
		}
	}

	/**
	 * Commits transaction and sets AutoCommit to true.
	 * @throws DataAccessException
	 */
	public void commitTransaction() throws DataAccessException {
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw e;
				// e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not commit transaction", e);
		}
	}

	/**
	 * Rolls back transaction and sets AutoCommit to true.
	 * @throws DataAccessException
	 */
	public void rollbackTransaction() throws DataAccessException {
		try {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw e;
				// e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not rollback transaction", e);
		}
	}

	/**
	 * Closes the connection to the database.
	 */
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


