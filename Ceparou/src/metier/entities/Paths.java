package metier.entities;

public class Paths {
	
	private String id_path;
	private String coordinates;
	private String date_time;
	private String user_id;
	private String building_id;
	
	public Paths() {
		// TODO Auto-generated constructor stub
	}

	public Paths(String id_path, String coordinates, String date_time, String user_id, String building_id) {
		super();
		this.id_path = id_path;
		this.coordinates = coordinates;
		this.date_time = date_time;
		this.user_id = user_id;
		this.building_id = building_id;
	}

	public String getId_path() {
		return id_path;
	}

	public void setId_path(String id_path) {
		this.id_path = id_path;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}

}
