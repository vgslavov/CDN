import java.io.File;

public class changeFileNames {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path_original = "/home/tivakar/Desktop/Thesis/Thesis datas/325/HL7V3Files";
		String path_converted="/home/tivakar/Desktop/Thesis/Thesis datas/325/HL7V3Files_converted";
//		String files;
		File folder = new File(path_original);
//		File f = null;
		File[] listOfFiles = folder.listFiles();

		for (int i = 0, newname = 1; i < listOfFiles.length; i++, newname++) {

			if (listOfFiles[i].isFile()) {

				//files = listOfFiles[i].getName();

				//f = new File(path + "/" + files);
				listOfFiles[i].renameTo(new File(path_converted + "/" + newname + ".xml"));

			}
		}

	}

}
