import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileDuplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path = "/home/tivakar/Desktop/Thesis/Thesis datas/335/final";
		String path_out = "/home/tivakar/Desktop/Thesis/Thesis datas/335/335_4";
		
		int DUP_NUM = 1;
		int START_DUP_NUM = 4;
		int FILE_RANGE = START_DUP_NUM + DUP_NUM -1;

		String fileName = null;

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		File file = null;
		File fileDup = null;

		for (int index = 0; index < listOfFiles.length; index++) {

			file = listOfFiles[index];
			fileName = file.getName().replaceAll(".xml", "");

			for (int i = START_DUP_NUM; i <= FILE_RANGE; i++) {

				fileDup = new File(path_out + "/" + fileName + "_" + i + ".xml");

				// duplicate
				try {
					FileUtils.copyFile(file, fileDup);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

}
