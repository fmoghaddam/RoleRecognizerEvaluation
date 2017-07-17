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

//		groundTruthProvider.printStatistic();
//		groundTruthProvider.printStatisticRolePhrase();
//		groundTruthProvider.printStatisticHeadRole();
		
//		originalRoleProvider.printFullStatisticCaseInSensitive();
		
		
		final ExactMatchEvaluation exm = new ExactMatchEvaluation(originalRoleProvider,groundTruthProvider);
		
//		exm.dictionaryCompletenessHeadRoleTestCaseInsensitive();
//		exm.dictionaryCompletenessHeadRoleCategoryTestCaseInsensitive();
//		exm.dictionaryCompletenessRolePhraseTestCaseInsensitive();
//		exm.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive();
//		exm.roleDetectionTestCaseInSensitive();
//		exm.roleDitectionAndCategorizationTestCaseInSensitive();
		exm.roleDitectionAndCategorizationTestCaseSensitive();
		
		
		final NerEvaluation nerEvaluator = new NerEvaluation(originalRoleProvider,groundTruthProvider);
//		nerEvaluator.dictionaryCompletenessHeadRoleTestCaseInsensitive();
//		nerEvaluator.dictionaryCompletenessHeadRoleCategoryTestCaseInsensitive();
//		nerEvaluator.dictionaryCompletenessRolePhraseTestCaseInsensitive();
//		nerEvaluator.dictionaryCompletenessRolePhraseCategoryTestCaseInsensitive();
//		nerEvaluator.roleDetectionTestCaseInsensitive();
		nerEvaluator.roleDetectionTestCategoryCaseSensitive();		
		
		//nere.roleDetectionTestCaseSensitive();
		
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
		
		
	}

}
