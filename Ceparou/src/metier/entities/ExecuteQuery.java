package metier.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.postgis.*;

public class ExecuteQuery {
	
	/*
	 * INSERT
	 */
	public void insertUser(String pseudo, String password, String firstname, String lastname, String email) {
		try {
			String insertUsersQuery = "INSERT INTO android.users (pseudo, password, firstname, lastname, email, grade_user) VALUES (?,?,?,?,?,'client')";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertUsersQuery);
			
			preparedStatement.setString(1, pseudo);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, firstname);
			preparedStatement.setString(4, lastname);
			preparedStatement.setString(5, email);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertPlace(String name_place, String area, String walls, String id) {
		try {
			String insertPlacesQuery = "INSERT INTO android.places (name_place, area, walls, building_id) VALUES ('"+ name_place + "', '"+ area + "', '"+ walls + "', "+ id + ")";
			
			Connection dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPlacesQuery);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void insertBuilding(String name_main, String name_specific) {
		try {
			String insertBuildingsQuery = "INSERT INTO android.buildings (name_main, name_specific) VALUES (?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertBuildingsQuery);
			
			preparedStatement.setString(1, name_main);
			preparedStatement.setString(2, name_specific);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public User selectUser(String pseudo, String password) {
		User user = new User();
		try {
			String selectUserQuery = "SELECT id_user, pseudo, password, grade_user FROM android.users WHERE pseudo = ? AND password = ?";
			
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectUserQuery);
			preparedStatement.setString(1, pseudo);
			preparedStatement.setString(2, password);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				user.setId_user(result.getInt("id_user"));
				user.setPseudo(result.getString("pseudo").trim());
				user.setPassword(result.getString("password").trim());
				user.setGrade_user(result.getString("grade_user").trim());
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public String selectPseudo(String pseudo) {
		String response = "";
		try {
			String selectPseudoQuery = "SELECT pseudo FROM android.users WHERE pseudo = ?";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectPseudoQuery);
			preparedStatement.setString(1, pseudo);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				response = result.getString("pseudo").trim();
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String selectMail(String mail) {
		String response = "";
		try {
			String selectMailQuery = "SELECT email FROM android.users WHERE email = ?";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectMailQuery);
			preparedStatement.setString(1, mail);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				response = result.getString("email").trim();
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public List<Building> selectBuilding() {
		List<Building> list_building = new ArrayList<Building>();
		try {
			String selectBuildingQuery = "SELECT * FROM android.buildings";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectBuildingQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Building building = new Building();
				building.setId_building(result.getInt("id_building"));
				building.setName_main(result.getString("name_main").trim());
				building.setName_specific(result.getString("name_specific").trim());
				list_building.add(building);
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list_building;
	}
	
	public String coordinatesWithId(String id) {
		String lastId = "";
		try {
			String selectIdQuery = "SELECT ST_X(ST_AsText(coordinates)) as X, ST_Y(ST_AsText(coordinates)) as Y FROM android.paths WHERE date_time = (SELECT max(date_time) FROM android.paths WHERE user_id = ?)";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			preparedStatement.setInt(1, Integer.parseInt(id));
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				lastId += result.getString("X");
				lastId += "-" + result.getString("Y");
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lastId;
	}
	
	public String placeWithCoord(Double X, Double Y) {
		String name_place = "";
		try {
			String selectIdQuery = "SELECT name_place FROM android.places WHERE ST_Within('POINT(" + X + " " + Y + ")', area)='true'";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				name_place = result.getString("name_place").trim();
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return name_place;
	}
	
	public String idPlaceWithCoord(Double X, Double Y) {
		String id_place = "";
		try {
			String selectIdQuery = "SELECT id_place FROM android.places WHERE ST_Within('POINT(" + X + " " + Y + ")', area)='true'";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				id_place = String.valueOf(result.getInt("id_place"));
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return id_place;
	}
	
	public String buildingWithCoord(Double X, Double Y) {
		String id_Building = "";
		try {
			String selectIdQuery = "SELECT building_id FROM android.places WHERE ST_Within('POINT(" + X + " " + Y + ")', area)='true'";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				id_Building = String.valueOf(result.getInt("building_id"));
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return id_Building;
	}
	
	public String newId() {
		String new_id = "";
		try {
			String selectIdQuery = "SELECT MAX(id_path)+1 as idMax FROM android.paths";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				new_id = String.valueOf(result.getInt("idMax"));
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new_id;
	}
	
	public String newCoord() {
		String new_coord = "";
		try {
			String selectIdQuery = "SELECT MAX(id_coord)+1 as maxCoord FROM android.paths";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIdQuery);
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				new_coord = String.valueOf(result.getInt("maxCoord"));
			}
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new_coord;
	}
	
	public void InsertPath(String idPath, String idCoord, String latitude, String longitude, String date, String idUser, String idBuilding) {
		try {
			String selectPathQuery = "INSERT INTO android.paths (id_path, id_coord, coordinates, date_time, user_id, building_id) VALUES(" + idPath + ", " + idCoord + ", 'POINT(" + latitude + " " + longitude + ")', '" + date + "', " + idUser + ", " + idBuilding + ")";
			Connection dbConnection;
			dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectPathQuery);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
