package metier.entities;

public class Place {
	
	private String id_place;
	private String name_place;
	private String area;
	private String walls;
	private String building_id;
	
	public Place() {
		// TODO Auto-generated constructor stub
	}

	public Place(String id_place, String name_place, String area, String walls, String building_id) {
		super();
		this.id_place = id_place;
		this.name_place = name_place;
		this.area = area;
		this.walls = walls;
		this.building_id = building_id;
	}

	public String getId_place() {
		return id_place;
	}

	public void setId_place(String id_place) {
		this.id_place = id_place;
	}

	public String getName_place() {
		return name_place;
	}

	public void setName_place(String name_place) {
		this.name_place = name_place;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getWalls() {
		return walls;
	}

	public void setWalls(String walls) {
		this.walls = walls;
	}

	public String getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}

}
