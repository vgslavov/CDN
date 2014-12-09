package net.cdn.connection;

import java.io.IOException;
import java.io.InputStream;

import net.cdn.utility.ClientHttpRequest;

public class PeerHandler {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		executeQuery("/Feedback", "psix/XMLDocs/Feedback-1_1.dtd/Feedback-1_1.dtd.1.xml");
	}
	
	public static String executeQuery(String query,String xmlDocumentPath) {
		// TODO Auto-generated method stub
		ClientHttpRequest clientReq = null;
		String output = "";
		try {
			clientReq = new ClientHttpRequest(
					"http://vortex.sce.umkc.edu/cgi-bin/raopr/xquery.cgi");

			clientReq.setParameter("xmlpath", query);
			clientReq.setParameter("xmlfile", xmlDocumentPath);

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

}
