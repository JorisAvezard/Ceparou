package service;

import javax.ws.rs.GET;
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
	
	@GET
	@Path("/coucou/{nom}/{heure}")
	@Produces(MediaType.APPLICATION_JSON)
	public String Coucou(@PathParam (value="nom")String nom, @PathParam (value="heure")String heure) {
		return hello.direCoucou(nom, heure);
	}
	
	@GET
	@Path("/insertUser/{id}/{pseudo}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newUser(@PathParam (value="id")String id, @PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password) {
		User user = new User(id, pseudo, password);
		ExecuteQuery.insertUser(user);
	}
	
	@GET
	@Path("/insertProfile/{id}/{fname}/{lname}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newProfile(@PathParam (value="id")String id, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
		Profile profile = new Profile(firstname, lastname, email, id);
		ExecuteQuery.insertProfile(profile);
	}
	
	@GET
	@Path("/insertBuilding/{id}/{name_main}/{name_specific}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newBuildings(@PathParam (value="id")String id, @PathParam (value="name_main")String name_main, @PathParam (value="name_specific")String name_specific) {
		Building building = new Building(id, name_main, name_specific);
		ExecuteQuery.insertBuilding(building);
	}
	
	@GET
	@Path("/insertPlace/{id_place}/{name_place}/{area}/{walls}/{building_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newPlace(@PathParam (value="id_place")String id, @PathParam (value="name_place")String name, @PathParam (value="area")String area, @PathParam (value="walls")String walls, @PathParam (value="building_id")String building_id) {
		Place place = new Place(id, name, area, walls, building_id);
		ExecuteQuery.insertPlace(place);
	}
	
	@GET
	@Path("/insertPath/{id_user}/{id_building}/{id_path}/{coordinate}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public void newPath(@PathParam (value="id_user")String id_user, @PathParam (value="id_building")String id_building, @PathParam (value="id_path")String id_path, @PathParam (value="coordinate")String coordinate, @PathParam (value="date")String date) {
		Way way = new Way(id_path, coordinate, date, id_user, id_building);
		ExecuteQuery.insertPath(way);
	}
	
}
