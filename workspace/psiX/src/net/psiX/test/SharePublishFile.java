package net.psiX.test;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.psiX.connection.PsiXConnector;

public class SharePublishFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file = new File(
				"/home/tivakar/Desktop/Thesis/HL7/Michaelsfiles/1.xml");

		PsiXConnector psiX = new PsiXConnector();

		if (file.exists()) {
			try {
				// publish
				psiX.publishDoc(file.getPath(), null, "http://"
						+ "192.168.1.201:8080"
						+ "/documents/Allfilesconverted-withoutduplication/"
						+ file.getName());
				// +file.getName());

				System.out.println("http://" + "192.168.1.201:8080"
						+ "/documents/Allfilesconverted-withoutduplication/"
						+ file.getName());
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

}
