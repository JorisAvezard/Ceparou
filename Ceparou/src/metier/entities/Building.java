package metier.entities;

public class Building {
	
	private String id_building;
	private String name_main;
	private String name_specific;
	
	public Building() {
		// TODO Auto-generated constructor stub
	}

	public Building(String id_building, String name_main, String name_specific) {
		super();
		this.id_building = id_building;
		this.name_main = name_main;
		this.name_specific = name_specific;
	}

	public String getId_building() {
		return id_building;
	}

	public void setId_building(String id_building) {
		this.id_building = id_building;
	}

	public String getName_main() {
		return name_main;
	}

	public void setName_main(String name_main) {
		this.name_main = name_main;
	}

	public String getName_specific() {
		return name_specific;
	}

	public void setName_specific(String name_specific) {
		this.name_specific = name_specific;
	}

}
