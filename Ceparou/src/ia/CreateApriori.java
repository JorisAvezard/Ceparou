package ia;

import java.util.ArrayList;

import ia.Engine;
import database.JdbcPersistence;

import weka.associations.Apriori;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * Cette classe crée les entrées de Apriori
 * @author nassima
 *
 */
public class CreateApriori {

	public CreateApriori() {

	}

	/**
	 * Creation of attributes
	 * @return
	 */
	public static ArrayList<Attribute> getAttribut() {
		JdbcPersistence jp = new JdbcPersistence();
		int longPaths = jp.longPath();

		FastVector attributes = new FastVector();
		ArrayList<String> valuesAttribute = new ArrayList<String>();
		valuesAttribute = jp.getPlace();
		for (int i = 0; i < longPaths; i++) {
			Attribute att = new Attribute(String.valueOf(i), valuesAttribute);

			attributes.addElement(att);

		}
		
//		for(int j=0; j<attributes.size();j++){
//			System.out.println(attributes.get(j));
//		}

		return attributes;

	}

	/**
	 * Creation of instances
	 * @return
	 * @throws Exception
	 */
	public static Instances getInstance() throws Exception {
		JdbcPersistence jp = new JdbcPersistence();
		int longPaths = jp.longPath();
		Engine en = new Engine();
		Instances dataSet;
		 FastVector<Attribute> attributes = (FastVector<Attribute>) getAttribut();
		dataSet = new Instances("Generate rules automatically", attributes, 0);
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		paths = en.getAllPAths();
		for (int k = 0; k < paths.size(); k++) {
//			System.out.println("k : " + k);
			Instance inst = new DenseInstance(longPaths);
			for (int h = 0; h < paths.get(k).size(); h++) {
//				System.out.println("h : " + h);
				inst.setValue((Attribute) attributes.get(h), paths.get(k).get(h));
			}
			dataSet.add(inst);
		}

//		System.out.println(dataSet);

		return dataSet;
	}

}
