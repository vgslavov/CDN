package net.cdn.functions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.cdn.Objects.FLOWRObject;
import net.cdn.Objects.XQueryObject;
import net.cdn.Objects.XQueryReturnObject;
import net.cdn.message.CDNMessage;
import net.cdn.utility.ClientHttpRequest;
import net.cdn.utility.RemoveStringCharacters;
import net.sf.saxon.Query;

public class ExecuteQueryOnPeers {

	public void executeQueriesOnAllPeers(XQueryObject xQueryObj) {

		String variable = null;
		String hostName = null;
		String pathName = null;
		String xQueryForHost = null;

		String ROOT = "ROOT";
		String TEMPROOT = "TempRoot";

		int pathNameCount;

		HashMap<String, HashMap<String, HashMap<String, String>>> allVarAllHostAllPaths = null;

		Iterator<String> iterAllVarAllHostAllPaths = null;
		Iterator<String> iterAllHost = null;
		Iterator<String> iterAllPath = null;

		FLOWRObject flowrObj = null;

		HashMap<String, HashMap<String, String>> allHostallPaths = null;
		HashMap<String, String> allPaths = null;
		HashMap<String, FLOWRObject> varNFLOWRObj = null;

		ArrayList<String> listOfFilesCreated = null;

		MergeXMLFiles mrg = new MergeXMLFiles();
		File file = null;

		allVarAllHostAllPaths = xQueryObj.getVarHostPath();
		iterAllVarAllHostAllPaths = allVarAllHostAllPaths.keySet().iterator();
		varNFLOWRObj = xQueryObj.getFlowrObjForXQuery();

		RemoveStringCharacters rmvStringChar = new RemoveStringCharacters();

		while (iterAllVarAllHostAllPaths.hasNext()) {
			variable = iterAllVarAllHostAllPaths.next();// Variable
			allHostallPaths = allVarAllHostAllPaths.get(variable);
			flowrObj = varNFLOWRObj.get(variable);
			file = new File(variable.replace("$", "") + ".xml");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			listOfFilesCreated = new ArrayList<String>();

			try {
				iterAllHost = allHostallPaths.keySet().iterator();
			} catch (Exception e) {
				iterAllHost = null;
			}
			xQueryForHost = "<" + ROOT + ">" + "{";

			if (iterAllHost != null) {
				while (iterAllHost.hasNext()) {
					hostName = iterAllHost.next();// Hostname
					allPaths = allHostallPaths.get(hostName);
					iterAllPath = allPaths.keySet().iterator();

					pathNameCount = 0;

					while (iterAllPath.hasNext()) {
						pathName = iterAllPath.next();

						// when return statement is present
						if (flowrObj.getsReturn() != null) {
							xQueryForHost = xQueryForHost
									+ " "
									+ "for"
									+ " "
									+ variable
									+ pathNameCount
									+ " "
									+ "in"
									+ " "
									+ "doc(\""
									+ rmvStringChar.removeFirstOccurenceIf(
											pathName, "/") + "\")"
									+ flowrObj.getsFor();
							if (flowrObj.getsWhere() != null) {
								xQueryForHost = xQueryForHost
										+ " "
										+ flowrObj.getsWhere().replace(
												variable,
												variable + pathNameCount);
							}

							xQueryForHost = xQueryForHost
									+ " "
									+ "return"
									+ " "
									+ "<"
									+ TEMPROOT
									+ ">"
									+ " "
									+ flowrObj.getsReturn().replace(variable,
											variable + pathNameCount);

							xQueryForHost = xQueryForHost + "</" + TEMPROOT
									+ ">";

							if (iterAllPath.hasNext()) {
								xQueryForHost = xQueryForHost + ",";

							}
						}
						pathNameCount++;
					}

					xQueryForHost = xQueryForHost + "}" + "</" + ROOT + ">";

					try {
						file = new File(variable.replace("$", "") + hostName
								+ ".xml");
						BufferedWriter out = new BufferedWriter(new FileWriter(
								file.getAbsolutePath()));
						out.write(executeQueriesOnPeer(hostName, xQueryForHost));
						out.close();
						listOfFilesCreated.add(variable.replace("$", "")
								+ hostName + ".xml");
					} catch (IOException e) {
						System.out.println("Exception" + e.getMessage());
					}

					Iterator<String> iterListOfFilesCreated = listOfFilesCreated
							.iterator();

					while (iterListOfFilesCreated.hasNext()) {

						mrg.mergeTwoFiles("ROOT", variable.replace("$", "")
								+ ".xml",
								(String) iterListOfFilesCreated.next(),
								variable.replace("$", "") + ".xml");
						if (file != null && file.exists()) {
							if (file.delete())
								System.out.println("** Deleted ");
							else
								System.out.println("Failed to delete");

						}
					}
					System.out.println("");
				}
			}

		}
	}

