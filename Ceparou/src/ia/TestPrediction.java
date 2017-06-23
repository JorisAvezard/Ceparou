package ia;

import java.util.ArrayList;

import database.JdbcPersistence;

/**
 * Cette classe fait la prédiction :
 * elle prend en entrée un id_user
 * elle retourne une liste des prochaines salles 
 * @author nassima
 *
 */
public class TestPrediction {
	static Engine en;
	static JdbcPersistence jp;
	static int totalPaths;
	
	public TestPrediction() {
		Engine en=new Engine();
		JdbcPersistence jp = new JdbcPersistence();
		totalPaths = jp.countTotalPath();
	}
	
	
	//renvoie le chemin sans repitition successif de salle de l'utilisateur 
	public static ArrayList<String> clearPaths(int id_path ){
		ArrayList<String> clearPath = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		path=jp.getPath(id_path);
		 clearPath=en.getDinstinctPlacesFromPath(path);
//		System.out.println(clearPath);
		return clearPath;
	}
	
	//methode qui renvoie les premises 
	public static ArrayList<ArrayList<String>> getAllPremise(){
		JdbcPersistence simulation = new JdbcPersistence();
		int totalPremise = simulation.countTotalRules();
		ArrayList<ArrayList<String>> premises = new ArrayList<ArrayList<String>>();
		
		for(int i=1;i<=totalPremise;i++){
			ArrayList<String> premise = new ArrayList<String>();
			premise = jp.getPremise(i);
			premises.add(premise);
			
		//	for(int j=0;j<premises.size();j++){
		//	for(int k=0;k<premises.get(j).size();k++){
			//		System.out.println(premises.get(j).get(k));
			//	}
			//	System.out.println("---------------");
			}
		//}
		return premises;
	
	}
		
	public static ArrayList<String> predict(int id_user) {
		int id_path = 0;
		id_path=jp.getIdPathUser(id_user);
		ArrayList<String> prediction = new ArrayList<String>();
		ArrayList<String> clearPath = new ArrayList<String>();
		ArrayList<ArrayList<String>> premises = new ArrayList<ArrayList<String>>();
		premises=getAllPremise();
		clearPath=clearPaths(id_path);
			
		for(int j=1;j<premises.size();j++){
			
			if(clearPath.containsAll(premises.get(j))){
			 //k[j+1]=j; 
			//if(premises.get(j).size()>premises.get(k[j-1]).size()){
            String result=jp.getResult(j+1);
            if (!prediction.contains(result)&& prediction.size()<3){
            prediction.add(result);
            //System.out.println("taille :" + premises.get(j).size()+"************");
			System.out.println("la prediction est : *********" +result+"************");}
		}}
		return prediction;
		
	}

	public static void main(String[] args) {
		
		ArrayList<String> predict=new ArrayList<String>();
		predict=predict(1);
		
	}
}


