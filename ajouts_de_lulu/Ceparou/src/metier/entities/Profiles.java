package metier.entities;

public class Profiles {
	
	private String firstname;
	private String lastname;
	private String email;
	private String user_id;
	
	public Profiles() {
		// TODO Auto-generated constructor stub
	}

	public Profiles(String firstname, String lastname, String email, String user_id) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.user_id = user_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
