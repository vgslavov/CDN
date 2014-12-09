package net.cdn.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import net.cdn.Objects.FLOWRObject;
import net.cdn.Objects.XQueryObject;
import net.cdn.Objects.XQueryReturnObject;
import net.cdn.utility.OccurenceOfSubstring;

public class QueriesForPeers {

	public XQueryObject getFLOWRObjforVariablesXQuery(XQueryObject xQueryObj) {
		String variable = null;
		String tempXQuery = null;
		Xq2MaximalXp xq2MaxXpath = new Xq2MaximalXp();
		// Parse Xquery String
		ArrayList<String> xQuerySplits = xq2MaxXpath.getEachLines(xQueryObj);
		HashMap<String, String> hMapVarNMaxXpath = xQueryObj
				.gethMapVarNMaxXpath();
		FLOWRObject flowrObj = null;
		HashMap<String, FLOWRObject> flowrObjForXQuery = null;

		Iterator<String> iterHMapVarMaxXpathKeys = hMapVarNMaxXpath.keySet()
				.iterator();
		Iterator<String> itrXQuerySplits = null;
		flowrObjForXQuery = new HashMap<String, FLOWRObject>();
		HashMap<String, XQueryReturnObject> varNxQueryReturnObj = null;

		while (iterHMapVarMaxXpathKeys.hasNext()) {
			variable = (String) iterHMapVarMaxXpathKeys.next();
			itrXQuerySplits = xQuerySplits.iterator();
			// Logic for FLOWR object starts here
			flowrObj = new FLOWRObject();
			while (itrXQuerySplits.hasNext()) {
				tempXQuery = (String) itrXQuerySplits.next();

				// for String in FLOWR object
				if (tempXQuery.contains("for") && tempXQuery.contains(variable)
						&& tempXQuery.contains("collection")) {
					StringTokenizer strTkzrtempXQuery = new StringTokenizer(
							tempXQuery, ")");
					strTkzrtempXQuery.nextToken();
					if (strTkzrtempXQuery.hasMoreTokens()) {
						flowrObj.setsFor(strTkzrtempXQuery.nextToken());
					}
				}
				// let String in FLOWR Object
				else if (tempXQuery.contains("let")
						&& tempXQuery.contains(variable)) {

				}
				// Order String in FlOWR Object
				else if (tempXQuery.contains("order")
						&& tempXQuery.contains(variable)) {

				}
				// Where String in FLOWR Object
				else if (tempXQuery.contains("where")
						&& tempXQuery.contains(variable)) {
					String[] splitTempXQueryWhere = tempXQuery.split("and");
					String[] splitForReturn = null;

					String whereCond = null;
					String varWhere = null;
					String forReturn = null;
					int countDollar;
					int countVar;

					OccurenceOfSubstring findCount = new OccurenceOfSubstring();

					int countStringArray = splitTempXQueryWhere.length;
					int count = 0;

					int countForReturnArray;
					int countForReturn;

					while (count < countStringArray) {

						// For return string
						splitForReturn = splitTempXQueryWhere[count].split(" ");
						countForReturnArray = splitForReturn.length;

						countForReturn = 0;

						while (countForReturn < countForReturnArray) {
							forReturn = splitForReturn[countForReturn];
							if (forReturn.contains("$")) {
								varNxQueryReturnObj = createXQueryReturnObject(
										varNxQueryReturnObj, forReturn.trim());
							}
							countForReturn++;
						}

						varWhere = splitTempXQueryWhere[count];

						countDollar = findCount.countMatches(varWhere, "$");
						countVar = findCount.countMatches(varWhere, variable);

						if (countDollar == countVar) {
							if (whereCond == null) {
								whereCond = varWhere;
							} else {
								whereCond = whereCond + " and " + varWhere;
							}
						}
						count++;
					}
					flowrObj.setsWhere(whereCond);
				}
				// Return String in FLOWR Object
				else if (tempXQuery.contains("return")
						&& tempXQuery.contains(variable)) {
					StringTokenizer strTokReturn = new StringTokenizer(
							tempXQuery);
					ArrayList<String> arrReturn = new ArrayList<String>();
					String tempReturn = null;
					String tempConstruction = null;
					char tempCharAt;
					int iReturn;
					int iTempReturn;
					// Splitting the correct return statement
					while (strTokReturn.hasMoreTokens()) {
						tempReturn = strTokReturn.nextToken();
						if (tempReturn.contains("$")) {
							tempConstruction = "";
							iTempReturn = tempReturn.length();
							iReturn = tempReturn.indexOf('$');

							for (int i = iReturn; i < iTempReturn; i++) {
								tempCharAt = tempReturn.charAt(i);
								if (tempCharAt != '}' && tempCharAt != '"') {
									tempConstruction = tempConstruction
											+ tempCharAt;
								} else {
									break;
								}
							}
							arrReturn.add(tempConstruction);
						}
					}

					// Forming return statement
					Iterator<String> iterAllReturns = null;
					iterAllReturns = arrReturn.iterator();
					String varReturnFormation = null;
					String tempVarReturnFormation = null;

					XQueryReturnObject tempReturnObj = null;
					XQueryReturnObject tempReturnObj1 = null;

					String temp1 = null;
					String temp2 = null;

					while (iterAllReturns.hasNext()) {
						tempVarReturnFormation = iterAllReturns.next();
						if (tempVarReturnFormation.contains(variable)) {
							varNxQueryReturnObj = createXQueryReturnObject(
									varNxQueryReturnObj, tempVarReturnFormation);
						}
					}

				}

			}
			// Construction of Return string from Return Object
			flowrObj.setsReturn(constructReturnString(varNxQueryReturnObj,
					variable));
			flowrObjForXQuery.put(variable, flowrObj);
			xQueryObj.setFlowrObjForXQuery(flowrObjForXQuery);

		}

		return xQueryObj;

	}

