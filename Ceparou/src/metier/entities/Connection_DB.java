package metier.entities;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connection_DB {
	
	private static String host = "localhost:5432";
	private static String base = "test";
	private static String user = "postgres";
	private static String password = "010395";
	private static String url = "jdbc:postgresql://" + host + "/" + base;

	/**
	 * Singleton instance.
	 */
	private static Connection connection;

	public static Connection getConnection() {		
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				System.err.println("Connection failed : " + e.getMessage());			
			}
		}
		return connection;
	}

}
