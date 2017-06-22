package ia;

import java.util.ArrayList;

import database.JdbcPersistence;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;

/**
 * Cette classe contient toutes les méthodes afin de faire 
 * l'insertion dans la base de données des règles à partir
 * des résultats d'Apriori
 * @author nassima
 *
 */
public class Engine {

	public Engine(){
		
	}
	
	/**
	 * Vérifie s'il y a au moins une virgule dans le texre en entrée
	 * @param entry
	 * @return
	 */
	public boolean containsComa(String entry){
		char[] entrybis = entry.toCharArray();
		
		for (int a = 0; a < entrybis.length; a++) {
			if (entrybis[a] == ',')
				return true;
		}
		
		return false;
	}
	
	/**
	 * Supprime les crochets du texte en entrée
	 * @param entry
	 * @return
	 */
	public String deleteHook(String entry){
		String[] end = entry.split("\\]");
		String beforeEnd = end[0];
		String[] begin = beforeEnd.split("\\[");
		String afterLastEqual = begin[begin.length - 1];
//		System.out.println(afterLastEqual);
		
		return afterLastEqual;
	}
	
	/**
	 * Compte le nombre de caractères dans le texte en entrée excepté les espaces
	 * @param textIn
	 * @return
	 */
	public int nombreTotalCaractere(String textIn) {
		int total = 0;
		int nbSpace = 0;

		char[] entry = textIn.toCharArray();

		for (int a = 0; a < entry.length; a++) {
			if (entry[a] == ' ')
				nbSpace++;
		}

		total = entry.length - nbSpace;

//		System.out.println(total);
		return total;
	}
	
	/**
	 * Supprime les espaces d'un texte en entrée
	 * @param textIn
	 * @return
	 */
	public String deleteSpace(String textIn){
		String result = null;
		int nbChar = 0;
		char[] entry = textIn.toCharArray();
		nbChar = nombreTotalCaractere(textIn);
		char[] entry_without_space = new char[nbChar];
		int indice = 0;

		for (int i = 0; i < entry.length; i++) {
			if (entry[i] != ' ') {
				entry_without_space[indice] = entry[i];
				indice++;
			}
		}
		
		result = String.valueOf(entry_without_space);
		
//		System.out.println(result);
		
		return result;
	}
	
	/**
	 * Découpe le texte en entrée au niveau des virgules
	 * Stocke chaque partie dans une array
	 * @param entry
	 * @return
	 */
	public ArrayList<String> splitComa(String entry){
		ArrayList<String> result = new ArrayList<String>();
		String[] firstComa = deleteSpace(entry).split("\\,");

		for(int i=0;i<firstComa.length;i++){
			result.add(firstComa[i]);
//			System.out.println(result.get(i));
		}
		
		return result;
	}
	
	public String SplitAttribute(String entry){
		String[] equalTokens = entry.split("=");
		String afterLastEqual = equalTokens[equalTokens.length - 1];
//		System.out.println(afterLastEqual);
		
		return afterLastEqual;
	}
	
	public static String SplitId(String entry){
		String[] equalTokens = entry.split("=");
		String beforeEqual = equalTokens[0];
//		System.out.println(beforeEqual);
		
		return beforeEqual;
	}
	
	/**
	 * Return all the places where the user passed by during his path
	 * @param entry
	 * @return
	 */
	public ArrayList<String> getDinstinctPlacesFromPath(ArrayList<String> entry){
		ArrayList<String> result = new ArrayList<String>();
		int indice = 0;
		
		result.add(entry.get(0));
		for(int i=1; i<entry.size(); i++){
			if(!(entry.get(i).equals(result.get(indice)))){
				result.add(entry.get(i));
				indice++;
			}
		}
		
//		for(int k=0;k<result.size();k++){
//			System.out.println(result.get(k));
//		}
		
		return result;
	}
	
	/**
	 * Retourne une liste contenant tous les chemins de la bdd
	 * à mettre en tant que données dans l'Algorithme Apriori
	 * @return
	 */
	public ArrayList<ArrayList<String>> getAllPAths(){
		JdbcPersistence simulation = new JdbcPersistence();
		int totalPaths = simulation.countTotalPath();
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		
		for(int i=1;i<=totalPaths;i++){
			ArrayList<String> tempPaths = new ArrayList<String>();
			tempPaths = getDinstinctPlacesFromPath(simulation.getPath(i));
			paths.add(tempPaths);
		}
		
//		for(int j=0;j<paths.size();j++){
//			for(int k=0;k<paths.get(j).size();k++){
//				System.out.println(paths.get(j).get(k));
//			}
//			System.out.println("---------------");
//		}
		
		return paths;
	}
	
