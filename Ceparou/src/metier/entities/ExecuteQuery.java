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
	public void insertUser(String pseudo, String password) {
		try {
			String insertUsersQuery = "INSERT INTO android.users (pseudo, password) VALUES (?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertUsersQuery);
			
			preparedStatement.setString(1, pseudo);
			preparedStatement.setString(2, password);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertProfile(Profile profile) {
		try {
			String insertProfilesQuery = "INSERT INTO android.profiles (firstname, lastname, email, user_id) VALUES (?,?,?,?)";
			Geometry geometry = new GeometryCollection();
			Connection dbConnection = Connection_DB.getConnection();
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertProfilesQuery);
			
			preparedStatement.setString(1, profile.getFirstname());
			preparedStatement.setString(2, profile.getLastname());
			preparedStatement.setString(3, profile.getEmail());
			preparedStatement.setInt(4, profile.getUser_id());
			
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
			
			String insertPlacesQuery = "INSERT INTO android.places (name_place, area, walls, building_id) VALUES (?,?,?,?)";
			
			Connection dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPlacesQuery);
			
			preparedStatement.setString(1, name_place);
			preparedStatement.setString(2, area.replaceAll("-", " "));
			preparedStatement.setString(3, walls.replaceAll("-", " "));
			preparedStatement.setInt(4, Integer.parseInt(id));
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
		} catch(SQLException se) {
			System.err.println(se.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPlace1(String name_place, String area, String walls, String id) {
		try {
			String insertPlacesQuery = "INSERT INTO android.places (name_place, area, walls, building_id) VALUES ('"+ name_place + "', '"+ area + "', '"+ walls + "', "+ id + ")";
			
			Connection dbConnection = Connection_DB.getConnection();
			
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPlacesQuery);
            
//			preparedStatement.setString(1, name_place);
//			preparedStatement.setString(2, area.replaceAll("-", " "));
//			preparedStatement.setString(3, walls.replaceAll("-", " "));
//			preparedStatement.setInt(4, Integer.parseInt(id));
			
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
//	
//	
//	public static void insertPath(Way path) {
//		try {
//			String insertPathsQuery = "INSERT INTO android.paths (id_path, coordinates, date_time, user_id, building_id) VALUES (?,?,?,?,?)";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPathsQuery);
//			
//			preparedStatement.setString(1, path.getId_path());
//			preparedStatement.setString(2, path.getCoordinates());
//			preparedStatement.setString(3, path.getDate_time());
//			preparedStatement.setString(4, path.getUser_id());
//			preparedStatement.setString(5, path.getBuilding_id());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	/*
//	 * UPDATE
//	 */
//	public static void updateUser(User user,String new_password) {
//		try {
//			String updateUsersQuery = "UPDATE android.users SET password = '?' WHERE id_user = '?' ";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(updateUsersQuery);
//			
//			preparedStatement.setString(1, new_password);
//			preparedStatement.setString(2, user.getId_user());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	public static void updateProfile(Profile profile,String new_email) {
//		try {
//			String updateProfilesQuery = "UPDATE android.profiles SET email = '?' WHERE user_id = '?' ";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(updateProfilesQuery);
//			
//			preparedStatement.setString(1, new_email);
//			preparedStatement.setString(2, profile.getUser_id());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	/*
//	 * DELETE
//	 */
//	public static void deleteProfile(Profile profile) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
//		try {
//			String deleteProfilesQuery = "DELETE FROM android.profiles WHERE user_id = ?";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteProfilesQuery);
//			
//			preparedStatement.setString(1, profile.getUser_id());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	public static void deleteUser(User user) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
//		try {
//			String deleteUsersQuery = "DELETE FROM android.users WHERE id_user = ?";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteUsersQuery);
//			
//			preparedStatement.setString(1, user.getId_user());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	public static void deletePath(Way path) {	/*CREER UN BOUTON 'EFFACER SON COMPTE' POUR PROPOSER CA ??*/
//		try {
//			String deletePathsQuery = "DELETE FROM android.paths WHERE user_id = ?";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(deletePathsQuery);
//			
//			preparedStatement.setString(1, path.getId_path());
//			
//			preparedStatement.executeUpdate();
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//	}
//	
//	/*
//	 * SELECT
//	 */
//	public static String selectProfile(Profile profile) {
//		String mail = "";
//		try {
//			String selectProfilesQuery = "SELECT email FROM android.profiles AS p WHERE p.user_id = ?";
//			
//			Connection dbConnection = Connection_DB.getConnection();
//			PreparedStatement preparedStatement = dbConnection.prepareStatement(selectProfilesQuery);
//			
//			preparedStatement.setString(1, profile.getUser_id());
//			
//			ResultSet result = preparedStatement.executeQuery();
//			
//			while (result.next()) {
//				
//				mail = result.getString("email");
//				
//			}
//			
//			preparedStatement.close();
//		} catch(SQLException se) {
//			System.err.println(se.getMessage());
//		}
//		return mail;
//	}
	
	public User selectUser(String pseudo, String password) {
		User user = new User();
		try {
			String selectUserQuery = "SELECT id_user, pseudo, password FROM android.users WHERE pseudo = ? AND password = ?";
			
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
			String selectMailQuery = "SELECT email FROM android.profiles WHERE email = ?";
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
