package net.cdn.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class MergeXMLFiles {

	public void mergeTwoFiles(String root, String file1, String file2,
			String mergedFileName) {

		// Creates file to write to
		Writer output = null;
		String sOutput = "";
		String newline = null;
		String startRoot = "<" + root + ">";
		String endRoot = "</" + root + ">";
		try {

			newline = System.getProperty("line.separator");

			// Read in xml file 1
			FileInputStream in = new FileInputStream(file1);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			boolean rootArrived1 = false;

			// output.write("<" + root + ">");
			sOutput = startRoot;
			while ((strLine = br.readLine()) != null) {

				if (strLine.contains(startRoot)) {
					strLine = strLine.substring(strLine.indexOf(startRoot),
							strLine.length());
					strLine = strLine.replace(startRoot, "");
					rootArrived1 = true;

				}
				if (strLine.contains(endRoot)) {

					strLine = strLine.substring(0, strLine.indexOf(endRoot)
							+ endRoot.length());
					strLine = strLine.replace(endRoot, "");
					sOutput = sOutput + strLine;
					sOutput = sOutput + newline;
					break;
				}
				if (rootArrived1) {
					// output.write("\n");
					// output.write(strLine);
					sOutput = sOutput + "\n";
					sOutput = sOutput + strLine;
				}
				// System.out.println(strLine);
			}

			br.close();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem with " + file1);
			e.printStackTrace();
		}

		// Read in xml file 2
		FileInputStream in2 = null;
		try {
			in2 = new FileInputStream(file2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
		String strLine2;
		boolean rootArrived2 = false;

		try {
			while ((strLine2 = br2.readLine()) != null) {

				if (strLine2.contains(startRoot)) {
					strLine2 = strLine2.substring(strLine2.indexOf(startRoot),
							strLine2.length());
					strLine2 = strLine2.replace(startRoot, "");
					rootArrived2 = true;
				}
				if (strLine2.contains(endRoot)) {

					strLine2 = strLine2.substring(0, strLine2.indexOf(endRoot)
							+ endRoot.length());
					strLine2 = strLine2.replace(endRoot, "");
					sOutput = sOutput + strLine2;
					sOutput = sOutput + newline;
					break;
				}
				if (rootArrived2) {
					// output.write(strLine2);
					// output.write(newline);
					sOutput = sOutput + strLine2;
					sOutput = sOutput + newline;
				}
				// System.out.println(strLine2);
			}

			br2.close();
			in2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// output.write("</" + root + ">");
			// sOutput.replaceAll("<body>", "");
			// sOutput.replaceAll("</body>", "");
			// sOutput.replaceAll("<html>", "");
			// sOutput.replaceAll("</html>", "");
			sOutput = sOutput + endRoot;
			output = new BufferedWriter(new FileWriter(mergedFileName));
			output.write(sOutput);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("** Merge Completed");

	}

}
