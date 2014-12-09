package net.cdn.utility;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class OccurenceOfSubstring {

	public int countMatches(String str, String sub) {
		if (str == null || str.equals("")||sub == null || sub.equals("")) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

}