	/**
	 * @author Tivakar
	 * @description Construct XQueryReturnObject by sending the query statement
	 * @param xQueryReturnObj
	 * @param returnString
	 * @return xQueryReturnObj
	 */

	public HashMap<String, XQueryReturnObject> createXQueryReturnObject(
			HashMap<String, XQueryReturnObject> xQueryReturnObj,
			String returnString) {
		XQueryReturnObject returnObj = null;
		StringTokenizer strTkzTempVarReturnFormation = null;
		String[] splitTempVarReturnFormation = null;
		String variable = null;
		int splitCount;
		int i = 0;
		ArrayList<XQueryReturnObject> children = null;

		if (returnString != null && returnString.contains("$")) {

			splitTempVarReturnFormation = returnString.split("/");
			splitCount = splitTempVarReturnFormation.length;

			while (i < splitCount) {
				variable = splitTempVarReturnFormation[i];
				if (variable.contains("$")) {
					break;
				}
			}

			if (xQueryReturnObj == null) {
				returnObj = null;
			} else {
				returnObj = xQueryReturnObj.get(variable);
			}
			if (returnObj == null) {
				returnObj = new XQueryReturnObject();
				returnObj.setRoot(true);
				returnObj.setName(variable);
				returnObj.setEnd(false);
				returnObj.setType("n");// node
			}

			XQueryReturnObject child = createChildForReturnObject(
					splitTempVarReturnFormation, 1, returnObj, returnString);

			if (child == null) {
				returnObj.setEnd(true);
				returnObj.setChildren(null);
				returnObj.setQuery(returnString);

			} else {
				children = returnObj.getChildren();
				if (children == null) {
					children = new ArrayList<XQueryReturnObject>();
				}

				Iterator<XQueryReturnObject> iterChildren = children.iterator();
				XQueryReturnObject tempChildren = null;
				int j = 0;
				boolean childPresent = false;

				while (iterChildren.hasNext()) {
					tempChildren = iterChildren.next();
					if (tempChildren.getName().equals(child.getName())) {
						childPresent = true;
						break;
					}
					j++;
				}
				if (childPresent) {
					children.set(j, child);
				} else {
					children.add(child);
				}
				returnObj.setChildren(children);
			}

			if (xQueryReturnObj == null) {
				xQueryReturnObj = new HashMap<String, XQueryReturnObject>();
			}
			xQueryReturnObj.put(variable, returnObj);

		}
		return xQueryReturnObj;
	}

