package service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.JdbcPersistence;
import ia.CreateApriori;
import ia.Engine;
import ia.IA;
import ia.TestPrediction;
import metier.entities.Building;
import metier.entities.Coordinate;
import metier.entities.ExecuteQuery;
import metier.entities.Hello;
import metier.entities.Way;
import weka.associations.AssociationRules;
import weka.core.Instances;
import metier.entities.Place;
import metier.entities.Profile;
import metier.entities.User;

@Path("/service")
public class WebService {
	
	private Hello hello = new Hello();
	private ExecuteQuery eq = new ExecuteQuery();
	private TestPrediction testPrediction = new TestPrediction();
	
	@GET
	@Path("/coucou/{nom}/{heure}")
	@Produces(MediaType.APPLICATION_JSON)
	public String Coucou(@PathParam (value="nom")String nom, @PathParam (value="heure")String heure) {
		return hello.direCoucou(nom, heure);
	}
	
	@GET
	@Path("/inscription/{pseudo}/{password}/{fname}/{lname}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public void inscription(@PathParam (value="pseudo")String pseudo, @PathParam (value="password")String password, @PathParam (value="fname")String firstname, @PathParam (value="lname")String lastname, @PathParam (value="email")String email) {
		System.out.println("Insertion : user");
		eq.insertUser(pseudo, password, firstname, lastname, email);
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
		String result = "{\"id_user\":\"" + user.getId_user() + "\", \"pseudo\":\""+ user.getPseudo() + "\", \"password\":\"" + user.getPassword() + "\", \"grade_user\":\"" + user.getGrade_user() + "\"}";
		return result;
	}
	
	@GET
	@Path("/mail/{email}")
	public String selectMail(@PathParam (value="email")String email) {
		System.out.println("Selection : 'mail' in users");
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
		eq.insertPlace(name_place, area, walls, building_id);
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
	
	@GET
	@Path("/selectIdP/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectIdPlace(@PathParam (value="user_id")String id) {
		String id_place = "";
		System.out.println("Select id_place : places");
		String lastId = eq.coordinatesWithId(id);
		System.out.println("IdPlace(coord) : " + lastId);
		if (!lastId.equals("")) {
			String[] coord = lastId.split("-");
			System.out.println("X : " + coord[0] + ", Y : " + coord[1]);
			id_place = eq.idPlaceWithCoord(Double.valueOf(coord[0]), Double.valueOf(coord[1]));
			System.out.println("IdPlace : " + id_place);
			return id_place;
		}
		else {
			id_place = "-1";
			System.out.println("IdPlace changé : " + id_place);
			return id_place;
		}
	}
	
	@GET
	@Path("/selectIdPName/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectNamePlaceWithCoord(@PathParam (value="latitude")double X, @PathParam (value="longitude")double Y) {
		System.out.println("Select name_place : places");
		String name_place = eq.placeWithCoord(X, Y);
		System.out.println("name_place : " + name_place);
		if(name_place.equals("")) {
			name_place = "non correspondance";
		}
		return name_place;
	}
	
	@GET
	@Path("/selectIdB/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectIdBuildingWithCoord(@PathParam (value="latitude")double X, @PathParam (value="longitude")double Y) {
		System.out.println("Select id_building : places");
		String id = eq.buildingWithCoord(X, Y);
		System.out.println("id_Building : " + id);
		if(id.equals("")) {
			id = "1";
		}
		return id;
	}
	
	@GET
	@Path("/selectIdPlace/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectIdPlaceWithCoord(@PathParam (value="latitude")double X, @PathParam (value="longitude")double Y) {
		System.out.println("Select id_place : places");
		String id = eq.idPlaceWithCoord(X, Y);
		System.out.println("id_place : " + id);
		if(id.equals("")) {
			id = "0";
			System.out.println("id_place changé : " + id);
		}
		return id;
	}
	
	@GET
	@Path("/newID")
	@Produces(MediaType.APPLICATION_JSON)
	public String newId() {
		System.out.println("Select max(id_p) : paths");
		String newId = eq.newId();
		System.out.println("newId : " + newId);
		if(newId.equals("0")) {
			newId = "1";
			System.out.println("newId changé : " + newId);
		}
		return newId;
	}
	
	@GET
	@Path("/newCoord")
	@Produces(MediaType.APPLICATION_JSON)
	public String newCoord() {
		System.out.println("Select max(id_c) : paths");
		String lastId = eq.newCoord();
		System.out.println("lastId : " + lastId);
		if(lastId.equals("0")) {
			lastId = "1";
			System.out.println("lastId changé : " + lastId);
		}
		return lastId;
	}
	
	@GET
	@Path("/newPath/{idPath}/{idCoord}/{latitude}/{longitude}/{idUser}/{idBuilding}/")
	@Produces(MediaType.APPLICATION_JSON)
	public void NewPath(@PathParam (value="idPath")String idPath, @PathParam (value="idCoord")String idCoord, 
			@PathParam (value="latitude")String latitude, @PathParam (value="longitude")String longitude, @PathParam (value="idUser")String idUser, 
			@PathParam (value="idBuilding")String idBuilding) {
		System.out.println("Insertion : path");
		//eq.InsertPath(idPath, idCoord, latitude, longitude, idUser, idBuilding);
	}
	
	@GET
	@Path("/apriori")
	public void aPriori() throws Exception{
		CreateApriori ca = new CreateApriori();
		JdbcPersistence jp = new JdbcPersistence();
		IA ia = new IA();
		Engine en = new Engine();
		ArrayList<String> test = new ArrayList<String>();
		Instances dataSet;
		AssociationRules ars = new AssociationRules(null);
		dataSet = ca.getInstance();
		ars = ia.getAllRules(dataSet);
		
		ArrayList<ArrayList<String>> premises = en.premisesToArray(ars);
		ArrayList<ArrayList<String>> consequences = en.consequenceToArray(ars);
		
		ArrayList<ArrayList<String>> rules = en.getRules(premises, consequences);
		jp.addRuleV2(rules);
	}
	
	@GET
	@Path("/predict/{id_user}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> prediction(@PathParam (value="id_user")int id_user) {
		ArrayList<String> predict=new ArrayList<String>();
		predict=testPrediction.predict(1);
		return predict;
	} 
}
