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
		
//		exm.dictionaryCompletenessHeadRoleTestCaseSensitive();
//		exm.dictionaryCompletenessHeadRoleCategoryTestCaseSensitive();
//		exm.dictionaryCompletenessRolePhraseTestCaseSensitive();
//		exm.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive();
//		exm.roleDetectionTestCaseSensitive();
//		exm.roleDitectionAndCategorizationTestCaseSensitive();
		
		
		final NerEvaluation nerEvaluator = new NerEvaluation(originalRoleProvider,groundTruthProvider);
//		nerEvaluator.dictionaryCompletenessHeadRoleTestCaseSensitive();
//		nerEvaluator.dictionaryCompletenessHeadRoleCategoryTestCaseSensitive();
//		nerEvaluator.dictionaryCompletenessRolePhraseTestCaseSensitive();
//		nerEvaluator.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive();
//		nerEvaluator.roleDetectionTestCaseSensitive();
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
