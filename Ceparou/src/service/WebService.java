package service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import metier.entities.Building;
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
		eq.insertUser(pseudo, password);
	}
	
	@GET
	@Path("/inscription/{pseudo}/{password}/{fname}/{lname}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public void inscription(@PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
		eq.insertUser(pseudo, password);
		User user = eq.selectUser(pseudo, password);
		Profile profile = new Profile(firstname, lastname, email, user.getId_user());
		eq.insertProfile(profile);
	}
	
//	@GET
//	@Path("/insertProfile/{id}/{fname}/{lname}/{email}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void newProfile(@PathParam (value="id")String id, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
//		Profile profile = new Profile(firstname, lastname, email, id);
//		eq.insertProfile(profile);
//	}
	
//	@GET
//	@Path("/insertBuilding/{id}/{name_main}/{name_specific}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void newBuildings(@PathParam (value="id")String id, @PathParam (value="name_main")String name_main, @PathParam (value="name_specific")String name_specific) {
//		Building building = new Building(id, name_main, name_specific);
//		ExecuteQuery.insertBuilding(building);
//	}
//	
//	@GET
//	@Path("/insertPlace/{id_place}/{name_place}/{area}/{walls}/{building_id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void newPlace(@PathParam (value="id_place")String id, @PathParam (value="name_place")String name, @PathParam (value="area")String area, @PathParam (value="walls")String walls, @PathParam (value="building_id")String building_id) {
//		Place place = new Place(id, name, area, walls, building_id);
//		ExecuteQuery.insertPlace(place);
//	}
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
		User user = eq.selectUser(pseudo, password);
		System.out.println(user.toString());
		String result = "{\"id_user\":\"" + user.getId_user() + "\", \"pseudo\":\""+ user.getPseudo() + "\", \"password\":\"" + user.getPassword() + "\"}";
		System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/mail/{email}")
	public String selectMail(@PathParam (value="email")String email) {
		String result = eq.selectMail(email);
		return result;
	}
	
	@GET
	@Path("/pseudo/{pseudo}")
	public String selectPseudo(@PathParam (value="pseudo")String pseudo) {
		String result = eq.selectMail(pseudo);
		return result;
	}
	
}
