package ia;

import java.util.ArrayList;

import database.JdbcPersistence;

import weka.associations.Apriori;
import weka.associations.AssociationRules;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * Cette classe permet de tester l'exécution de Apriori
 * ainsi que l'insertion des données dans la table rules
 * @author nassima
 *
 */
public class Test {

	public static void main(String[] args) throws Exception {
		CreateApriori ca = new CreateApriori();
		JdbcPersistence jp = new JdbcPersistence();
		IA ia = new IA();
		Engine en = new Engine();
		ArrayList<String> test = new ArrayList<String>();
		Instances dataSet;
		AssociationRules ars = new AssociationRules(null);
		dataSet = ca.getInstance();
		ars = ia.getAllRules(dataSet);
		
		ArrayList<ArrayList<String>> premises = en.premisesToArray(ars);
		ArrayList<ArrayList<String>> consequences = en.consequenceToArray(ars);
	
//		jp.addRule(premises, consequences);
//		jp.getConsequence("A458");
		
		ArrayList<ArrayList<String>> rules = en.getRules(premises, consequences);
		jp.addRuleV2(rules);


	}

}