	/**
	 * Convertit les règles d'Apriori en une ArrayList d'arraylist de premise comportant chacun :
	 * l'id du chemin, l'id de l'étape du chemin ainsi que le nom de la salle
	 * @param ars
	 * @return
	 */
	public ArrayList<ArrayList<String>> premisesToArray(AssociationRules ars){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		int indice_rule = 1;
		
		for(AssociationRule ar : ars.getRules())
	     {
			if(containsComa(String.valueOf(ar.getPremise()))){
				String entry_modified = deleteSpace(deleteHook(String.valueOf(ar.getPremise())));
				ArrayList<String> tmp = splitComa(entry_modified);
				
				for(int i=0;i<tmp.size();i++){
					ArrayList<String> tmp_bis = new ArrayList<String>();
					tmp_bis.add(String.valueOf(indice_rule));
					tmp_bis.add(SplitId(tmp.get(i)));
					tmp_bis.add(SplitAttribute(tmp.get(i)));
//					tmp.add(String.valueOf(ar.getPrimaryMetricValue()));
					result.add(tmp_bis);
				}
				indice_rule++;
			}
			else{
				String entry_modified = deleteHook(String.valueOf(ar.getPremise()));
				ArrayList<String> tmp_bis = new ArrayList<String>();
				tmp_bis.add(String.valueOf(indice_rule));
				tmp_bis.add(SplitId(entry_modified));
				tmp_bis.add(SplitAttribute(entry_modified));
//				 tmp.add(String.valueOf(ar.getPrimaryMetricValue()));
		         result.add(tmp_bis);
		         indice_rule++;
			}
		 }
		
//		for(int j=0;j<result.size();j++){
//			System.out.println(result.get(j).get(0) + "	" + result.get(j).get(1) + "	" + result.get(j).get(2));
//		}
		
		return result;
	}
	
	/**
	 * Convertit les règles d'Apriori en une ArrayList d'arraylist de consequence comportant chacun :
	 * l'id de l'étape du chemin ainsi que le nom de la salle
	 * @param ars
	 * @return
	 */
	public ArrayList<ArrayList<String>> consequenceToArray(AssociationRules ars){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		int indice_rule = 1;
		
		for(AssociationRule ar : ars.getRules())
	     {   
			String entry_modified = deleteHook(String.valueOf(ar.getConsequence()));
			ArrayList<String> tmp_bis = new ArrayList<String>();
			tmp_bis.add(String.valueOf(indice_rule));
			tmp_bis.add(SplitId(entry_modified));
			tmp_bis.add(SplitAttribute(entry_modified));
//			 tmp.add(String.valueOf(ar.getPrimaryMetricValue()));
	         result.add(tmp_bis);
	         indice_rule++;
		 }
		
//		for(int j=0;j<result.size();j++){
//		System.out.println(result.get(j).get(0) + "	" + result.get(j).get(1) + "	" + result.get(j).get(2));
//	}
		
		
		return result;
	}
	
	public ArrayList<ArrayList<String>> getRules(ArrayList<ArrayList<String>> p_entry, ArrayList<ArrayList<String>> c_entry){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		for(int i=0;i<p_entry.size();i++){
			for(int j=0;j<c_entry.size();j++){
				if(p_entry.get(i).get(0).equals(c_entry.get(j).get(0))){
					ArrayList<String> tmp = new ArrayList<String>();
					tmp.add(p_entry.get(i).get(0));
					tmp.add(p_entry.get(i).get(1));
					tmp.add(p_entry.get(i).get(2));
					tmp.add(c_entry.get(j).get(1));
					tmp.add(c_entry.get(j).get(2));
					result.add(tmp);
				}
			}
		}
		
		for(int j=0;j<result.size();j++){
		System.out.println(result.get(j).get(0) + "	" + result.get(j).get(1) + "	" + result.get(j).get(2) + "	" + result.get(j).get(3) + "	" + result.get(j).get(4));
		}
		
		return result;
	}
	
	
}
