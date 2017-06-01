package metier.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection_DB {
	
	private static String host = "192.168.137.1:5432";
	private static String base = "ceparou";
	private static String user = "postgres";
	private static String password = "010395";
	private static String url = "jdbc:postgresql://" + host + "/" + base;

	/**
	 * Singleton instance.
	 */
	private static Connection connection;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {		
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection(url, user, password);
		if (connection != null) {
			System.out.println("Connexion établie ! :)");
			return connection;
		}
		System.out.println("Connexion non établie ! :(");
		return null;
	}

}
