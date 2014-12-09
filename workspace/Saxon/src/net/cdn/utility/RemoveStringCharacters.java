package net.cdn.utility;

public class RemoveStringCharacters {

	public String removeFirstOccurenceIf(String originalString, String c) {

		if (originalString != null) {
			return originalString.replaceFirst(c, "");
		}

		return originalString;

	}

}
