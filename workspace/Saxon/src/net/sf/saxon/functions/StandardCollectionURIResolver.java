package net.sf.saxon.functions;

import net.sf.saxon.CollectionURIResolver;
import net.sf.saxon.Controller;
import net.sf.saxon.event.ParseOptions;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Stripper;
import net.sf.saxon.expr.*;
import net.sf.saxon.om.*;
import net.sf.saxon.pattern.NodeKindTest;
import net.sf.saxon.trans.Err;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AnyURIValue;
import net.sf.saxon.value.ObjectValue;
import net.sf.saxon.value.TextFragmentValue;
import net.sf.saxon.value.Whitespace;
import org.xml.sax.XMLReader;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class implements the default collection URI Resolver.
 * <p>
 * This supports two implementations of collections. If the URI supplied uses the "file:/" scheme, and the
 * file that is referenced is a directory, then the collection is the set of files in that directory. Query parameters
 * may be included in the URI:
 * <ul>
 * <li><p>recurse=yes|no controls whether the directory is scanned recursively; </p></li>
 * <li><p>strip-space=yes|no  determines whether whitespace text nodes are stripped from the selected documents; </p></li>
 * <li><p>validation=strict|lax|preserve|strip determines whether schema validation is applied;</p></li>
 * <li><p>select=pattern determines which files in the directory are selected.</p></li>
 * <li><p>on-error=fail|warn|ignore determines the action taken if processing of a file fails</p></li>
 * <li><p>parser=qualified.class.name selects the parser (XMLReader) to be used to read the files</p></li>
 * </ul>
 * <p>
 * Otherwise, the resolver attempts to dereference the URI to obtain a catalog file. This is an XML file
 * containing a list of documents, in the format: </p>
 * <pre>
 * &lt;collection>
 *   &lt;doc href="doc1.xml"/>
 *   &lt;doc href="doc2.xml"/>
 * &lt;/collection>
 * </pre>
 */

public class StandardCollectionURIResolver implements CollectionURIResolver {

    /**
     * Resolve a URI.
     *
     * @param href The relative URI of the collection. This corresponds to the
     *             argument supplied to the collection() function. If the collection() function
     *             was called with no arguments (to get the "default collection") this argument
     *             will be null.
     * @param base The base URI that should be used. This is the base URI of the
     *             static context in which the call to collection() was made, typically the URI
     *             of the stylesheet or query module
     * @return an Iterator over the documents in the collection. The items returned
     * by this iterator must be instances either of xs:anyURI, or of node() (specifically,
     * {@link net.sf.saxon.om.NodeInfo}.). If xs:anyURI values are returned, the corresponding
     * document will be retrieved as if by a call to the doc() function: this means that
     * the system first checks to see if the document is already loaded, and if not, calls
     * the registered URIResolver to dereference the URI. This is the recommended approach
     * to ensure that the resulting collection is stable: however, it has the consequence
     * that the documents will by default remain in memory for the duration of the query
     * or transformation.
     */

    public SequenceIterator resolve(String href, String base, XPathContext context) throws XPathException {

        if (href == null) {
            // default collection. This returns empty, we previously threw an error.
            return EmptyIterator.getInstance();
        }

        URIQueryParameters params = null;
        URI relativeURI;
        try {
            relativeURI = new URI(ResolveURI.escapeSpaces(href));
            String query = relativeURI.getQuery();
            if (query != null) {
                params = new URIQueryParameters(query, context.getConfiguration());
                int q = href.indexOf('?');
                href = ResolveURI.escapeSpaces(href.substring(0, q));
                relativeURI = new URI(href);
            }

        } catch (URISyntaxException e) {
            XPathException err = new XPathException("Invalid relative URI " + Err.wrap(href, Err.VALUE) + " passed to collection() function");
            err.setErrorCode("FODC0004");
            err.setXPathContext(context);
            throw err;
        }

        URI resolvedURI = makeAbsoluteURI(href, base, context, relativeURI);

        if ("file".equals(resolvedURI.getScheme())) {
            File file = new File(resolvedURI);
            if (!file.exists()) {
                XPathException err = new XPathException("The file or directory " + resolvedURI + " does not exist");
                err.setErrorCode("FODC0004");
                err.setXPathContext(context);
                throw err;
            }
            if (file.isDirectory()) {
                return directoryContents(file, params, context);
            }
        }
        return catalogContents(href, base, resolvedURI.toString(), context);

    }

