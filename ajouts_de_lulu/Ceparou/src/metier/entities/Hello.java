package metier.entities;

public class Hello {
	
	private String nom;
	private String heure;
	
	public Hello() {
		super();
	}
	
	public Hello(String nom, String heure) {
		super();
		this.nom = nom;
		this.heure = heure;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getHeure() {
		return heure;
	}
	
	public void setHeure(String heure) {
		this.heure = heure;
	}

	public String direCoucou(String nom, String heure) {
		return "Coucou " + nom + ", il est " + heure + " !";
	}
	
	
}
