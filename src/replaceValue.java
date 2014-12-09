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
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            nodeList.item(i).setTextContent(elementValue);
        }

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