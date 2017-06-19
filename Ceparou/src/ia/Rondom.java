package ia;

import java.util.ArrayList;

import weka.core.Attribute;
import database.JdbcPersistence;

/**
 * Elle génère automatiquement des données dans la table paths
 * @author nassima
 *
 */
public class Rondom {

	static JdbcPersistence simulation = new JdbcPersistence();

	public static double randomCoordonneX() {
		double x;
		x = (Math.random() * 16);
		System.out.println(x);
		return x;
	}
	public static double randomCoordonneY() {
		double x;
		x = (Math.random() * 4);
		System.out.println(x);
		return x;
	}


	public static void randomSalle() {

	}

	public static void main(String[] args) {

//		for (int i=0;i<150;i++){
//		double x;
//		x = randomCoordonneX();
//		
//		}
		int nombrepath = 1000;
		int nombreetape = 0;
		
		for(int i=1; i<=nombrepath;i++){
			nombreetape = (int) (Math.random() * 500);
			
			for(int j=1;j<=nombreetape; j++){
				simulation.addstep(i, j, randomCoordonneX(), randomCoordonneY());
			}
			
		}
//		simulation.addstep(10, 1, 0, 0);
		
	}

}
