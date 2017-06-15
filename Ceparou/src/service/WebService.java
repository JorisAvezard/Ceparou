package service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import metier.entities.Building;
import metier.entities.Coordinate;
import metier.entities.ExecuteQuery;
import metier.entities.Hello;
import metier.entities.Way;
import metier.entities.Place;
import metier.entities.Profile;
import metier.entities.User;

@Path("/service")
public class WebService {
	
	private Hello hello = new Hello();
	private ExecuteQuery eq = new ExecuteQuery();
	
	@GET
	@Path("/coucou/{nom}/{heure}")
	@Produces(MediaType.APPLICATION_JSON)
	public String Coucou(@PathParam (value="nom")String nom, @PathParam (value="heure")String heure) {
		return hello.direCoucou(nom, heure);
	}
	
	@GET
	@Path("/insertUser/{pseudo}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newUser(@PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password) {
		System.out.println("Insertion : user");
		eq.insertUser(pseudo, password);
	}
	
	@GET
	@Path("/inscription/{pseudo}/{password}/{fname}/{lname}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public void inscription(@PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
		System.out.println("Insertion : user");
		eq.insertUser(pseudo, password);
		System.out.println("Selection : user");
		User user = eq.selectUser(pseudo, password);
		Profile profile = new Profile(firstname, lastname, email, user.getId_user());
		System.out.println("Insertion : profile");
		eq.insertProfile(profile);
	}
	
//	@GET
//	@Path("/insertProfile/{id}/{fname}/{lname}/{email}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void newProfile(@PathParam (value="id")String id, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
//		Profile profile = new Profile(firstname, lastname, email, id);
//		eq.insertProfile(profile);
//	}
	
	@GET
	@Path("/insertBuilding/{name_main}/{name_specific}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newBuildings(@PathParam (value="name_main")String name_main, @PathParam (value="name_specific")String name_specific) {
		System.out.println("Insert : building");
		eq.insertBuilding(name_main, name_specific);
	}
//	
//	@GET
//	@Path("/insertPath/{id_user}/{id_building}/{id_path}/{coordinate}/{date}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void newPath(@PathParam (value="id_user")String id_user, @PathParam (value="id_building")String id_building, @PathParam (value="id_path")String id_path, @PathParam (value="coordinate")String coordinate, @PathParam (value="date")String date) {
//		Way way = new Way(id_path, coordinate, date, id_user, id_building);
//		ExecuteQuery.insertPath(way);
//	}
//	
//	@GET
//	@Path("/updatePass/{user}/{newPassword}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void modifyPass(@PathParam (value="user")User user, @PathParam (value="newPassword")String newPassword) {
//		ExecuteQuery.updateUser(user, newPassword);
//	}
//	
//	@GET
//	@Path("/updateMail/{profile}/{newMail}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void modifyMail(@PathParam (value="profile")Profile profile, @PathParam (value="newMail")String newMail) {
//		ExecuteQuery.updateProfile(profile, newMail);
//	}
//
//	@POST
//	@Path("/selectMail/{profile}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String findMail(@PathParam (value="profile")Profile profile) {
//		return ExecuteQuery.selectProfile(profile);
//	}
	
	@GET
	@Path("/connexion/{pseudo}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public String connexion(@PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password) {
		System.out.println("Selection : user");
		User user = eq.selectUser(pseudo, password);
		String result = "{\"id_user\":\"" + user.getId_user() + "\", \"pseudo\":\""+ user.getPseudo() + "\", \"password\":\"" + user.getPassword() + "\"}";
		return result;
	}
	
	@GET
	@Path("/mail/{email}")
	public String selectMail(@PathParam (value="email")String email) {
		System.out.println("Selection : 'mail' in profile");
		String result = eq.selectMail(email);
		return result;
	}
	
	@GET
	@Path("/pseudo/{pseudo}")
	public String selectPseudo(@PathParam (value="pseudo")String pseudo) {
		System.out.println("Selection : 'pseudo' in user");
		String result = eq.selectPseudo(pseudo);
		return result;
	}
	
	@GET
	@Path("/savePlace/{name_place}/{area}/{walls}/{building_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void savePlace(@PathParam (value="name_place")String name_place, @PathParam (value="area")String area, @PathParam (value="walls")String walls, @PathParam (value="building_id")String building_id) {
		System.out.println("Insertion : place");
		eq.insertPlace1(name_place, area, walls, building_id);
	}
	
	@GET
	@Path("/building")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Building> selectAllBuilding() {
		System.out.println("Select * : building");
		List<Building> list_building = new ArrayList<Building>();
		list_building = eq.selectBuilding();
		
		return list_building;
	}
}
