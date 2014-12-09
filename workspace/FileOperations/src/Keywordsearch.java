import java.io.*;
import java.util.StringTokenizer;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Keywordsearch {
	public static void main(String[] arg) {
		String path = "/home/tk27f/Desktop/Thesis/Thesis_datas/stopremove2";
		String path_out = "/home/tk27f/Desktop/Thesis/Thesis_datas/keyword3";

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

					String titleValue = null;
					String textValue = null;

					StringTokenizer strTknzr = null;

					Element element = null;
					Element title = null;
					Element text = null;

					Element childElement = null;

					NodeList list = doc.getElementsByTagName("section");

					for (int i = 0; i < list.getLength(); i++) {
						// Get element
						element = (Element) list.item(i);

						title = (element.getElementsByTagName("title") != null ? (Element) element
								.getElementsByTagName("title").item(0) : null);
						text = (element.getElementsByTagName("text") != null ? (Element) element
								.getElementsByTagName("text").item(0) : null);

						if (title != null) {
							titleValue = title.getTextContent();

							if (text != null) {
								textValue = text.getTextContent();

								strTknzr = new StringTokenizer(textValue);

								if (titleValue.contains("Allergies")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<AL>"
										// + strTknzr.nextToken() + "</AL>";
										childElement = doc.createElement("AL");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);

									}

								} else if (titleValue
										.contains("Discharge Date")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue +
										// "<DDate>" + strTknzr.nextToken() +
										// "</DDate>";
										childElement = doc
												.createElement("DDate");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("Discharge Diagnosis")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue +
										// "<DDiag>" + strTknzr.nextToken() +
										// "</DDiag>";
										childElement = doc
												.createElement("DDiag");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("Discharge Disposition")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue +
										// "<DDisp>" + strTknzr.nextToken() +
										// "</DDisp>";
										childElement = doc
												.createElement("DDisp");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("History of  Present Illness")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<HPI>"
										// + strTknzr.nextToken() + "</HPI>";
										childElement = doc.createElement("HPI");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("Hospital Course")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<HC>"
										// + strTknzr.nextToken() + "</HC>";
										childElement = doc.createElement("HC");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("Past Medical History")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<PMH>"
										// + strTknzr.nextToken() + "</PMH>";
										childElement = doc.createElement("PMH");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue
										.contains("Past Surgical History")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<PSH>"
										// + strTknzr.nextToken() + "</PSH>";
										childElement = doc.createElement("PSH");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								} else if (titleValue.contains("Physical Exam")) {

									text.setTextContent("");

									while (strTknzr.hasMoreTokens()) {

										// newTextValue = newTextValue + "<PE>"
										// + strTknzr.nextToken() + "</PE>";
										childElement = doc.createElement("PE");
										childElement.setTextContent(strTknzr
												.nextToken());
										text.appendChild(childElement);
									}

								}

								// text.setTextContent(newTextValue);
							}
						}

						System.out.println(element.getNodeName() + ":");
						System.out.println(title.getNodeName() + ":"
								+ titleValue);
						System.out
								.println(text.getNodeName() + ":" + textValue);

					}

					Source src = new DOMSource(doc);
					Result dest = new StreamResult(
							new File(path_out + "/" + file.getName()));
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