package net.psiX.test;
import java.io.FileNotFoundException;

import net.psiX.connection.PsiXConnector;

/*
 * @(#)HttpExample.java
 *
 * Copyright (c) JSCAPE LLC.
 *
 * This software is the confidential and proprietary information of
 * JSCAPE. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with JSCAPE.
 */


public class HttpExample {

	public static void main(String[] args) throws FileNotFoundException {
		PsiXConnector psiX = new PsiXConnector();
		psiX.publishDoc(
				"/home/tivakar/Desktop/Thesis/HL7/Allfiles/1_1.xml",
				null,
				"tiv");
//		psiX.publishDoc(
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/dblp.xml",
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/dblp.dtd",
//				"http://vortex.sce.umkc.edu/psix/dblp.xml;2608");	
//		psiX.publishDoc(
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/region.xml",
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/region.dtd",
//				"http://vortex.sce.umkc.edu/psix/region.xml;2609");
//		psiX.publishDoc(
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/treebank.xml",
//				"/home/tivakar/Desktop/Thesis/XML and DTD examples/treebank.dtd",
//				"http://vortex.sce.umkc.edu/psix/treebank.xml;2610");
//		psiX.retrieveURLnDocID(psiX.locateDoc(
//				"Actor",
//				"/home/tivakar/Desktop/actors.dtd",
//				"1"));
		
//		String machine ="vortex";
//		String machineName = "vortex.sce.umkc.edu";
//		String location = "/home/tivakar/Desktop/Thesis/XMLGenerator/XMLFiles/vortex/";
//		int count = 1;
//		
//		for(int i=1;i <= count; i++){
//			psiX.publishDoc(
//			location+machine+"."+i+".xml",
//			"/home/tivakar/Desktop/Thesis/DTDGenerator/SampleCDADocument.dtd",
//			machineName+"/psix/XMLDocs/cda/"+machine+"."+i+".xml");
//		}
//		
	}
}