    protected URI makeAbsoluteURI(String href, String base, XPathContext context, URI relativeURI) throws XPathException {
        URI resolvedURI;
        if (!relativeURI.isAbsolute()) {
            if (base == null) {
                base = ResolveURI.tryToExpand(base);
                if (base == null) {
                    XPathException err = new XPathException("Cannot resolve relative URI: no base URI available");
                    err.setErrorCode("FODC0004");
                    err.setXPathContext(context);
                    throw err;
                }
            }
            try {
                resolvedURI = ResolveURI.makeAbsolute(href, base);
            } catch (URISyntaxException e) {
                XPathException err = new XPathException("Cannot resolve relative URI: " + e.getMessage());
                err.setErrorCode("FODC0004");
                err.setXPathContext(context);
                throw err;
            }
        } else {
            resolvedURI = relativeURI;
        }
        return resolvedURI;
    }

    /**
     * Return the contents of a collection that maps to a directory in filestore
     * @param directory the directory to be processed
     * @param params parameters indicating whether to process recursively, what to do on
     * errors, and which files to select
     * @param context the dynamic XPath evaluation context
     * @return an iterator over the documents in the collection
     */

    private SequenceIterator directoryContents(File directory, URIQueryParameters params, XPathContext context) {

        FilenameFilter filter = null;

        if (params != null) {
            FilenameFilter f = params.getFilenameFilter();
            if (f != null) {
                filter = f;
            }
        }

        File[] files;
        if (filter == null) {
            files = directory.listFiles();
        } else {
            files = directory.listFiles(filter);
        }

        ObjectValue[] fileValues = new ObjectValue[files.length];
        for (int f=0; f<files.length; f++) {
            fileValues[f] = new ObjectValue(files[f]);
        }

        // If the URI requested suppression of errors, or that errors should be treated
        // as warnings, we set up a special ErrorListener to achieve this

        int onError = URIQueryParameters.ON_ERROR_FAIL;
        if (params != null && params.getOnError() != null) {
            onError = params.getOnError().intValue();
        }
        final Controller controller = context.getController();
        final PipelineConfiguration oldPipe = controller.makePipelineConfiguration();
        final PipelineConfiguration newPipe = new PipelineConfiguration(oldPipe);
        final ErrorListener oldErrorListener = controller.getErrorListener();
        if (onError == URIQueryParameters.ON_ERROR_IGNORE) {
            newPipe.setErrorListener(new ErrorListener() {
                public void warning(TransformerException exception) {}
                public void error(TransformerException exception) {}
                public void fatalError(TransformerException exception) {}
            });
        } else if (onError == URIQueryParameters.ON_ERROR_WARNING) {
            newPipe.setErrorListener(new ErrorListener() {
                public void warning(TransformerException exception) throws TransformerException {
                    oldErrorListener.warning(exception);
                }
                public void error(TransformerException exception) throws TransformerException {
                    oldErrorListener.warning(exception);
                    XPathException supp = new XPathException("The document will be excluded from the collection");
                    supp.setLocator(exception.getLocator());
                    oldErrorListener.warning(supp);
                }
                public void fatalError(TransformerException exception) throws TransformerException {
                    error(exception);
                }
            });
        }
        FileExpander expander = new FileExpander(params, newPipe);
        SequenceIterator base = new ArrayIterator(fileValues);
        return new MappingIterator(base, expander);
    }

    /**
     * Return a collection defined as a list of URIs in a catalog file
     * @param href the relative URI as supplied
     * @param baseURI the base URI
     * @param absURI the absolute URI of the catalog file
     * @param context the dynamic evaluation context
     * @return an iterator over the documents in the collection
     * @throws XPathException if any failures occur
     */

