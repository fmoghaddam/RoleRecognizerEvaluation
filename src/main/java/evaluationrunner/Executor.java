package evaluationrunner;

import dictionary.RoleListProvider;
import dictionary.RoleListProviderFileBased;
import groundtruth.GroundTruthProvider;
import groundtruth.GroundTruthProviderFileBased;

public class Executor {

	public static void main(String[] args) {
		
		final RoleListProvider originalRoleProvider = new RoleListProviderFileBased();
		final GroundTruthProvider groundTruthProvider = new GroundTruthProviderFileBased();

		originalRoleProvider.loadRoles();
		groundTruthProvider.loadData();

		groundTruthProvider.printStatistic();
		groundTruthProvider.printStatisticRolePhrase();
		groundTruthProvider.printStatisticHeadRole();
		
		
		final ExactMatchEvaluation exm = new ExactMatchEvaluation(originalRoleProvider,groundTruthProvider);
		final NerEvaluation nere = new NerEvaluation(originalRoleProvider,groundTruthProvider);
//		exm.DictionaryCompletenessHeadRoleTestCaseSensitive();
//		exm.DictionaryCompletenessHeadRoleTestCaseInsensitive();
//		exm.DictionaryCompletenessHeadRoleCategoryTestCaseSensitive();
//		exm.DictionaryCompletenessRolePhraseTestCaseSensitive();
//		exm.DictionaryCompletenessRolePhraseCategoryTestCaseSensitive();
		//exm.DictionaryCompletenessTestCaseInsensitive();
		//exm.roleDetectionTestCaseSensitive();
		//exm.roleDetectionTestCaseInSensitive();
		
		//exm.roleDitectionAndCategorizationTestCaseSensitive();
		//exm.roleDitectionAndCategorizationTestCaseInSensitive();
		
		//nere.DictionaryCompletenessHeadRoleTestCaseSensitive();
		//nere.DictionaryCompletenessRolePhraseTestCaseSensitive();
		
		nere.roleDetectionTestCaseSensitive();
	}

}
