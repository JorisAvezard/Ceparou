package ia;

import java.util.ArrayList;

import weka.core.Attribute;
import database.JdbcPersistence;

/**
 * Elle génère automatiquement des données dans la table paths
 * @author nassima
 *
 */
public class CreateRandomPaths {

	static JdbcPersistence simulation = new JdbcPersistence();

	public static double randomCoordonneX() {
		double x;
		x = Math.random() * (49.04294228 - 49.04249746) + 49.04249746; // Math.random() * (max - min) + min
		System.out.println(x);
		return x;
	}
	public static double randomCoordonneY() {
		double y;
		y = Math.random() * (2.08415821 - 2.08352521) + 2.08352521;
		System.out.println(y);
		return y;
	}


	public static void main(String[] args) {

		int nombre_path =1000;
		
		for(int i=1; i<=nombre_path;i++){
			int nombre_etape = (int) (Math.random() * (300-1) + 1);
			
			for(int j=1;j<=nombre_etape; j++){
				simulation.addstep(i, j, randomCoordonneX(), randomCoordonneY());
			}
			
		}
		
	}

}
