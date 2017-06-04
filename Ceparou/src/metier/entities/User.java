package metier.entities;

public class User {
	
	private String id_user;
	private String pseudo;
	private String password;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String id_user, String pseudo, String password) {
		super();
		this.id_user = id_user;
		this.pseudo = pseudo;
		this.password = password;
	}

	public String getId_user() {
		return id_user;
	}

	public void setId_user(String id_user) {
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

	@Override
	public String toString() {
		return "User [id_user=" + id_user + ", pseudo=" + pseudo + ", password=" + password + "]";
	}
	
	

}