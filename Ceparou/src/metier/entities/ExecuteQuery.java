package metier.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecuteQuery {
	
	private static void persistUsers(Users user) {
		try {
			String insertUsersQuery = "INSERT INTO users (id_user, pseudo, password) VALUES (?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertUsersQuery);
			
			preparedStatement.setString(1, user.getId_user());
			preparedStatement.setString(2, user.getPseudo());
			preparedStatement.setString(3, user.getPassword());
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		}
	}
	
	private static void persistProfiles(Profiles profile) {
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
	
	private static void persistBuildings(Buildings building) {
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
	
	private static void persistPlaces(Places place) {
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
	
	private static void persistPaths(Paths path) {
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

}
