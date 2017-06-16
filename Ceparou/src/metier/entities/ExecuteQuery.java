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
			String insertUsersQuery = "INSERT INTO android.users (pseudo, password, firstname, lastname, email) VALUES (?,?,?,?,?)";
			
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
	
	
}
