package net.cdn.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.cdn.Objects.XQueryObject;
import net.psiX.connection.PsiXConnector;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// String xQuery =
		// "for $x in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor for $y in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc2.xml')/PersonName where $x/Name/FirstName = $y/GivenName and $x/Name = \"tivakar\" and $y/GivenName = \"stud\" and $x/price > 50 return $x/Filmograph/Movie/Title";
		// String xQuery =
		// "for $x in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor for $y in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor for $z in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor  return {$x/Filmography/Movie/Title} {$x/Filmography} {$y/Movie/Title} {$y/some} {$y/Movie/Title/one} {$y/Movie/song} {$y/hee/Movie} {$y/movie/tiva/ra/stupid} {$z/tivakar} {$y/t/hey/this/that} {$x/Filmography/tew}";
		// String xQuery =
		// "for $x in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor for $y in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor for $z in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc1.xml')/W4F_DOC/Actor  return {$y/Movie/Title} {$y/some} {$y/Movie/Title/one} {$y/Movie/song} {$y/hee/Movie} {$y/movie/tiva/ra/stupid} {$y/t/hey/this/that}";
//		String xQuery = "for $x in collection(\"something\")/W4F_DOC/Actor for $y in collection(\"something\")/W4F_DOC/Actor where  $x/Filmography/Movie/Title = $y/Filmography/Movie/Title return $x/Name/FirstName";
		
		String xQuery = "for $x in collection(\"cdn\")/ClinicalDocument return $x//Patient";
		
		// String xQuery =
		// "for $x in doc('psix/XMLDocs/actors.dtd/actors.dtd.1.xml') return $x";
		// Creating XQueryObject - which contains all the information

		System.out.println("##########");
		System.out.println("# XQUERY #");
		System.out.println("##########\n");

		System.out.println(xQuery);

		XQueryObject xQueryObj = new XQueryObject();
		xQueryObj.setxQuery(xQuery);// Setting original XQuery

		Xq2MaximalXp xq2MaxXpath = new Xq2MaximalXp();
		// Parse Xquery
		ArrayList<String> xQuerySplits = xq2MaxXpath.getEachLines(xQueryObj);
		// Get Maximal Xpath
		HashMap<String, String> hMapVarNMaxXpath = xq2MaxXpath
				.getMaxXpath(xQuerySplits);
		
		System.out.println("\n**************************************************************\n");
		Iterator<String> varIter = hMapVarNMaxXpath.keySet().iterator();
        String var = null;
		while (varIter.hasNext()) {
			var = varIter.next();
			System.out.println("##################################");
			System.out.println("# MAXIMAL XPATH FOR VARIABLE "+ var+" #");
			System.out.println("##################################\n");
			
			System.out.println(hMapVarNMaxXpath.get(var));
			System.out.println("");
		}
		
		System.out.println("**************************************************************\n");

		// Set in XQuery Object
		xQueryObj.sethMapVarNMaxXpath(hMapVarNMaxXpath);

		// Creating PsiXConnector Object to connect to psiX and getting
		// hostnames
		// and pathnames
		PsiXConnector psiXConn = new PsiXConnector();
		xQueryObj = psiXConn.getHostNPathForVarLocateMaxXpath(xQueryObj);

		// Forming queries for peers
		QueriesForPeers qP = new QueriesForPeers();
		xQueryObj = qP.getFLOWRObjforVariablesXQuery(xQueryObj);

		// Forming query strings for peers
		ExecuteQueryOnPeers exeQPeers = new ExecuteQueryOnPeers();
		exeQPeers.executeQueriesOnAllPeers(xQueryObj);

		exeQPeers.executeQueryLocal(xQueryObj);

	}

}
