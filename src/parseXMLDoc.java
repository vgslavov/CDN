import java.io.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class parseXMLDoc {
	public static void main(String[] arg) {
		
		//String xmlFile = "/home/tivakar/Desktop/Thesis/HL7/A/503.xml";

		String path = "/home/tivakar/Desktop/Thesis/HL7/Allfiles";

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		File file = null;

		for (int index = 0; index < listOfFiles.length; index++) {

			file = listOfFiles[index];
			
			if (file.exists()) {
				try {
					// Create a factory
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					// Use document builder factory
					DocumentBuilder builder = factory.newDocumentBuilder();
					// Parse the document
					Document doc = builder.parse(file.getPath());
					TransformerFactory tranFact = TransformerFactory
							.newInstance();
					Transformer transfor = tranFact.newTransformer();

					transfor.setOutputProperty(OutputKeys.METHOD, "xml");
					//System.out.println(transfor.getOutputProperty("standalone"));
					
					String nodeContent = null;
					String attribute = null;
					String value = null;

					boolean gotAttribute = false;
					boolean gotFirstQuoute = false;

					NodeList list = doc.getElementsByTagName("*");
					System.out.println("XML Elements: ");
					for (int i = 0; i < list.getLength(); i++) {
						// Get element
						Element element = (Element) list.item(i);
						NodeList n = element.getChildNodes();

						attribute = "";
						value = "";
						gotAttribute = false;
						gotFirstQuoute = false;

						if (n.getLength() == 1) {
							// System.out.println(element.getNodeName() + ":" +
							// element.getTextContent());

							nodeContent = element.getTextContent().trim();

							for (int j = 0; j < nodeContent.length(); j++) {

								if (!gotAttribute) {

									if (nodeContent.charAt(j) == '=') {

										gotAttribute = true;

									} else {

										attribute = attribute
												+ nodeContent.charAt(j);

									}
								} else if (!gotFirstQuoute) {

									if (nodeContent.charAt(j) == '"') {

										gotFirstQuoute = true;
									}

								} else {

									if (nodeContent.charAt(j) == '"') {

										element.setAttribute(attribute.trim()
												.replace(" ", ""), value.trim());
										element.setTextContent("");

										attribute = "";
										value = "";
										gotAttribute = false;
										gotFirstQuoute = false;
									} else {

										value = value + nodeContent.charAt(j);

									}

								}

							}

						}

					}

					//Output modifications
					Source src = new DOMSource(doc);
					Result dest = new StreamResult(new File(file.getPath()));
					transfor.transform(src, dest);
					
				} catch (Exception e) {
					System.err.println(e);
				}
			} else {
				System.out.print("File not found!");
			}
		}

	}
}