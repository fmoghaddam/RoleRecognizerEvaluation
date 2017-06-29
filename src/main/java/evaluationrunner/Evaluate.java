package evaluationrunner;

import dictionary.RoleListProvider;
import dictionary.RoleListProviderFileBased;
import groundtruth.GroundTruthProvider;
import groundtruth.GroundTruthProviderFileBased;

public class Evaluate {

	public static void main(String[] args) {
		
		final RoleListProvider originalRoleProvider = new RoleListProviderFileBased();
		final GroundTruthProvider groundTruthProvider = new GroundTruthProviderFileBased();

		originalRoleProvider.loadRoles();
		groundTruthProvider.loadData();

		groundTruthProvider.printStatistic();
		groundTruthProvider.printStatisticRolePhrase();

	}

}
