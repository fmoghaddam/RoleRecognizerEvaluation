package evaluation;

import java.io.File;

public class GroundTruthProviderFileBased extends GroundTruthProvider {
	private static final String DATA_FOLDER = "groundTruth";

	@Override
	public void loadData() {
		final File[] listOfFiles = new File(DATA_FOLDER).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			final String file = listOfFiles[i].getName();
			document.add(GroundTruthParser.parse(DATA_FOLDER + File.separator + file));
		}
	}
}
