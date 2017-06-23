package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ia.Engine;
import metier.entities.Connection_DB;

import java.sql.Statement;

/**
 * 
 * This class creates the link with the database and contains the methods for
 * the sql queries
 * 
 */
public class JdbcPersistence {

	/**
	 * Compte le nombre total de chemins stockés dans la base de données
	 * 
	 * @return
	 */
	public int countTotalPath() {
		int count = 0;
		try {
			// Initialisation of query
			String selectCountQuery = "SELECT p.id_path AS last FROM android.paths AS p ORDER BY id_path DESC LIMIT 1";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("last");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public int longPath() {
		int count = 0;
		try {
			// Initialisation of query
			String selectCountQuery = "SELECT max(mycount)As longPath from (SELECT count(pa.id_coord) as mycount"
					+ " FROM  android.paths AS pa group by pa.id_path )As longPath";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("longPath");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	// renvoie les chemins
	public ArrayList<String> getPath(int idPath) {
		ArrayList<String> path = new ArrayList<String>();

		try {
			// Initialisation of query
			String query = "SELECT DISTINCT trim(pl.name_place)AS name "
					+ "FROM android.places AS pl, android.buildings AS b, android.paths AS pa "
					+ "WHERE pa.id_path='"
					+ idPath
					+ "' And b.id_building=pa.building_id AND b.id_building=pl.building_id "
					+ "AND ST_Within(pa.coordinates, pl.area)='true';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {

				String place = result.getString("name");
				path.add(place);
			}

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for(int i=0;i<path.size();i++){
		// System.out.println(path.get(i));
		// }

		return path;
	}

	// renvoie les places visitées

	public ArrayList<String> getPlace() {
		ArrayList<String> places = new ArrayList<String>();

		try {
			// Initialisation of query
			String query = "SELECT DISTINCT trim(pl.name_place) AS name "
					+ "FROM android.places AS pl, android.buildings AS b, android.paths AS pa "
					+ "WHERE  b.id_building=pa.building_id AND b.id_building=pl.building_id "
					+ "AND ST_Within(pa.coordinates, pl.area)='true';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {

				String place = result.getString("name");
				places.trimToSize();
				places.add(place);

			}

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for(int i=0;i<path.size();i++){
		// System.out.println(path.get(i));
		// }

		return places;
	}

	public void truncateRules() {
		try {

			String truncateQuery = "TRUNCATE TABLE android.rules";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(truncateQuery);

			preparedStatement.executeUpdate();

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addRule(ArrayList<ArrayList<String>> premise,
			ArrayList<ArrayList<String>> consequence) {
		int id = 1;

		truncateRules();

		for (int i = 0; i < premise.size(); i++) {
			try {
				String insertRuleQuery = "INSERT INTO android.rules (id_rule, premise, consequence, confidence) VALUES "
						+ "(?, ?, ?, ?)";

				Connection connection = Connection_DB.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(insertRuleQuery);

				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, premise.get(i).get(1));
				preparedStatement.setString(3, consequence.get(i).get(1));
				preparedStatement.setFloat(4,
						Float.valueOf(premise.get(i).get(2)));

				preparedStatement.executeUpdate();

				preparedStatement.close();

				id++;
			} catch (SQLException se) {
				System.err.println(se.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Success insert");

	}

	public void addstep(int id_path_entry, int id_coord_entry, double x,
			double y) {

		try {
			// String insertRuleQuery = "INSERT INTO android.paths " +
			// "(id_path, id_coord, coordinates, date_time, user_id, building_id) VALUES "
			// + "(?, ?, ?, ?, ?, ?)";

			String insertRuleQuery = "INSERT INTO android.paths "
					+ "(id_path, id_coord, coordinates, date_time, user_id, building_id) VALUES "
					+ "(" + id_path_entry + ", " + id_coord_entry + ", 'POINT("
					+ x + " " + y + ")', NOW(), 1, 1)";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(insertRuleQuery);

			preparedStatement.executeUpdate();

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Success insert");

	}

	public ArrayList<String> getPremise(int id_rules) {
		ArrayList<String> path = new ArrayList<String>();

		try {
			// Initialisation of query
			String query = "SELECT trim(premises)AS place from android.rules "
					+ "WHERE id_rule='" + id_rules + "';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {

				String place = result.getString("place");
				path.add(place);
			}

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
	}

	public int countTotalRules() {
		int count = 0;
		try {
			// Initialisation of query
			String selectCountQuery = "SELECT re.id_rule AS last FROM android.rules AS re ORDER BY re.id_rule DESC LIMIT 1";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectCountQuery);
			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("last");

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public String getResult(int id_rules) {

		String results = null;

		try {
			// Initialisation of query
			String query = "SELECT trim(ru.consequence) as res from android.rules as ru "
					+ "WHERE ru.consequence is not null and  id_rule='"
					+ id_rules + "';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			result.next();

			results = result.getString("res");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for(int i=0;i<path.size();i++){
		// System.out.println(path.get(i));
		// }

		return results;
	}

	public int getIdPathUser(int user_id) {

		int results = 0;

		try {
			// Initialisation of query
			String query = "SELECT max(id_path) as path from android.paths "
					+ "WHERE  user_id='" + user_id + "';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			result.next();

			results = result.getInt("path");
			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Ajouter des règles dans la table android.rules2
	 * 
	 * @param premise
	 * @param consequence
	 * 
	 */
	public void addRuleV2(ArrayList<ArrayList<String>> r_entry) {
		truncateRules();
		String tmp = "1";

		try {

			String insertRuleQuery = "INSERT INTO android.rules (id_rule, id_step, premises, consequence, confidence) VALUES "
					+ "(?, ?, ?, ?, ?)";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(insertRuleQuery);

			preparedStatement.setInt(1, Integer.valueOf(r_entry.get(0).get(0)));
			preparedStatement.setInt(2, Integer.valueOf(r_entry.get(0).get(1)));
			preparedStatement.setString(3, r_entry.get(0).get(2));
			preparedStatement.setString(4, r_entry.get(0).get(4));
			preparedStatement.setDouble(5,
					Double.valueOf(r_entry.get(0).get(5)));

			preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i < r_entry.size(); i++) {
			try {
				if (!(r_entry.get(i).get(0).equals(tmp))) {

					String insertRuleQuery2 = "INSERT INTO android.rules (id_rule, id_step, premises, consequence, confidence) VALUES "
							+ "(?, ?, ?, ?, ?)";

					Connection connection = Connection_DB.getConnection();
					PreparedStatement preparedStatement2 = connection
							.prepareStatement(insertRuleQuery2);

					preparedStatement2.setInt(1,
							Integer.valueOf(r_entry.get(i).get(0)));
					preparedStatement2.setInt(2,
							Integer.valueOf(r_entry.get(i).get(1)));
					preparedStatement2.setString(3, r_entry.get(i).get(2));
					preparedStatement2.setString(4, r_entry.get(i).get(4));
					preparedStatement2.setDouble(5,
							Double.valueOf(r_entry.get(i).get(5)));

					preparedStatement2.executeUpdate();

					preparedStatement2.close();

					tmp = r_entry.get(i).get(0);
				} else {
					String insertRuleQuery2 = "INSERT INTO android.rules (id_rule, id_step, premises) VALUES "
							+ "(?, ?, ?)";

					Connection connection = Connection_DB.getConnection();
					PreparedStatement preparedStatement2 = connection
							.prepareStatement(insertRuleQuery2);

					preparedStatement2.setInt(1,
							Integer.valueOf(r_entry.get(i).get(0)));
					preparedStatement2.setInt(2,
							Integer.valueOf(r_entry.get(i).get(1)));
					preparedStatement2.setString(3, r_entry.get(i).get(2));
					preparedStatement2.executeUpdate();

					preparedStatement2.close();
				}
			} catch (SQLException se) {
				System.err.println(se.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Success insert");

	}

	/**
	 * 
	 * @param name_place
	 * @return le nom du couloir ayant un mur en commun avec la salle en entrée
	 */
	public String getCorridor(String name_place) {

		String results = null;

		try {
			// Initialisation of query
			String query = "SELECT t.name_place as name "
					+ "FROM android.places AS pl inner join android.places AS t "
					+ "ON ST_Intersects(pl.walls, t.walls) where  t.name_place like 'A4C%' and pl.name_place='"
					+ name_place + "' limit 1;";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			result.next();

			results = result.getString("name");
			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println(results);
		return results;
	}

	/**
	 * 
	 * @param x1
	 *            : latitude point de départ
	 * @param y1
	 *            : longitude point de départ
	 * @param x2
	 *            : latitude point d'arrivée
	 * @param y2
	 *            : longitude point d'arrivée
	 * @return : liste de liste (nom de la salle, la latitude de son centre, la
	 *         longitude de son centre)
	 */
	public ArrayList<ArrayList<String>> createPath(double x1, double y1,
			double x2, double y2) {
		ArrayList<ArrayList<String>> path = new ArrayList<ArrayList<String>>();

		try {
			if (x1 < x2) {
				// Initialisation of query
				String query = "SELECT trim(pl.name_place) as name,"
						+ " ST_X(ST_AsText(ST_Centroid(pl.area))) as x,"
						+ " ST_Y(ST_AsText(ST_Centroid(pl.area))) as y"
						+ " FROM android.places AS pl"
						+ " WHERE ST_Intersects(ST_MakeLine(ST_MakePoint(" + x1
						+ " ," + y1 + ")," + " ST_MakePoint(" + x2 + "," + y2
						+ "))" + " , pl.area)='true'" + " ORDER BY x ASC;";

				Connection connection = Connection_DB.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(query);

				ResultSet result = preparedStatement.executeQuery();
				while (result.next()) {
					ArrayList<String> tmp = new ArrayList<String>();

					tmp.add(result.getString("name"));
					tmp.add(result.getString("x"));
					tmp.add(result.getString("y"));

					path.add(tmp);
				}

				preparedStatement.close();

			} else {
				// Initialisation of query
				String query = "SELECT trim(pl.name_place) as name,"
						+ " ST_X(ST_AsText(ST_Centroid(pl.area))) as x ,"
						+ " ST_Y(ST_AsText(ST_Centroid(pl.area))) as y"
						+ " FROM android.places AS pl"
						+ " WHERE ST_Intersects(ST_MakeLine(ST_MakePoint(" + x1
						+ " ," + y1 + ")," + " ST_MakePoint(" + x2 + "," + y2
						+ "))" + " , pl.area)='true'" + " ORDER BY x DESC;";

				Connection connection = Connection_DB.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(query);

				ResultSet result = preparedStatement.executeQuery();
				while (result.next()) {
					ArrayList<String> tmp = new ArrayList<String>();

					tmp.add(result.getString("name"));
					tmp.add(result.getString("x"));
					tmp.add(result.getString("y"));

					path.add(tmp);
				}

				preparedStatement.close();

			}
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
		}

		return path;
	}
	
	
	/**
	 * 
	 * @param x1
	 *            : latitude point de départ
	 * @param y1
	 *            : longitude point de départ
	 * @param x2
	 *            : latitude point d'arrivée
	 * @param y2
	 *            : longitude point d'arrivée
	 * @return : liste de liste (nom du couloir, la latitude de son centre, la
	 *         longitude de son centre)
	 */
	public ArrayList<ArrayList<String>> createPathCorridors(double x1, double y1,
			double x2, double y2) {
		ArrayList<ArrayList<String>> path = new ArrayList<ArrayList<String>>();
		Engine en = new Engine();

		try {
			if (x1 < x2) {
				// Initialisation of query
				String query = "SELECT trim(pl.name_place) as name,"
						+ " ST_X(ST_AsText(ST_Centroid(pl.area))) as x,"
						+ " ST_Y(ST_AsText(ST_Centroid(pl.area))) as y"
						+ " FROM android.places AS pl"
						+ " WHERE ST_Intersects(ST_MakeLine(ST_MakePoint(" + x1
						+ " ," + y1 + ")," + " ST_MakePoint(" + x2 + "," + y2
						+ "))" + " , pl.area)='true' and pl.name_place like 'A4C%'" + " ORDER BY x ASC;";

				Connection connection = Connection_DB.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(query);

				ResultSet result = preparedStatement.executeQuery();
				while (result.next()) {
						ArrayList<String> tmp = new ArrayList<String>();
	
						tmp.add(result.getString("name"));
						tmp.add(result.getString("x"));
						tmp.add(result.getString("y"));
	
						path.add(tmp);
				}

				preparedStatement.close();

			} else {
				// Initialisation of query
				String query = "SELECT trim(pl.name_place) as name,"
						+ " ST_X(ST_AsText(ST_Centroid(pl.area))) as x ,"
						+ " ST_Y(ST_AsText(ST_Centroid(pl.area))) as y"
						+ " FROM android.places AS pl"
						+ " WHERE ST_Intersects(ST_MakeLine(ST_MakePoint(" + x1
						+ " ," + y1 + ")," + " ST_MakePoint(" + x2 + "," + y2
						+ "))" + " , pl.area)='true' and pl.name_place like 'A4C%'" + " ORDER BY x DESC;";

				Connection connection = Connection_DB.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(query);

				ResultSet result = preparedStatement.executeQuery();
				while (result.next()) {
						ArrayList<String> tmp = new ArrayList<String>();
	
						tmp.add(result.getString("name"));
						tmp.add(result.getString("x"));
						tmp.add(result.getString("y"));
	
						path.add(tmp);
				}

				preparedStatement.close();

			}
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
		}

		return path;
	}

	/**
	 * 
	 * @param name_place
	 * @return les coordonnées du centre de la salle
	 */
	public ArrayList<String> getCoorPlace(String name_place) {

		ArrayList<String> results = new ArrayList<String>();

		try {
			// Initialisation of query
			String query = "select ST_X(ST_AsText(ST_Centroid(pl.area))) AS X , ST_Y(ST_AsText(ST_Centroid(pl.area))) as Y from android.places as pl where pl.name_place='"
					+ name_place + "';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			result.next();
			results.add(result.getString("X"));
			results.add(result.getString("Y"));
			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return le  nom de la salle en fonction des coordonnées en entrée
	 */
	public String getNamePlaceFromCoord(double x, double y){
		String place = null;

		try {
			// Initialisation of query
			String query = "SELECT pl.name_place as name" +
					" FROM android.places AS pl" +
					" WHERE ST_Within('POINT("+ x +" "+ y+")', pl.area)='true';";

			Connection connection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);

			ResultSet result = preparedStatement.executeQuery();

			result.next();

			place = result.getString("name");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return place;
	}

}
