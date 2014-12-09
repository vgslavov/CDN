package net.psiX.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class psixResults {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Writer output = null;

		String[] HOSTLIST = { "192.168.1.203:8080",
				"192.168.1.204:8080" };

		int hostNo = HOSTLIST.length;
		int totalDocument = 335;
		int duplicationNo = 1;

		String psiXResults = "";
		String outputPath = "/home/tk27f/Desktop/Baseline/psixResults.txt";

		for (int i = 0; i < hostNo; i++) {

			for (int j = 1; j <= duplicationNo; j++) {

				for (int k = 1; k <= totalDocument; k++) {
					psiXResults = psiXResults + "/>[Docid: http://"
							+ HOSTLIST[i] + "/documents/335/" + k + ".xml.#]\n";

				}

			}

		}

		File file = new File(outputPath);
		output = new BufferedWriter(new FileWriter(file));
		output.write(psiXResults);
		output.close();

	}

}
