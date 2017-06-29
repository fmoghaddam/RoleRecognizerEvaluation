package evaluationrunner;

public class Evaluate {

	public static void main(String[] args) {
		
//		final RoleListProvider originalRoleProvider = new RoleListProviderFileBased();
//		final GroundTruthProvider groundTruthProvider = new GroundTruthProviderFileBased();
//
//		originalRoleProvider.loadRoles();
//		groundTruthProvider.loadData();
//
//		groundTruthProvider.printStatistic();
//		groundTruthProvider.printStatisticRolePhrase();
		
		
		final ExactMatchEvaluation exm = new ExactMatchEvaluation();
		exm.DictionaryCompletenessTestCaseSensitive();
		exm.DictionaryCompletenessTestCaseInsensitive();
		exm.roleDetectionTestCaseSensitive();
		exm.roleDitectionAndCategorizationTestCaseSensitive();
		exm.roleDetectionTestCaseInSensitive();
		exm.roleDitectionAndCategorizationTestCaseInSensitive();
	}

}
