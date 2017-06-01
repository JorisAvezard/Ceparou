package metier.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQuery {
	
	/*
	 * INSERT
	 */
	public static void insertUser(String pseudo, String password) {
		try {
			String insertUsersQuery = "INSERT INTO users (pseudo, password) VALUES (?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertUsersQuery);
			
			preparedStatement.setString(1, pseudo);
			preparedStatement.setString(2, password);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void insertProfile(Profile profile) {
		try {
			String insertProfilesQuery = "INSERT INTO profiles (firstname, lastname, email, user_id) VALUES (?,?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertProfilesQuery);
			
			preparedStatement.setString(1, profile.getFirstname());
			preparedStatement.setString(2, profile.getLastname());
			preparedStatement.setString(3, profile.getEmail());
			preparedStatement.setString(4, profile.getUser_id());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void insertBuilding(Building building) {
		try {
			String insertBuildingsQuery = "INSERT INTO buildings (id_building, name_main, name_specific) VALUES (?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertBuildingsQuery);
			
			preparedStatement.setString(1, building.getId_building());
			preparedStatement.setString(2, building.getName_main());
			preparedStatement.setString(3, building.getName_specific());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void insertPlace(Place place) {
		try {
			String insertPlacesQuery = "INSERT INTO places (id_place, name_place, area, walls, building_id) VALUES (?,?,?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPlacesQuery);
			
			preparedStatement.setString(1, place.getId_place());
			preparedStatement.setString(2, place.getName_place());
			preparedStatement.setString(3, place.getArea());
			preparedStatement.setString(4, place.getWalls());
			preparedStatement.setString(5, place.getBuilding_id());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void insertPath(Way path) {
		try {
			String insertPathsQuery = "INSERT INTO paths (id_path, coordinates, date_time, user_id, building_id) VALUES (?,?,?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPathsQuery);
			
			preparedStatement.setString(1, path.getId_path());
			preparedStatement.setString(2, path.getCoordinates());
			preparedStatement.setString(3, path.getDate_time());
			preparedStatement.setString(4, path.getUser_id());
			preparedStatement.setString(5, path.getBuilding_id());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	/*
	 * UPDATE
	 */
	public static void updateUser(User user,String new_password) {
		try {
			String updateUsersQuery = "UPDATE users SET password = '?' WHERE id_user = '?' ";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(updateUsersQuery);
			
			preparedStatement.setString(1, new_password);
			preparedStatement.setString(2, user.getId_user());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void updateProfile(Profile profile,String new_email) {
		try {
			String updateProfilesQuery = "UPDATE profiles SET email = '?' WHERE user_id = '?' ";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(updateProfilesQuery);
			
			preparedStatement.setString(1, new_email);
			preparedStatement.setString(2, profile.getUser_id());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	/*
	 * DELETE
	 */
	public static void deleteProfile(Profile profile) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
		try {
			String deleteProfilesQuery = "DELETE FROM 'profiles' WHERE 'user_id' = ?";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteProfilesQuery);
			
			preparedStatement.setString(1, profile.getUser_id());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void deleteUser(User user) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
		try {
			String deleteUsersQuery = "DELETE FROM 'users' WHERE 'id_user' = ?";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteUsersQuery);
			
			preparedStatement.setString(1, user.getId_user());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public static void deletePath(Way path) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
		try {
			String deletePathsQuery = "DELETE FROM 'paths' WHERE 'user_id' = ?";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(deletePathsQuery);
			
			preparedStatement.setString(1, path.getId_path());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	/*
	 * SELECT
	 */
	public static String selectProfile(Profile profile) {
		String mail = "";
		try {
			String selectProfilesQuery = "SELECT email FROM profiles AS p WHERE p.user_id = ?";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectProfilesQuery);
			
			preparedStatement.setString(1, profile.getUser_id());
			
			ResultSet result = preparedStatement.executeQuery();
			
			while (result.next()) {
				
				mail = result.getString("email");
				
			}
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
		return mail;
	}
	
}