	public String executeQueriesOnPeer(String hostUrl, String xQuery) {

		HttpClient httpclient = new DefaultHttpClient();

		String output = "";
		StringBuilder outputBuilder = new StringBuilder(output);

		System.out.println("############################################");
		System.out.println("# XQuery for Host <<< " + hostUrl + " >>> #");
		System.out.println("############################################");
		System.out.println("");
		System.out.println(xQuery);
		System.out.println("");

		try {
			//
			// List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			// formparams.add(new BasicNameValuePair("xquery", xQuery));
			// UrlEncodedFormEntity entity = new
			// UrlEncodedFormEntity(formparams, "UTF-8");
			// HttpPost httppost = new HttpPost("http://" + hostUrl +
			// "/XquerySaxon/Xquery");
			// httppost.setEntity(entity);
			//
			// ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
			// public byte[] handleResponse(
			// HttpResponse response) throws ClientProtocolException,
			// IOException {
			// HttpEntity entity = response.getEntity();
			// if (entity != null) {
			// return EntityUtils.toByteArray(entity);
			// } else {
			// return null;
			// }
			// }
			// };
			//
			// byte[] response = httpclient.execute(httppost, handler);
			//
			// output = new String(response);

			// servlet URL
			URL url = new URL("http://" + hostUrl + "/XquerySaxon/Xquery");

			// open a connection to the servlet
			URLConnection servletConnection = url.openConnection();

			// prepare for both input and output
			servletConnection.setDoInput(true);
			servletConnection.setDoOutput(true);

			// don't use a cached version of URL connection
			servletConnection.setUseCaches(false);
			servletConnection.setDefaultUseCaches(false);

			// set the content type to indicate that we're sending binary data
			servletConnection.setRequestProperty("Content-Type",
					"application/x-java-serialized-object");

			// String input = "vettri pettraan";

			CDNMessage cdnMsg = new CDNMessage();

			cdnMsg.setMessage(xQuery);

			// XQueryReturnObject data_in = new XQueryReturnObject();
			// data_in.setName("vettri");
			// Object here = (Object) data_in;

			Object sendCdnMsg = (Object) cdnMsg;

			// create an output stream
			OutputStream out = servletConnection.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);

			// write the serialized data object to the output stream
			oos.writeObject(sendCdnMsg);
			oos.flush();
			oos.close();

			// get input streams on servlet
			InputStream is = servletConnection.getInputStream();

			if (servletConnection.getContentLength() > 0) {
				
				int c;
				int i = servletConnection.getContentLength();
				while (((c = is.read()) != -1) && (--i > 0)) {
					
					outputBuilder.append((char) c);
					//System.out.print((char) c);
				}
				is.close();
			} else {

			}
			
			output = outputBuilder.toString();
			
			System.out.println("############ <<< OUTPUT >>> #############");
			System.out.println(output);
			System.out.println("");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;

	}

	public String executeQueryLocal(XQueryObject xQueryObj) {

		Xq2MaximalXp xq2MaxXpath = new Xq2MaximalXp();
		ArrayList<String> xQuerySplits = xq2MaxXpath.getEachLines(xQueryObj);
		HashMap<String, String> hMapVarNMaxXPath = xQueryObj
				.gethMapVarNMaxXpath();
		Iterator<String> vars = hMapVarNMaxXPath.keySet().iterator();
		FileWriter fWriter = null;
		BufferedWriter bWriter = null;
		File file = null;

		String localXQuery = "";
		String temp = null;
		String variable = null;

		while (vars.hasNext()) {
			variable = vars.next();
			localXQuery = localXQuery + " " + "for" + " " + variable + " "
					+ "in" + " " + "doc('" + variable.replace("$", "") + ".xml"
					+ "')" + "/ROOT/TempRoot";
		}
		Iterator<String> arrayXQuerySplits = xQuerySplits.iterator();

		while (arrayXQuerySplits.hasNext()) {
			temp = arrayXQuerySplits.next();
			if (!temp.contains("for")) {
				localXQuery = localXQuery + " " + temp;
			}
		}

		localXQuery = "count(" + localXQuery + ")";
		
		try {
			file = new File("query.txt");
			file.createNewFile();
			fWriter = new FileWriter("query.txt");
			bWriter = new BufferedWriter(fWriter);

			bWriter.write(localXQuery);
			bWriter.close();
			fWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] args = { "-q:" + "query.txt" , "-o:" + "tivakar.txt"};

		Query saxon = new Query();

		System.out.println("################");
		System.out.println("# Local XQuery #");
		System.out.println("################");
		System.out.println("");
		System.out.println(localXQuery);
		System.out.println("");
		System.out.println("##### <<< Final Output >>> ####");
		saxon.doQuery(args, "java net.sf.saxon.Query");

		return null;

	}
}
