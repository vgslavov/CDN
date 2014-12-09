import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class RemoveStopWords {
	public static void main(String args[]) throws IOException {
		// The folder path where the xml files reside is given as an argument
		String path_input = "/home/tk27f/Desktop/Thesis/Thesis_datas/out";
		String path_stopwords = "/home/tk27f/Desktop/workspace/FileOperations/bin/stopwords.txt";
		String path_output="/home/tk27f/Desktop/Thesis/Thesis_datas/stopremove2";
			
		File folder = new File(path_input);
		File[] listOfFiles = folder.listFiles();
		File file = null;

		for (int index = 0; index < listOfFiles.length; index++) {
			file = listOfFiles[index];
			String fileName = file.getName();
			// System.out.println("Retrieved File Name" + fileName);
			if (file.exists()) {
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory
							.newDocumentBuilder();
					Document doc = docBuilder.parse(file.getPath());

					NodeList listOfText = doc.getElementsByTagName("text");
					int totalText = listOfText.getLength();
					// File path of Stop words is given as argument
					FileReader fileReader = new FileReader(path_stopwords);
					BufferedReader br = new BufferedReader(fileReader);
					HashMap map = new HashMap();
					String wrd;
					String[] newS = new String[totalText];
					/* Storing all the stop words from the file into map with
					 the 'key' having the same name as word */
					while ((wrd = br.readLine()) != null) {
						map.put(wrd, wrd);
					}
					for (int i = 0; i < totalText; i++) {
						newS[i] = "";
						Node element = listOfText.item(i);
						String content = element.getTextContent();
						StringTokenizer st2 = new StringTokenizer(content);
						while (st2.hasMoreTokens()) {
							String ss = st2.nextToken();
							/* Checking whether the 'Text' content has any stop
							 words. If the stop word is present it is not
							 appended to the text thereby removing the stop
							 words*/
							if (map.get(ss.toLowerCase()) == null) {
								// sb.append(ss);
								newS[i] += ss + " ";
							}
						}
						System.out.println("///////" + newS[i]);
						/* The content of the element 'Text' is being replaced
						 with the new one wherein stop words are removed */
						element.setTextContent(newS[i]);
					}
					// Forming the new xml file
					TransformerFactory tranFact = TransformerFactory
							.newInstance();
					Transformer transformer = tranFact.newTransformer();
					transformer.setOutputProperty(OutputKeys.METHOD, "xml");

					StringWriter sw = new StringWriter();
					// Folder to which the new xml file is being written
					StreamResult result = new StreamResult(new File(path_output
							+ "/" + file.getName()));

					DOMSource source = new DOMSource(doc);
					transformer.transform(source, result);
					String xmlString = sw.toString();

					OutputStream f0;
					byte buf[] = xmlString.getBytes();
					System.out.println(index);
					f0 = new FileOutputStream(fileName);
					for (int i = 0; i < buf.length; i++) {
						f0.write(buf[i]);
					}
					f0.close();
				} catch (SAXParseException err) {
					System.out.println("** Parsing error" + ", line "
							+ err.getLineNumber() + ", uri "
							+ err.getSystemId());
					System.out.println(" " + err.getMessage());

				} catch (SAXException e) {
					Exception x = e.getException();
					((x == null) ? e : x).printStackTrace();

				} catch (Throwable t) {
					t.printStackTrace();
				}
			} else {
				System.out.print("File not found!");
			}
		}
	}
}