	/**
	 * @author Tivakar
	 * @description creates XqueryReturnObject which is a child of the parent
	 *              object
	 * @param split
	 * @param splitIndex
	 * @param xQueryObjParent
	 * @param query
	 * @return child
	 */
	public XQueryReturnObject createChildForReturnObject(String[] split,
			int splitIndex, XQueryReturnObject xQueryObjParent, String query) {

		if (splitIndex >= split.length) {
			// When parent node is not an end and no children to add
			return null;
		} else {
			// When there is children to be added
			ArrayList<XQueryReturnObject> children = xQueryObjParent
					.getChildren();
			XQueryReturnObject child = null;
			XQueryReturnObject childChild = null;
			ArrayList<XQueryReturnObject> childChildren = null;
			XQueryReturnObject childChild1 = null;
			boolean childChildPresent = false;

			// When children == null nothing is created for a variable
			if (children == null) {
				child = new XQueryReturnObject();
				child.setName(split[splitIndex]);
				if (split[splitIndex].contains("@")) {
					child.setType("a");
				} else {
					child.setType("n");
				}
				childChild = createChildForReturnObject(split, splitIndex + 1,
						child, query);
				if (childChild == null) {
					// Remove all previous children
					child.setEnd(true);
					child.setQuery(query);
					return child;
				} else {
					children = new ArrayList<XQueryReturnObject>();
					children.add(childChild);
					child.setChildren(children);
					return child;
				}
			} else {
				// If children not null
				Iterator<XQueryReturnObject> iterChildren = children.iterator();

				while (iterChildren.hasNext()) {
					child = iterChildren.next();
					if (child.getName().equals(split[splitIndex])) {
						childChild = createChildForReturnObject(split,
								splitIndex + 1, child, query);
						if (childChild == null) {
							child.setEnd(true);
							child.setQuery(query);
						} else {
							// add child
							childChildren = child.getChildren();
							if (childChildren == null) {
								childChildren = new ArrayList<XQueryReturnObject>();
							}

							Iterator<XQueryReturnObject> iterChildChildren = childChildren
									.iterator();
							childChildPresent = false;
							while (iterChildChildren.hasNext()) {
								childChild1 = iterChildChildren.next();
								if (childChild1.getName().equals(
										childChild.getName())) {
									childChildPresent = true;
									break;
								}
							}

							if (!childChildPresent) {
								childChildren.add(childChild);
								child.setChildren(childChildren);
							}
						}
						return child;
					}
				}

				child = new XQueryReturnObject();
				child.setName(split[splitIndex]);
				if (split[splitIndex].contains("@")) {
					child.setType("a");
				} else {
					child.setType("n");
				}

				childChild = createChildForReturnObject(split, splitIndex + 1,
						child, query);
				if (childChild == null) {
					// Remove all previous children
					child.setChildren(null);
					child.setEnd(true);
					child.setQuery(query);
				} else {

					// add child
					childChildren = child.getChildren();
					if (childChildren == null) {
						childChildren = new ArrayList<XQueryReturnObject>();
					}
					Iterator<XQueryReturnObject> iterChildChildren = childChildren
							.iterator();
					childChildPresent = false;
					while (iterChildChildren.hasNext()) {
						childChild1 = iterChildChildren.next();
						if (childChild1.getName().equals(childChild.getName())) {
							childChildPresent = true;
							break;
						}
					}

					if (!childChildPresent) {
						childChildren.add(childChild);
						child.setChildren(childChildren);
					}
				}
				return child;
			}
		}
	}

	public String constructReturnString(
			HashMap<String, XQueryReturnObject> varNXQueryReturnObj,
			String variable) {

		XQueryReturnObject xQueryReturnObj = null;
		String returnString = "";
		ArrayList<XQueryReturnObject> children = null;
		int childrenCount;
		if (varNXQueryReturnObj != null) {
			xQueryReturnObj = varNXQueryReturnObj.get(variable);
		}
		if (xQueryReturnObj != null) {
			if (xQueryReturnObj.getName().equals(variable)) {
				if (xQueryReturnObj.isEnd()) {
					returnString = xQueryReturnObj.getQuery();

				} else {
					children = xQueryReturnObj.getChildren();

					if (children != null) {
						childrenCount = children.size();
					} else {
						childrenCount = 0;
					}

					for (int i = 0; i < childrenCount; i++) {
						returnString = returnString
								+ "{"
								+ constructReturnStringEnterChild(children
										.get(i)) + "}";
					}

				}
			}
			//System.out.println(returnString);

			return returnString;
		} else {
			return null;
		}

	}

	public String constructReturnStringEnterChild(
			XQueryReturnObject xQueryReturnObj) {
		String returnString = "";
		ArrayList<XQueryReturnObject> children = null;
		int childrenCount;

		if (xQueryReturnObj.isEnd()) {
			returnString = "{" + xQueryReturnObj.getQuery() + "}";
		} else {
			children = xQueryReturnObj.getChildren();

			if (children != null) {
				childrenCount = children.size();
			} else {
				childrenCount = 0;
			}

			for (int i = 0; i < childrenCount; i++) {
				returnString = returnString
						+ constructReturnStringEnterChild(children.get(i));
			}

			returnString = "<" + xQueryReturnObj.getName() + "> "
					+ returnString + " </" + xQueryReturnObj.getName() + ">";

		}

		return returnString;

	}

}
