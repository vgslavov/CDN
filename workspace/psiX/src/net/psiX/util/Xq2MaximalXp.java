package net.psiX.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Xq2MaximalXp {

	/**
	 * @param args
	 */
	public HashMap<String, String> getMaximalXpath(String xQuery) {
		//String xQuery = "for $x in collection('...')/bookstore/book for $y in collection('////kc.umkc.edu/kc-users/home/t/tk27f/Desktop/Thesis/collc.xml')/bookstore/book where $x/ti/price>30 and $y/price>30 order by $x/title return $x/title";
		String maxXpath = null;
		ArrayList<String> xQuerySplits = getEachLines(xQuery);

		return(getMaxXpath(xQuerySplits));

	}

	/**
	 * Method to seperate each line of XQuery commands
	 * 
	 * @param XQuery
	 * @return
	 */
	public ArrayList<String> getEachLines(String XQuery) {
		String cutWordOld = null;
		String cutWord = null;
		StringTokenizer strTknzr = null;
		String temp = null;
		String tempArray[] = null;
		ArrayList<String> xQuerySplits = new ArrayList<String>();

		while (!XQuery.equals("")) {

			cutWord = null;

			strTknzr = new StringTokenizer(XQuery, " ");

			while (strTknzr.hasMoreTokens()) {
				temp = strTknzr.nextToken().trim();
				if (temp.equalsIgnoreCase("for")
						|| temp.equalsIgnoreCase("let")
						|| temp.equalsIgnoreCase("where")
						|| temp.equalsIgnoreCase("order")
						|| temp.equalsIgnoreCase("return")) {
					cutWordOld = cutWord;
					cutWord = temp;
					if (cutWordOld != null
							&& !cutWord.equalsIgnoreCase("return")) {
						tempArray = XQuery.split(cutWord);

						if (cutWord.equals(cutWordOld)) {
							XQuery = cutWord + tempArray[2];
							xQuerySplits.add(cutWord + tempArray[1]);
						} else {
							XQuery = cutWord + tempArray[1];
							xQuerySplits.add(tempArray[0]);
						}

						// System.out.println(tempArray[0]);//Remove to see o/p
						break;
					} else if (cutWord.equalsIgnoreCase("return")) {
						tempArray = XQuery.split(cutWord);

						// code needs to be added
						XQuery = cutWord + tempArray[1];
						// System.out.println(tempArray[0]);//Remove to see o/p
						xQuerySplits.add(tempArray[0]);
						// System.out.println(cutWord + tempArray[1]);
						xQuerySplits.add(cutWord + tempArray[1]);
						XQuery = "";
						break;
					}

				}
			}

		}
		return xQuerySplits;
	}

	public HashMap<String, String> getMaxXpath(ArrayList<String> xQuerySplits) {

		Iterator itr = xQuerySplits.iterator();
		String temp = null;
		HashMap<String, String> hashMap = new HashMap<String, String>();
		HashMap<String, String> hMapInitialValue = new HashMap<String, String>();

		while (itr.hasNext()) {
			temp = (String) itr.next();
			// System.out.println(temp);
			if (temp.contains("for")) {

				StringTokenizer strTkzrFor = new StringTokenizer(temp);
				String tempFor = null;
				String variable = null;
				String temp1 = null;
				while (strTkzrFor.hasMoreTokens()) {
					tempFor = strTkzrFor.nextToken();

					if (tempFor.contains("$")) {
						variable = tempFor;
					}

					if (tempFor.contains("collection")) {
						StringTokenizer strTkzrForTemp = new StringTokenizer(
								tempFor, "()");
						int tokenNo = strTkzrForTemp.countTokens();
						int start = 1;
						while (start != tokenNo) {
							strTkzrForTemp.nextToken();
							start++;
						}
						String old = hashMap.get(variable);
						if (old != null) {
							temp1 = strTkzrForTemp.nextToken();
							hashMap.put(variable, old + temp1);
							hMapInitialValue.put(variable, old + temp1);
						} else {
							temp1 = strTkzrForTemp.nextToken();
							hashMap.put(variable, temp1);
							hMapInitialValue.put(variable, temp1);
						}

					}

				}

			} else if (temp.contains("let")) {

			} else if (temp.contains("where")) {

				// Old code
				// StringTokenizer strTkzrWhere = new StringTokenizer(temp);
				// String tempWhere = null;
				// String variable = null;
				// while (strTkzrWhere.hasMoreTokens()) {
				// tempWhere = strTkzrWhere.nextToken();
				//
				// while (tempWhere.contains("$")) {
				// Iterator<String> iterator = hashMap.keySet().iterator();
				// while (iterator.hasNext()) {
				// variable = iterator.next();
				// tempWhere = tempWhere.replace(variable,
				// hashMap.get(variable));
				// hashMap.put(variable, tempWhere);
				// }
				//
				// }
				//
				// }

				// New code
				StringTokenizer strTkzrWhere = new StringTokenizer(temp);
				String tempWhere = null;
				String variable = null;
				String[] split1 = null;
				String constructString = null;
				boolean startbracket;// Start bracket [

				while (strTkzrWhere.hasMoreTokens()) {
					tempWhere = strTkzrWhere.nextToken();
					startbracket = false;
					if (tempWhere.contains("$")) {
						if (tempWhere.contains("/")) {
							// Splitting based on /
							split1 = tempWhere.split("/");
							// Checking for occurrence of $
							int splitLength = split1.length;
							int i = 0;
							if (splitLength != 0) {
								while (i != splitLength) {
									constructString = "";
									if (split1[i].contains("$")) {
										constructString = constructString
												+ hashMap.get(split1[i]);
										int j = i + 1;
										while (j != splitLength) {
											if (split1[j].contains("$")) {
												constructString = constructString
														+ "/"
														+ hashMap
																.get(split1[j]);
											} else {
												if (splitLength > 1
														&& !startbracket) {
													constructString = constructString
															+ "[";
													startbracket = true;// Start
																		// bracket
																		// [
																		// added

												}
												constructString = constructString
														+ "/" + split1[j];

											}
											j++;
										}
										 if (startbracket) {
										 constructString = constructString + "]";// close bracket ]
										 }
										hashMap.put(split1[i], constructString);
									}
									i++;
								}
							}

						}
					}
				}

				

			} else if (temp.contains("return")) {
				// StringTokenizer strTkzrWhere = new StringTokenizer(temp);
				// String tempReturn = null;
				// String variable = null;
				// while (strTkzrWhere.hasMoreTokens()) {
				// tempReturn = strTkzrWhere.nextToken();
				//
				// while (tempReturn.contains("$")) {
				// Iterator<String> iterator = hashMap.keySet().iterator();
				// while (iterator.hasNext()) {
				// variable = iterator.next();
				// tempWhere = tempWhere.replace(variable,
				// hashMap.get(variable));
				// hashMap.put(variable, tempWhere);
				// }
				//
				// }
				//
				// }
				
				StringTokenizer strTkzrWhere = new StringTokenizer(temp);
				String tempWhere = null;
				String variable = null;
				String[] split1 = null;
				String constructString = null;
				boolean startbracket;// Start bracket [

				while (strTkzrWhere.hasMoreTokens()) {
					tempWhere = strTkzrWhere.nextToken();
					startbracket = false;
					if (tempWhere.contains("$")) {
						if (tempWhere.contains("/")) {
							// Splitting based on /
							split1 = tempWhere.split("/");
							// Checking for occurrence of $
							int splitLength = split1.length;
							int i = 0;
							if (splitLength != 0) {
								while (i != splitLength) {
									constructString = "";
									if (split1[i].contains("$")) {
										constructString = constructString
												+ hashMap.get(split1[i]);
										int j = i + 1;
										while (j != splitLength) {
											if (split1[j].contains("$")) {
												constructString = constructString
														+ "/"
														+ hashMap
																.get(split1[j]);
											} else {
												if (splitLength > 1
														&& !startbracket) {
													constructString = constructString
															+ "[";
													startbracket = true;// Start
																		// bracket
																		// [
																		// added

												}
												constructString = constructString
														+ "/" + split1[j];

											}
											j++;
										}
										 if (startbracket) {
										 constructString = constructString + "]";// close bracket ]
										 }
										hashMap.put(split1[i], constructString);
									}
									i++;
								}
							}

						}
					}
				}
				
				
				
				
			}
		}

		return hashMap;
	}
}
