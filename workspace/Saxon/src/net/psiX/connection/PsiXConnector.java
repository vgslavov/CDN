package net.psiX.connection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import net.cdn.Objects.XQueryObject;
import net.cdn.utility.ClientHttpRequest;
import net.psiX.objects.PsiXObject;

public class PsiXConnector {

	/**
	 * 
	 * @author Tivakar
	 * @param xmlfile
	 * @param xmldtd
	 * @param docid
	 * @return
	 * 
	 *         This method publishDoc is used for publishing the document along
	 *         with document id
	 */
	public static String publishDoc(String xmlfile, String xmldtd, String docid) {
		ClientHttpRequest clientReq = null;
		String output = "";
		try {
			clientReq = new ClientHttpRequest(
					"http://vortex.sce.umkc.edu/cgi-bin/raopr/publish.cgi");
			 //"ec2-107-22-67-132.compute-1.amazonaws.com/cgi-bin/ec2-user/publish.cgi");"

			clientReq.setParameter("xmlfile", new File(xmlfile));
			clientReq.setParameter("xmldtd", new File(xmldtd));
			clientReq.setParameter("docid", docid);

			InputStream input = clientReq.post();

			int data = input.read();
			while (data != -1) {
				output = output + (char) data;
				data = input.read();
			}
			input.close();
			System.out.println(output);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;

	}

	/**
	 * @author Tivakar
	 * @param xmlpath
	 * @param xmldtd
	 * @param replica
	 * @return
	 * 
	 *         locateDoc method returns the URLs(Where document resides) and the
	 *         document IDs
	 * 
	 */

	public String locateDoc(String xmlpath, String xmldtd, String replica) {
		// TODO Auto-generated method stub
		ClientHttpRequest clientReq = null;
		String output = "";
		try {
			/*
			 * Httprequest to psiX website
			 */
			clientReq = new ClientHttpRequest(
					"http://vortex.sce.umkc.edu/cgi-bin/raopr/query.cgi");

			clientReq.setParameter("xmlpath", xmlpath);
			clientReq.setParameter("xmldtd", new File(xmldtd));
			clientReq.setParameter("replica", replica);

			InputStream input = clientReq.post();

			int data = input.read();
			while (data != -1) {
				output = output + (char) data;
				data = input.read();
			}
			input.close();
			// System.out.println(output);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;

	}

	/**
	 * @author tivakar
	 * @param htmlText
	 * @return
	 * 
	 *         retrieveURLnDocID method accepts htmlText string and seperates
	 *         url and docid , adds it to PsiXObject and then to ArrayList
	 */
	public ArrayList<PsiXObject> retrieveURLnDocID(String htmlText) {
		PsiXObject psiXObject = null;
		String token1;
		String urlTrimmed;
		StringTokenizer stToken1 = new StringTokenizer(htmlText);
		StringTokenizer stToken2 = null;
		StringTokenizer stToken3 = null;

		String urlString;
		String path;
		URL url = null;

		ArrayList<PsiXObject> psiXObjects = new ArrayList<PsiXObject>();

		while (stToken1.hasMoreElements()) {
			token1 = stToken1.nextElement().toString();
			if (token1.equals("/>[Docid:")) {
				urlString = stToken1.nextElement().toString();
				urlTrimmed = urlString.substring(0, urlString.length() - 3);

				System.out.println(urlTrimmed);

				path = "";

				psiXObject = new PsiXObject();

				if (urlTrimmed.contains("http")) {
					try {
						url = new URL(urlTrimmed);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Invalid URL");
					}
					psiXObject.setHostname(url.getHost());
					psiXObject.setPath(url.getPath());
				} else {
					stToken3 = new StringTokenizer(urlTrimmed, "/");
					if (stToken3.hasMoreTokens()) {
						psiXObject.setHostname(stToken3.nextToken());
					}
					while (stToken3.hasMoreTokens()) {
						path = path + "/" + stToken3.nextToken();
					}
					psiXObject.setPath(path);
				}

				psiXObjects.add(psiXObject);

				// stToken2 = new StringTokenizer(url.getPath(), ":");
				//
				// if (stToken2.hasMoreElements() && stToken2.countTokens() ==
				// 2) {
				// psiXObject.setPath(stToken2.nextElement().toString());
				// psiXObject.setDocId(stToken2.nextElement().toString());
				// psiXObjects.add(psiXObject);
				// }

			}
		}

		return psiXObjects;

	}

	/**
	 * @author tivakar
	 * @param htmlText
	 * @return
	 * 
	 *         retrieveHostAndPath method accepts htmlText string and seperates
	 *         it into Hostname and path , adds it to PsiXObject and then to
	 *         ArrayList
	 */
	public HashMap<String, HashMap<String, String>> retrieveHostAndPath(
			String htmlText) {
		PsiXObject psiXObject = null;
		String token1;
		String urlTrimmed;
		StringTokenizer stToken1 = new StringTokenizer(htmlText);
		StringTokenizer stToken2 = null;
		StringTokenizer stToken3 = null;

		String urlString;

		URL url = null;
		String host = null;
		String path;

		HashMap<String, HashMap<String, String>> allHostAllPaths = null;
		HashMap<String, String> allPaths;

		while (stToken1.hasMoreElements()) {
			allPaths = null;

			token1 = stToken1.nextElement().toString();
			if (token1.equals("/>[Docid:")) {
				urlString = stToken1.nextElement().toString();
				urlTrimmed = urlString.substring(0, urlString.length() - 3);

				path = "";

				psiXObject = new PsiXObject();
				System.out.println("URL: " + urlTrimmed);
				if (urlTrimmed.contains("http")) {
					try {
						url = new URL(urlTrimmed);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Invalid URL");
					}
					// When allHostAllPaths != null
					if (allHostAllPaths != null) {
						allPaths = allHostAllPaths.get(url.getHost());
						if (allPaths != null) {
							if (allPaths.get(url.getPath()) == null) {
								allPaths.put(url.getPath(), "1");
							}
						} else {
							allPaths = new HashMap<String, String>();
							allPaths.put(url.getPath(), "1");
						}
						allHostAllPaths.put(url.getHost(), allPaths);
					} else {
						// When allHostAllPath == null
						allHostAllPaths = new HashMap<String, HashMap<String, String>>();
						allPaths = new HashMap<String, String>();
						allPaths.put(url.getPath(), "1");
						allHostAllPaths.put(url.getHost(), allPaths);
					}
					host = url.getHost();
					path = url.getPath();
					psiXObject.setHostname(host);
					psiXObject.setPath(path);
				} else {
					stToken3 = new StringTokenizer(urlTrimmed, "/");
					if (stToken3.hasMoreTokens()) {
						host = stToken3.nextToken();
						// When allHostAllPaths != null
						if (allHostAllPaths != null) {
							allPaths = allHostAllPaths.get(host);
							while (stToken3.hasMoreTokens()) {
								path = path + "/" + stToken3.nextToken();
							}
							if (allPaths != null) {
								if (allPaths.get(path) == null) {
									allPaths.put(path, "1");
								}
							} else {
								allPaths = new HashMap<String, String>();
								allPaths.put(path, "1");
							}
							allHostAllPaths.put(host, allPaths);
						} else {
							// When allHostAllPath == null
							while (stToken3.hasMoreTokens()) {
								path = path + "/" + stToken3.nextToken();
							}
							allHostAllPaths = new HashMap<String, HashMap<String, String>>();
							allPaths = new HashMap<String, String>();
							allPaths.put(path, "1");
							allHostAllPaths.put(host, allPaths);
						}
					}
				}

				// System.out.println(" 	Host: " + host + " 	Path: " + path);

				// stToken2 = new StringTokenizer(url.getPath(), ":");
				//
				// if (stToken2.hasMoreElements() && stToken2.countTokens() ==
				// 2) {
				// psiXObject.setPath(stToken2.nextElement().toString());
				// psiXObject.setDocId(stToken2.nextElement().toString());
				// psiXObjects.add(psiXObject);
				// }

			}
		}

		return allHostAllPaths;

	}

	/**
	 * @author Tivakar
	 * @method getHostNPathForVarLocateMaxXpath()
	 * @description When all variables with maximal XPath are given, the method
	 *              returns Host name and Path name for every variable Path
	 *              names are grouped for a Host name and Host Name are grouped
	 *              for a variable
	 * @return HashMap<variable, HashMap<Hostname,ArrayList<pathname>>
	 */

	public XQueryObject getHostNPathForVarLocateMaxXpath(XQueryObject xQueryObj) {

		HashMap<String, HashMap<String, String>> allHostAllPaths = null;
		HashMap<String, HashMap<String, HashMap<String, String>>> allVarAllHostAllPaths = null;

		HashMap<String, String> hMapVarNMaxXpath = xQueryObj
				.gethMapVarNMaxXpath();

		Iterator<String> iterHmapVarNMaxXpath = hMapVarNMaxXpath.keySet()
				.iterator();

		allVarAllHostAllPaths = new HashMap<String, HashMap<String, HashMap<String, String>>>();

		System.out.println("###########################");
		System.out.println("# LOCATION DOC USING psiX #");
		System.out.println("###########################");
		
		//For windows
		//String xmldtd = "\\\\kc.umkc.edu\\kc-users\\home\\t\\tk27f\\Desktop\\Thesis\\DTDs\\actors.xml";
		
		//For Linux
		String xmldtd = "/home/tivakar/Desktop/Thesis/DTD files/actors.dtd/actors.dtd";

		while (iterHmapVarNMaxXpath.hasNext()) {

			String keyVariable = (String) iterHmapVarNMaxXpath.next();
			String valueMaxXpath = hMapVarNMaxXpath.get(keyVariable);

			System.out.println("######################");
			System.out.println("# VARIABLE " + keyVariable + " #");
			System.out.println("######################");

			// allHostAllPaths = retrieveHostAndPath(locateDoc(valueMaxXpath,
			// "/home/tivakar/Desktop/Thesis/actors.dtd", "1"));
						
//			allHostAllPaths = retrieveHostAndPath(locateDoc(
//					valueMaxXpath,
//					xmldtd,
//					"1"));
			
//			allHostAllPaths = retrieveHostAndPath("/>[Docid: http://vortex.sce.umkc.edu/psix/XMLDocs/actors.dtd/actors.dtd.1.xmlkkk");
			
//			allHostAllPaths = retrieveHostAndPath("/>[Docid: http://192.168.1.204:8080/psix/XMLFile/Actor/actors.dtd.1.xmlkkk");
			allHostAllPaths = retrieveHostAndPath("/>[Docid: http://192.168.1.204:8080/psix/XMLFile/allfiles/1_2.xmlkkk");
			
			allVarAllHostAllPaths.put(keyVariable, allHostAllPaths);
			System.out.println("");
		}
		xQueryObj.setVarHostPath(allVarAllHostAllPaths);

		System.out
				.println("******************************************************************\n");

		return xQueryObj;

	}
	
	public static void main(String args[]){
		String xmlfile = args[0];
		String xmldtd = args[1];
		String docid = args[2];
		
		publishDoc(xmlfile, xmldtd, docid);
	}

}
