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
import types.Category;

public class Database {

	protected static Connection connection;
	protected static Statement statement;

	public static boolean connect() {

		try {

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

	public static boolean executeSQLFile(String filePath) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			String sql = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
			return execute(sql);
		} catch (IOException ex) {
			if (ex.getMessage() != null)
				System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static boolean execute(String sql) {
		try {
			statement.execute(sql);
			return true;
		} catch (SQLException ex) {
			if (ex.getMessage() != null)
				System.out.println(ex.getMessage());
			return false;
		}
	}

	public static ResultSet executeQuery(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException ex) {
			if (ex.getMessage() != null)
				System.out.println(ex.getMessage());
			return null;
		}
	}

	public static void disconnect() {

		try {
			connection.close();
			statement.close();
			System.out.println("Connection Ended!");
		} catch (SQLException ex) {
			if (ex.getMessage() != null)
				System.out.println(ex.getMessage());
		}

	}

}
