package metier.entities;

public class Profile {
	
	private String firstname;
	private String lastname;
	private String email;
	private int user_id;
	
	public Profile() {
		// TODO Auto-generated constructor stub
	}

	public Profile(String firstname, String lastname, String email, int user_id) {
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
