package wholesalebooks;

import cmp.DataSource;
import cmp.DataSourceException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A utility class used for interaction with the SQL database.
 *
 * @author Joshua Barnett
 */
public class Database {

	protected static Connection connection;
	protected static Statement statement;

	/**
	 * Connects to the database specified in DataSource.
	 *
	 * @return True if it connected successfully, false otherwise.
	 */
	public static boolean connect() {

		try {

			// Getting the login details from the Datasource 
			String userName = DataSource.getUserName();
			String password = DataSource.getPassword();
			String className = DataSource.getClassName();
			String url = DataSource.getURL() + userName;
			Class.forName(className);

			System.out.printf("Database Driver: %s\n", className);
			System.out.printf("Database URL:    %s\n", url);
			System.out.println("Connecting...");

			try {

				connection = DriverManager.getConnection(url, userName, password);
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				System.out.println("Connection Successful!");
				return true;

			} catch (SQLException ex) {

				System.out.println("Connection Failed!");
				return false;

			}

		} catch (DataSourceException | ClassNotFoundException ex) {

			System.out.println("Data Source Invalid!");
			return false;

		}

	}

	/**
	 * Execute a SQL file on the connected database.
	 *
	 * @param filePath The path of the SQL file.
	 * @return True if executed successfully, false otherwise.
	 */
	public static boolean executeSQLFile(String filePath) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			String sql = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
			return execute(sql);
		} catch (IOException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return false;
		}
	}

	/**
	 * Execute a raw SQL commands, used for deletion and updating.
	 *
	 * @param sql The query to execute.
	 * @return True if executed successfully, false otherwise.
	 */
	public static boolean execute(String sql) {
		try {
			statement.execute(sql);
			return true;
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return false;
		}
	}

	/**
	 * Execute a SQL query, and get the returned results.
	 *
	 * @param sql The query to execute.
	 * @return True if executed successfully, false otherwise.
	 */
	public static ResultSet executeQuery(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return null;
		}
	}

	/**
	 * Disconnect from the SQL database.
	 */
	public static void disconnect() {

		try {
			connection.close();
			statement.close();
			System.out.println("Connection Ended!");
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}

	}

}
