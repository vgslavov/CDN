package net.psiX.test;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.psiX.connection.PsiXConnector;

public class SharePublishCloud {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String dataSet = "1340/1340";
		File file = new File("/home/tivakar/Desktop/Thesis/Thesis datas/"
				+ dataSet);

		String HOSTLIST[] = { "ec2-107-20-108-143.compute-1.amazonaws.com",
				"ec2-107-22-155-81.compute-1.amazonaws.com",
				"ec2-50-17-170-229.compute-1.amazonaws.com",
				"ec2-107-22-107-194.compute-1.amazonaws.com",
				"ec2-50-16-58-98.compute-1.amazonaws.com",
				"ec2-107-20-129-113.compute-1.amazonaws.com",
				"ec2-107-22-1-82.compute-1.amazonaws.com",
				"ec2-184-73-63-205.compute-1.amazonaws.com",
				"ec2-184-73-151-174.compute-1.amazonaws.com",
				"ec2-50-16-179-25.compute-1.amazonaws.com",
				"ec2-184-72-150-94.compute-1.amazonaws.com",
				"ec2-67-202-6-181.compute-1.amazonaws.com",
				"ec2-107-22-30-18.compute-1.amazonaws.com",
				"ec2-107-20-83-157.compute-1.amazonaws.com",
				"ec2-107-22-62-65.compute-1.amazonaws.com",
				"ec2-50-17-157-217.compute-1.amazonaws.com",
				"ec2-107-22-118-50.compute-1.amazonaws.com",
				"ec2-107-22-32-230.compute-1.amazonaws.com",
				"ec2-184-72-150-69.compute-1.amazonaws.com",
				"ec2-107-22-18-69.compute-1.amazonaws.com" };

		int docs2Publish[] = { 1, 1, 1, 1, 1, 1, 2, 3, 5, 8, 14, 23, 36, 55,
				81, 115, 159, 212, 274, 345 };

		int index_count = 0;
		int currentDocs2Publish = docs2Publish[index_count];
		int currentDocPublished = 0;

		if (file.isDirectory()) {

			// Get all files in the directory
			File[] listOfFiles = file.listFiles();

			PsiXConnector psiX = new PsiXConnector();

			// Iterate through all files in the directory
			for (int index = 0; index < listOfFiles.length; index++) {

				file = listOfFiles[index];

				if (file.exists()) {
					try {
						// Publish

						psiX.publishDoc(
								file.getPath(),
								null,
								"http://" + HOSTLIST[index_count] + ":8080"
										+ "/documents/" + dataSet + "/"
										+ file.getName());

						// Increase current published document number
						currentDocPublished++;

						System.out.println("Hostindex: " + (index_count + 1)
								+ " # To publish: " + currentDocs2Publish
								+ " # CurrentPublished: " + currentDocPublished
								+ " # TotalPublished: " + index);

						System.out.println("http://" + HOSTLIST[index_count]
								+ ":8080" + "/documents/" + dataSet + "/"
								+ file.getName());

						// Increase index_count and Reset currentDoc published
						// no.
						if (currentDocPublished == currentDocs2Publish) {

							index_count++;
							currentDocs2Publish = docs2Publish[index_count];
							currentDocPublished = 0;

						}

						// Wait
						Thread.sleep(15000);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			}

		}
	}

}
