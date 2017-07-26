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
		
		originalRoleProvider.printFullStatisticCaseSensitive();

		final boolean useRolePhrase = false;
		
//		final ExactMatchEvaluation exm = new ExactMatchEvaluation(originalRoleProvider,groundTruthProvider);
//		exm.dictionaryCompletenessHeadRoleTestCaseSensitive();
//		exm.dictionaryCompletenessHeadRoleCategoryTestCaseSensitive();
//		exm.dictionaryCompletenessRolePhraseTestCaseSensitive();
//		exm.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive();
//		exm.roleDetectionTestCaseSensitive(useRolePhrase);
//		exm.roleDetectionAndCategorizationTestCaseSensitive(useRolePhrase);
//		exm.roleDetectionTestCaseInSensitive(useRolePhrase);
//		exm.roleDetectionAndCategorizationTestCaseInSensitive(useRolePhrase);
		
		final NerEvaluation nerEvaluatorOnlyRule = new NerEvaluation(originalRoleProvider,groundTruthProvider);
		nerEvaluatorOnlyRule.dictionaryCompletenessHeadRoleTestCaseSensitive(true);
//		nerEvaluatorOnlyRule.dictionaryCompletenessHeadRoleCategoryTestCaseSensitive(true);
//		nerEvaluatorOnlyRule.dictionaryCompletenessRolePhraseTestCaseSensitive(true);
//		nerEvaluatorOnlyRule.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive(true);
//		nerEvaluatorOnlyRule.roleDetectionTestCaseSensitive(useRolePhrase,true);
//		nerEvaluatorOnlyRule.roleDetectionTestCategoryCaseSensitive(useRolePhrase,true);		
//		nerEvaluatorOnlyRule.roleDetectionTestCaseInsensitive(useRolePhrase,true);
//		nerEvaluatorOnlyRule.roleDetectionTestCategoryCaseInsensitive(useRolePhrase,true);

		final NerEvaluation nerEvaluator = new NerEvaluation(originalRoleProvider,groundTruthProvider);
		nerEvaluator.dictionaryCompletenessHeadRoleTestCaseSensitive(false);
//		nerEvaluator.dictionaryCompletenessHeadRoleCategoryTestCaseSensitive(false);
//		nerEvaluator.dictionaryCompletenessRolePhraseTestCaseSensitive(false);
//		nerEvaluator.dictionaryCompletenessRolePhraseCategoryTestCaseSensitive(false);
//		nerEvaluator.roleDetectionTestCaseSensitive(useRolePhrase,false);
//		nerEvaluator.roleDetectionTestCategoryCaseSensitive(useRolePhrase,false);		
//		nerEvaluator.roleDetectionTestCaseInsensitive(useRolePhrase,false);
//		nerEvaluator.roleDetectionTestCategoryCaseInsensitive(useRolePhrase,false);
	}

}