    private SequenceIterator catalogContents(String href, String baseURI, String absURI, final XPathContext context)
    throws XPathException {

        boolean stable = true;
        NamePool pool = context.getController().getNamePool();

        Source source = Document.resolveURI(href, baseURI, null, context.getController());
        ParseOptions options = new ParseOptions();
        options.setSchemaValidationMode(Validation.SKIP);
        DocumentInfo catalog = context.getConfiguration().buildDocument(source, options);
        if (catalog==null) {
            // we failed to read the catalogue
            XPathException err = new XPathException("Failed to load collection catalog " + absURI);
            err.setErrorCode("FODC0004");
            err.setXPathContext(context);
            throw err;
        }

        // Now return an iterator over the documents that it refers to

        SequenceIterator iter =
                catalog.iterateAxis(Axis.CHILD, NodeKindTest.ELEMENT);
        NodeInfo top = (NodeInfo)iter.next();
        if (top == null || !("collection".equals(top.getLocalPart()) && top.getURI().length() == 0)) {
            XPathException err = new XPathException("collection catalog must contain top-level element <collection>");
            err.setErrorCode("FODC0004");
            err.setXPathContext(context);
            throw err;
        }
        iter.close();

        String stableAtt = top.getAttributeValue(pool.allocate("", "", "stable"));
        if (stableAtt != null) {
            if ("true".equals(stableAtt)) {
                stable = true;
            } else if ("false".equals(stableAtt)) {
                stable = false;
            } else {
                XPathException err = new XPathException(
                        "The 'stable' attribute of element <collection> must be true or false");
                err.setErrorCode("FODC0004");
                err.setXPathContext(context);
                throw err;
            }
        }

        final boolean finalStable = stable;
        SequenceIterator documents =
                top.iterateAxis(Axis.CHILD, NodeKindTest.ELEMENT);

        ItemMappingFunction catalogueMapper = new ItemMappingFunction() {
            public Item map(Item item) throws XPathException {
                NodeInfo element = (NodeInfo)item;
                if (!("doc".equals(element.getLocalPart()) &&
                        element.getURI().length() == 0)) {
                    XPathException err = new XPathException("children of <collection> element must be <doc> elements");
                    err.setErrorCode("FODC0004");
                    err.setXPathContext(context);
                    throw err;
                }
                String href = Navigator.getAttributeValue(element, "", "href");
                if (href==null) {
                    XPathException err = new XPathException("\"<doc> element in catalog has no @href attribute\"");
                    err.setErrorCode("FODC0004");
                    err.setXPathContext(context);
                    throw err;
                }
                String uri;
                try {
                    uri = new URI(element.getBaseURI()).resolve(href).toString();
                } catch (URISyntaxException e) {
                    XPathException err = new XPathException("Invalid base URI or href URI in collection catalog: ("
                            + element.getBaseURI() + ", " + href + ")");
                    err.setErrorCode("FODC0004");
                    err.setXPathContext(context);
                    throw err;
                }
                if (finalStable) {
                    return new AnyURIValue(uri);
                } else {
                    // stability not required, bypass the document pool and URI resolver
                    return context.getConfiguration().buildDocument(new StreamSource(uri));
                }
            }
        };

        return new ItemMappingIterator(documents, catalogueMapper);
    }

    /**
     * Mapping function to process the files in a directory. This maps a sequence of external
     * objects representing files to a sequence of DocumentInfo nodes representing the parsed
     * contents of those files.
     */

    private static class FileExpander implements MappingFunction {

        private URIQueryParameters params;
        boolean recurse = false;
        int strip = Whitespace.UNSPECIFIED;
        int validation = Validation.STRIP;
        Boolean xinclude = null;
        boolean unparsed;
        XMLReader parser = null;
        int onError = URIQueryParameters.ON_ERROR_FAIL;
        FilenameFilter filter = null;
        PipelineConfiguration pipe;

        public FileExpander(URIQueryParameters params, PipelineConfiguration pipe) {
            this.params = params;
            this.pipe = pipe;
            if (params != null) {
                FilenameFilter f = params.getFilenameFilter();
                if (f != null) {
                    filter = f;
                }
                Boolean r = params.getRecurse();
                if (r != null) {
                    recurse = r.booleanValue();
                }
                Integer v = params.getValidationMode();
                if (v != null) {
                    validation = v.intValue();
                }
                xinclude = params.getXInclude();
                strip = params.getStripSpace();
                unparsed = params.isUnparsed();
                Integer e = params.getOnError();
                if (e != null) {
                    onError = e.intValue();
                }
                XMLReader p = params.getXMLReader();
                if (p != null) {
                    parser = p;
                }
            }

        }

