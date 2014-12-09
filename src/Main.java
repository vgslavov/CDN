// vsfgd: most of this code is copied from stackoverflow.com
package replacevalue;

import java.io.*;
import java.util.Random;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.xml.sax.InputSource;

public class Main {

    public static void main(String[] args) {
        String inputXmlFilePathName;
        String outputXmlFilePathName;
        String tmpXmlFilePathName;
        //String inputXmlDir = "/home/vsfgd/hl7/test";
        String inputXmlDir = "/home/vsfgd/hl7/HL7V3Files";
        String outputXmlDir = "/home/vsfgd/hl7/out";
        String tmpXmlDir = "/home/vsfgd/hl7/tmp";
        String elementValue = "junk";
        String elementXpathExpression;
        String filewext, filewoext;
        int newperfile = 300;
        boolean replaceAllFoundElements = true;

		File folder = new File(inputXmlDir);
		File[] listOfFiles = folder.listFiles();
        System.out.println("# of input files: "+listOfFiles.length);

        /*
        // change code attribute
        elementXpathExpression = "//code/@code";
		for (int index = 0; index < listOfFiles.length; index++) {
			inputXmlFilePathName = listOfFiles[index].getAbsolutePath();
            for (int j = 0; j < newperfile; j++) {
                filewext = listOfFiles[index].getName();
                filewoext = filewext.substring(0, filewext.lastIndexOf('.'));
                tmpXmlFilePathName = tmpXmlDir+"/"+filewoext+"-"+j+".xml";
                //System.out.print(filewoext+"-"+j+": ");
                // generate random values for all code elements
                replaceElementValueRand(inputXmlFilePathName, tmpXmlFilePathName, elementXpathExpression, elementValue, replaceAllFoundElements);
            }
		}
        */

        folder = new File(tmpXmlDir);
		listOfFiles = folder.listFiles();
        System.out.println("# of tmp files: "+listOfFiles.length);

        // change ID nodes
        elementXpathExpression = "//ID";
        for (int index = 0; index < listOfFiles.length; index++) {
			tmpXmlFilePathName = listOfFiles[index].getAbsolutePath();
            filewext = listOfFiles[index].getName();
            outputXmlFilePathName = outputXmlDir+"/"+filewext;
            elementValue = Integer.toString(index);
            replaceElementValue(tmpXmlFilePathName, outputXmlFilePathName, elementXpathExpression, elementValue, replaceAllFoundElements);
		}
    }

    /**
     * Takes an input XML file, replaces the text value of the node specified by an XPath parameter, and writes a new
     * XML file with the updated data.
     *
     * @param inputXmlFilePathName
     * @param outputXmlFilePathName
     * @param elementXpath
     * @param elementValue
     * @param replaceAllFoundElements
     */

    public static void replaceElementValue(final String inputXmlFilePathName,
                               final String outputXmlFilePathName,
                               final String elementXpathExpression,
                               final String elementValue,
                               final boolean replaceAllFoundElements)
    {
        try
        {
            // get the template XML as a W3C Document Object Model which we can later write back as a file
            InputSource inputSource = new InputSource(new FileInputStream(inputXmlFilePathName));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            Document document = documentBuilderFactory.newDocumentBuilder().parse(inputSource);

            // create an XPath expression to access the element's node
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression xpathExpression = xpath.compile(elementXpathExpression);

            // get the node(s) which corresponds to the XPath expression and replace the value
            Object xpathExpressionResult = xpathExpression.evaluate(document, XPathConstants.NODESET);
            if (xpathExpressionResult == null)
            {
                throw new RuntimeException("Failed to find a node corresponding to the provided XPath.");
            }
            NodeList nodeList = (NodeList) xpathExpressionResult;
            if ((nodeList.getLength() > 1) && !replaceAllFoundElements)
            {
                throw new RuntimeException("Found multiple nodes corresponding to the provided XPath and multiple replacements not specified.");
            }
            //System.out.print("replaced: "+nodeList.getLength()+", codes: ");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                nodeList.item(i).setTextContent(elementValue);
            }
            //System.out.println();

            // prepare the DOM document for writing
            Source source = new DOMSource(document);

            // prepare the output file
            File file = new File(outputXmlFilePathName);
            Result result = new StreamResult(file);

            // write the DOM document to the file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);

        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to replace the element value.", ex);
        }
    }

    /**
     * Takes an input XML file, replaces the text value of the node specified by an XPath parameter, and writes a new
     * XML file with the updated data.
     *
     * @param inputXmlFilePathName
     * @param outputXmlFilePathName
     * @param elementXpath
     * @param elementValue
     * @param replaceAllFoundElements
     */

    public static void replaceElementValueRand(final String inputXmlFilePathName,
                               final String outputXmlFilePathName,
                               final String elementXpathExpression,
                               final String elementValue,
                               final boolean replaceAllFoundElements)
    {
        int pick1, pick2;
        String newValue;
        Random rand = new Random();

        try
        {
            // get the template XML as a W3C Document Object Model which we can later write back as a file
            InputSource inputSource = new InputSource(new FileInputStream(inputXmlFilePathName));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            Document document = documentBuilderFactory.newDocumentBuilder().parse(inputSource);

            // create an XPath expression to access the element's node
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression xpathExpression = xpath.compile(elementXpathExpression);

            // get the node(s) which corresponds to the XPath expression and replace the value
            Object xpathExpressionResult = xpathExpression.evaluate(document, XPathConstants.NODESET);
            if (xpathExpressionResult == null)
            {
                throw new RuntimeException("Failed to find a node corresponding to the provided XPath.");
            }
            NodeList nodeList = (NodeList) xpathExpressionResult;
            if ((nodeList.getLength() > 1) && !replaceAllFoundElements)
            {
                throw new RuntimeException("Found multiple nodes corresponding to the provided XPath and multiple replacements not specified.");
            }
            //System.out.print("replaced: "+nodeList.getLength()+", codes: ");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                pick1 = rand.nextInt(100000);
                pick2 = rand.nextInt(10);
                newValue = Integer.toString(pick1)+"-"+Integer.toString(pick2);
                //System.out.print(newValue+", ");
                nodeList.item(i).setTextContent(newValue);
            }
            //System.out.println();

            // prepare the DOM document for writing
            Source source = new DOMSource(document);

            // prepare the output file
            File file = new File(outputXmlFilePathName);
            Result result = new StreamResult(file);

            // write the DOM document to the file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to replace the element value.", ex);
        }
    }
}


