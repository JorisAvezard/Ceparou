package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;

/**
 * 
 * This class creates the link with the database and contains the methods for the
 * sql queries
 * 
 */
public class JdbcPersistence {
	
	private static String host = "localhost:5432";
	private static String base = "geobase";
	private static String user = "postgres";
	private static String password = "le_nouveau_mot_de_passe";
	private static String url = "jdbc:postgresql://" + host + "/" + base;

	/**
	 * Lazy singleton instance.
	 */
	private Connection connection;
	
	public JdbcPersistence() {
		prepareConnection();
	}

	private void prepareConnection() {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				System.err.println("Connection failed : " + e.getMessage());
			}
			//System.out.println("Opened database successfully");
		}
	}
	
	/**
	 * This method is a test It returns the number of users in the
	 * database
	 */
	//@Override
	public int countUsers() {
		int count = 0;
		try {
			//Initialisation of query
			String selectCountQuery = "SELECT count(*) AS co FROM android.users";

			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);

			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("co");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		//System.out.println(count);
		return count;
	}
	
	public int countSpecifyUser(String userEntry) {
		int count = 0;
		try {
			//Initialisation of query
			String selectCountQuery = "SELECT count(*) AS co FROM android.users WHERE pseudo='"+userEntry+"'";

			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);

			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("co");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		return count;
	}
	
	public int existUser(String userEntry){
		int count= countSpecifyUser(userEntry);
		int result=0;
		
		if(count==0){
			System.out.println("This user doesn't exist");
		}
		else {
			System.out.println("Success");
			result=1;
		}
		
		return result;
	}
	
	public void readPlaces(int userid) {
		List<String> places = new ArrayList<String>();
		try {
			//Initialisation of query
			String selectQuery = "SELECT DISTINCT pl.name_place AS p "
					+ "FROM android.places AS pl, android.buildings AS b, android.paths AS pa"
					+ " WHERE pa.user_id='"+ userid +"' AND b.id_building=pa.building_id AND b.id_building=pl.building_id"
					+ " AND ST_Within(pa.coordinates, pl.area)='true';";

			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {
				String element = null;
				element=result.getString("p");
				places.add(element);
				// System.out.println(readSite.toString());
			}

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		for(int i=0; i<places.size();i++){
			System.out.println(places.get(i));
		}
	}
	
	/**
	 * Compte le nombre total de chemins stockés dans la base de données
	 * @return
	 */
	public int countTotalPath() {
		int count = 0;
		try {
			//Initialisation of query
			String selectCountQuery = "SELECT p.id_path AS last FROM android.paths2 AS p ORDER BY id_path DESC LIMIT 1";

			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("last");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		return count;
	}
	
	
	
	public int longPath() {
		int count = 0;
		try {
			//Initialisation of query
			String selectCountQuery = "select max(mycount)As longPath from (SELECT count (id_coord) as mycount" +
					" FROM  android.paths2 AS pa group by pa.id_path )As longPath";

			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("longPath");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		return count;
	}
	
	//renvoie les chemins 
	public ArrayList<String> getPath(int idPath) {
		ArrayList<String> path = new ArrayList<String>();
		
		try {
			// Initialisation of query
			String query = "SELECT DISTINCT trim(pl.name_place)AS name "
					+ "FROM android.places AS pl, android.buildings AS b, android.paths2 AS pa "
					+ "WHERE pa.id_path='"+ idPath +"' And b.id_building=pa.building_id AND b.id_building=pl.building_id "
						+ "AND ST_Within(pa.coordinates, pl.area)='true';";
	
			PreparedStatement preparedStatement = connection.prepareStatement(query);
	
			ResultSet result = preparedStatement.executeQuery();
	
			while (result.next()) {
	
			String place = result.getString("name");
			path.add(place);
		}
	
		preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		
//		for(int i=0;i<path.size();i++){
//			System.out.println(path.get(i));
//		}

		return path;
	}
	
	//renvoie les places visitées 
	
	public ArrayList<String> getPlace() {
		ArrayList<String> places = new ArrayList<String>();
		
		try {
			// Initialisation of query
			String query = "SELECT DISTINCT trim(pl.name_place) AS name "
					+ "FROM android.places AS pl, android.buildings AS b, android.paths2 AS pa "
					+ "WHERE  b.id_building=pa.building_id AND b.id_building=pl.building_id "
						+ "AND ST_Within(pa.coordinates, pl.area)='true';";
	
			PreparedStatement preparedStatement = connection.prepareStatement(query);
	
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
	
			String place = result.getString("name");
			places.trimToSize();
			places.add(place);
			
		}
	
		preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		
//		for(int i=0;i<path.size();i++){
//			System.out.println(path.get(i));
//		}

		return places;
	}
	
	
	
	public void truncateRules(){
		try {
 
			String truncateQuery = "TRUNCATE TABLE android.rules";
 
			PreparedStatement preparedStatement = connection.prepareStatement(truncateQuery);
 
			preparedStatement.executeUpdate();
 
			preparedStatement.close();
			
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
	}
 

	
	public void addRule(ArrayList<ArrayList<String>> premise, ArrayList<ArrayList<String>> consequence) {
		int id = 1;
		
		truncateRules();
	
		for(int i=0;i<premise.size();i++){
			try {
				String insertRuleQuery = "INSERT INTO android.rules (id_rule, premise, consequence, confidence) VALUES "
						+ "(?, ?, ?, ?)";
 
				PreparedStatement preparedStatement = connection.prepareStatement(insertRuleQuery);
 
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, premise.get(i).get(1));
				preparedStatement.setString(3, consequence.get(i).get(1));
				preparedStatement.setFloat(4, Float.valueOf(premise.get(i).get(2)));
 
				preparedStatement.executeUpdate();
 
				preparedStatement.close();
				
				id++;
			} catch (SQLException se) {
				System.err.println(se.getMessage());
			}
		}
		
		System.out.println("Success insert");
		
	}
 

	
	public void addstep(int id_path_entry, int id_coord_entry, double x, double y) {

			try {
//				String insertRuleQuery = "INSERT INTO android.paths2 " +
//						"(id_path, id_coord, coordinates, date_time, user_id, building_id) VALUES "
//						+ "(?, ?, ?, ?, ?, ?)";
				
				String insertRuleQuery = "INSERT INTO android.paths2 " +
						"(id_path, id_coord, coordinates, date_time, user_id, building_id) VALUES "
						+ "("+id_path_entry+", "+  id_coord_entry + ", 'POINT("+x+" "+y+")', '2017-01-08 08:50:00', 1, 5)";
 
				PreparedStatement preparedStatement = connection.prepareStatement(insertRuleQuery);
 
//				preparedStatement.setInt(1, id_path_entry);
//				preparedStatement.setInt(2, id_coord_entry);
//				preparedStatement.setDouble(3, x);
//				preparedStatement.setFloat(4, Float.valueOf(premise.get(i).get(2)));
 
				preparedStatement.executeUpdate();
 
				preparedStatement.close();
				
			} catch (SQLException se) {
				System.err.println(se.getMessage());
			}
		
		System.out.println("Success insert");
		
	}
 

 
 /*---------------------------------------------------------------------------------------------------------*/
	
	public ArrayList<String> getPremise(int id_result) {
		ArrayList<String> path = new ArrayList<String>();
		
		try {
			// Initialisation of query
			String query = "SELECT trim(place)AS place from android.results2 " +
					 "WHERE id_result='"+ id_result +"';";
	
			PreparedStatement preparedStatement = connection.prepareStatement(query);
	
			ResultSet result = preparedStatement.executeQuery();
	
			while (result.next()) {
	
			String place = result.getString("place");
			path.add(place);
		}
	
		preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		
//		for(int i=0;i<path.size();i++){
//			System.out.println(path.get(i));
//		}

		return path;
	}
	
	
	public int countTotalPremise() {
		int count = 0;
		try {
			//Initialisation of query
			String selectCountQuery = "SELECT re.id_result AS last FROM android.results2 AS re ORDER BY id_result DESC LIMIT 1";

			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("last");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}

		return count;
	}

	public String getResult(int id_result){
		
			String results = null;
			
			try {
				// Initialisation of query
				String query = "SELECT trim(result) as res from android.results2 " +
						 "WHERE result is not null and  id_result='"+ id_result +"';";
		
				PreparedStatement preparedStatement = connection.prepareStatement(query);
		
				ResultSet result = preparedStatement.executeQuery();
		
				result.next();
		
				results = result.getString("res");
				
			
		
			preparedStatement.close();

			} catch (SQLException se) {
				System.err.println(se.getMessage());
			}
			
//			for(int i=0;i<path.size();i++){
//				System.out.println(path.get(i));
//			}

			return results;
		}
	
	
	public int getIdPathUser(int user_id){
		
		int results = 0;
		
		try {
			// Initialisation of query
			String query = "SELECT max(id_path) as path from android.paths2 " +
					 "WHERE  user_id='"+ user_id +"';";
	
			PreparedStatement preparedStatement = connection.prepareStatement(query);
	
			ResultSet result = preparedStatement.executeQuery();
	
			result.next();
	
			results = result.getInt("path");
		preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		
//		for(int i=0;i<path.size();i++){
//			System.out.println(path.get(i));
//		}

		return results;
	}
	
	
	public void truncateRules2(){
		try {
 
			String truncateQuery = "TRUNCATE TABLE android.results2";
 
			PreparedStatement preparedStatement = connection.prepareStatement(truncateQuery);
 
			preparedStatement.executeUpdate();
 
			preparedStatement.close();
			
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	
	
	/**
	 * Ajouter des règles dans la table android.rules
	 * @param premise
	 * @param consequence
	 * 
	 */
	public void addRuleV2(ArrayList<ArrayList<String>> r_entry) {
		truncateRules2();
		String tmp = "1";
		
		try {
		
			String insertRuleQuery = "INSERT INTO android.results2 (id_result, id_step, place, result) VALUES "
					+ "(?, ?, ?, ?)";
			

			PreparedStatement preparedStatement = connection.prepareStatement(insertRuleQuery);

			preparedStatement.setInt(1, Integer.valueOf(r_entry.get(0).get(0)));
			preparedStatement.setInt(2, Integer.valueOf(r_entry.get(0).get(1)));
			preparedStatement.setString(3, r_entry.get(0).get(2));
			preparedStatement.setString(4, r_entry.get(0).get(4));

			preparedStatement.executeUpdate();

			preparedStatement.close();
	} catch (SQLException se) {
		System.err.println(se.getMessage());
	}
		for(int i=1;i<r_entry.size();i++){
			try {
				if(!(r_entry.get(i).get(0).equals(tmp))){
					
				
				String insertRuleQuery2 = "INSERT INTO android.results2 (id_result, id_step, place, result) VALUES "
						+ "(?, ?, ?, ?)";
				

				PreparedStatement preparedStatement2 = connection.prepareStatement(insertRuleQuery2);

				preparedStatement2.setInt(1, Integer.valueOf(r_entry.get(i).get(0)));
				preparedStatement2.setInt(2, Integer.valueOf(r_entry.get(i).get(1)));
				preparedStatement2.setString(3, r_entry.get(i).get(2));
				preparedStatement2.setString(4, r_entry.get(i).get(4));

				preparedStatement2.executeUpdate();

				preparedStatement2.close();
				
				tmp = r_entry.get(i).get(0);
				}
				else{
					String insertRuleQuery2 = "INSERT INTO android.results2 (id_result, id_step, place) VALUES "
							+ "(?, ?, ?)";
					

					PreparedStatement preparedStatement2 = connection.prepareStatement(insertRuleQuery2);

					preparedStatement2.setInt(1, Integer.valueOf(r_entry.get(i).get(0)));
					preparedStatement2.setInt(2, Integer.valueOf(r_entry.get(i).get(1)));
					preparedStatement2.setString(3, r_entry.get(i).get(2));
					preparedStatement2.executeUpdate();

					preparedStatement2.close();
				}
			} catch (SQLException se) {
				System.err.println(se.getMessage());
			}
		}
		
		System.out.println("Success insert");
		
	}

	
}