        /**
         * Map one item to a sequence.
         *
         * @param item    The item to be mapped.
         *                If context is supplied, this must be the same as context.currentItem().
         * @return either (a) a SequenceIterator over the sequence of items that the supplied input
         *         item maps to, or (b) an Item if it maps to a single item, or (c) null if it maps to an empty
         *         sequence.
         */

        public SequenceIterator map(Item item) throws XPathException {
            File file = (File)((ObjectValue)item).getObject();
            if (file.isDirectory()) {
                if (recurse) {
                   File[] files;
                    if (filter == null) {
                        files = file.listFiles();
                    } else {
                        files = file.listFiles(filter);
                    }

                    ObjectValue[] fileValues = new ObjectValue[files.length];
                    for (int f=0; f<files.length; f++) {
                        fileValues[f] = new ObjectValue(files[f]);
                    }

                    FileExpander expander = new FileExpander(params, pipe);
                    return new MappingIterator(new ArrayIterator(fileValues), expander);
                } else {
                    return null;
                }
            } else if (unparsed) {
                try {
                    Reader reader = new FileReader(file);
                    NameChecker checker = pipe.getConfiguration().getNameChecker();
                    CharSequence content = UnparsedText.readFile(checker, reader);
                    String uri = file.toURI().toString();
                    TextFragmentValue doc = new TextFragmentValue(content, uri);
                    doc.setSystemId(file.toURI().toString());
                    doc.setConfiguration(pipe.getConfiguration());
                    return SingletonIterator.makeIterator(doc);
                } catch (IOException err) {
                    if (onError == URIQueryParameters.ON_ERROR_IGNORE) {
                        return null;
                    } else if (onError == URIQueryParameters.ON_ERROR_WARNING) {
                        try {
                            XPathException warn = new XPathException("Failed to read " + file.getPath(), err);
                            pipe.getErrorListener().warning(warn);
                            XPathException supp = new XPathException("The document will be excluded from the collection");
                            pipe.getErrorListener().warning(supp);
                        } catch (TransformerException err2) {
                            //
                        }
                        return null;
                    } else {
                        throw new XPathException("Failed to read " + file.getPath(), err);
                    }
                }
            } else {
                try {
                    Source source = new StreamSource(file.toURI().toString());
                    ParseOptions options = new ParseOptions();
                    if (validation != Validation.STRIP && validation != Validation.PRESERVE) {
                        options.setSchemaValidationMode(validation);
                    }
                    if (xinclude != null) {
                        options.setXIncludeAware(xinclude.booleanValue());
                    }
                    if (parser != null) {
                        options.setXMLReader(parser);
                    }

                    Stripper stripper;
                    if (params != null) {
                        int stripSpace = params.getStripSpace();
                        switch (strip) {
                            case Whitespace.ALL: {
                                stripper = AllElementStripper.getInstance();
                                stripper.setStripAll();
                                options.addFilter(stripper);
                                break;
                            }
                            case Whitespace.IGNORABLE:
                            case Whitespace.NONE:
                                options.setStripSpace(stripSpace);
                        }
                    }
                    DocumentInfo doc = pipe.getConfiguration().buildDocument(source, options);
                    return SingletonIterator.makeIterator(doc);
                } catch (XPathException err) {
                    if (onError == URIQueryParameters.ON_ERROR_IGNORE) {
                        return null;
                    } else if (onError == URIQueryParameters.ON_ERROR_WARNING) {
                        try {
                            if (!err.hasBeenReported()) {
                                pipe.getErrorListener().warning(err);
                                XPathException supp = new XPathException("The document will be excluded from the collection");
                                supp.setLocator(err.getLocator());
                                pipe.getErrorListener().warning(supp);
                            }
                        } catch (TransformerException err2) {
                            //
                        }
                        return null;
                    } else {
                        throw err;
                    }
                }
            }
        }
    }




}

//
// The contents of this file are subject to the Mozilla Public License Version 1.0 (the "License");
// you may not use this file except in compliance with the License. You may obtain a copy of the
// License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the License for the specific language governing rights and limitations under the License.
//
// The Original Code is: all this file.
//
// The Initial Developer of the Original Code is Michael H. Kay.
//
// Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
// Contributor(s): none.
//

