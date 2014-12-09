package net.cdn.Objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Tivakar
 * @description Object for storing values of the 1. XQuery for a variable 2.
 *              Storing all hostnames and all its pathnames for a Variable
 * 
 */

public class XQueryObject {

	private HashMap<String, HashMap<String, HashMap<String, String>>> varHostPath = null;// Variable
																		// for
																		// storing
																		// all
																		// hostnames
																		// and
																		// all
																		// its
																		// pathnames
	private HashMap<String, FLOWRObject> flowrObjForXQuery = null;
	private HashMap<String, String> hMapVarNMaxXpath = null;// Vars and its Max.
															// XPath
	
	private String xQuery = null;

	public HashMap<String, HashMap<String, HashMap<String, String>>> getVarHostPath() {
		return varHostPath;
	}

	public void setVarHostPath(
			HashMap<String, HashMap<String, HashMap<String, String>>> varHostPath) {
		this.varHostPath = varHostPath;
	}

	public HashMap<String, FLOWRObject> getFlowrObjForXQuery() {
		return flowrObjForXQuery;
	}

	public void setFlowrObjForXQuery(
			HashMap<String, FLOWRObject> flowrObjForXQuery) {
		this.flowrObjForXQuery = flowrObjForXQuery;
	}

	public HashMap<String, String> gethMapVarNMaxXpath() {
		return hMapVarNMaxXpath;
	}

	public void sethMapVarNMaxXpath(HashMap<String, String> hMapVarNMaxXpath) {
		this.hMapVarNMaxXpath = hMapVarNMaxXpath;
	}

	public String getxQuery() {
		return xQuery;
	}

	public void setxQuery(String xQuery) {
		this.xQuery = xQuery;
	}

}
