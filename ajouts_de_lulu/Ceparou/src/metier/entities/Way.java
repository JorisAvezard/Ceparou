package metier.entities;

public class Way {
	
	private int id_path;
	private String coordinates;
	private String date_time;
	private int user_id;
	private int building_id;
	
	public Way() {
		// TODO Auto-generated constructor stub
	}

	public Way(int id_path, String coordinates, String date_time, int user_id, int building_id) {
		super();
		this.id_path = id_path;
		this.coordinates = coordinates;
		this.date_time = date_time;
		this.user_id = user_id;
		this.building_id = building_id;
	}

	public int getId_path() {
		return id_path;
	}

	public void setId_path(int id_path) {
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}

}
