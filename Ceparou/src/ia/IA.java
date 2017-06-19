package ia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ia.Engine;
import database.JdbcPersistence;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * Cette classe contient la méhode qui exécute Apriori
 * elle est appelée dans Test.java
 * @author nassima
 *
 */
public class IA {

	public IA() {

	}

	public static AssociationRules getAllRules(Instances datasetentry) throws Exception {
		datasetentry.setClassIndex(datasetentry.numAttributes() - 1);

		double deltaValue = 0.05;
		double lowerBoundMinSupportValue = 0.1;
		double minMetricValue = 0.5;
		int numRulesValue = 20;
		double upperBoundMinSupportValue = 1.0;
		String resultapriori;

		Apriori apriori = new Apriori();
		apriori.setDelta(deltaValue);
		apriori.setLowerBoundMinSupport(lowerBoundMinSupportValue);
		apriori.setNumRules(numRulesValue);
		apriori.setUpperBoundMinSupport(upperBoundMinSupportValue);
		apriori.setMinMetric(minMetricValue);
		apriori.setClassIndex(datasetentry.classIndex());
		apriori.buildAssociations(datasetentry);

//		System.out.println(Utils.doubleToString(apriori.getLowerBoundMinSupport(), 2));
//		System.out.println(apriori.toString());

		AssociationRules arules = apriori.getAssociationRules();
//		 for(AssociationRule ar : arules.getRules())
//		 {
//		 System.out.println("conf: " + ar.getPrimaryMetricValue());
//		 System.out.println("premise: " + ar.getPremise());
//		 System.out.println("consequence: "+ar.getConsequence());
//		
//		 System.out.println("---");
//		
//		 }

		return arules;
	}

}
