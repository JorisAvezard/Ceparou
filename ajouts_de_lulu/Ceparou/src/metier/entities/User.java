package metier.entities;

public class User {
	
	private int id_user;
	private String pseudo;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private String grade_user;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id_user, String pseudo, String password, String firstname, String lastname, String email, String grade_user) {
		super();
		this.id_user = id_user;
		this.pseudo = pseudo;
		this.password = password;
		this.password = firstname;
		this.password = lastname;
		this.password = email;
		this.password = grade_user;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getGrade_user() {
		return grade_user;
	}

	public void setGrade_user(String grade_user) {
		this.grade_user = grade_user;
	}

	@Override
	public String toString() {
		return "User [id_user=" + id_user + ", pseudo=" + pseudo + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", email=" + email + ", grade_user=" + grade_user + "]";
	}
}