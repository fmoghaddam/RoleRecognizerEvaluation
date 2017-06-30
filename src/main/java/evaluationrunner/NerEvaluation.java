package evaluationrunner;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.log4j.Logger;

import dictionary.RoleListProvider;
import groundtruth.GroundTruthFile;
import groundtruth.GroundTruthProvider;
import metrics.FMeasure;
import metrics.Precision;
import metrics.Recall;
import model.Category;
import model.NerTag;
import model.Position;
import model.Role;
import model.TagPosition;
import model.TagPositions;
import tagger.NERTagger;

public class NerEvaluation {

	private static Logger LOG = Logger.getLogger(NerEvaluation.class);

	private final Precision precision;
	private final Recall recall;

	private final RoleListProvider originalRoleProvider;
	private final GroundTruthProvider groundTruthProvider;

	public NerEvaluation(final RoleListProvider rlp, final GroundTruthProvider gtp) {
		if(rlp == null){
			throw new IllegalArgumentException("RoleListProvider is null");
		}
		if(gtp == null){
			throw new IllegalArgumentException("GroundTruthProvider is null");
		}
		precision = new Precision();
		recall = new Recall();
		groundTruthProvider = gtp.getCopy();
		originalRoleProvider = rlp;
	}

	public void DictionaryCompletenessHeadRoleTestCaseSensitive() {
		resetMetrics();
		final Map<String, Set<Category>> generatedNerDictionary = generateNerDictionary(originalRoleProvider.getRoleMapCaseSensitive());
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getHeadRole();

				final String convretedToNerText = NERTagger.runTaggerString(candicateText);

				final Set<Category> categories = generatedNerDictionary.get(convretedToNerText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					final boolean hasIntesection = categories.contains(category);
					if (hasIntesection) {
						precision.addTruePositive();
						recall.addTruePositive();
					} else {
						precision.addFalsePositive();
					}
				}
			}

		}
		LOG.info("DictionaryCompletenessHeadRoleTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	public void DictionaryCompletenessRolePhraseTestCaseSensitive() {
		resetMetrics();
		final Map<String, Set<Category>> generatedNerDictionary = generateNerDictionary(originalRoleProvider.getRoleMapCaseSensitive());
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getRolePhrase();

				final String convretedToNerText = NERTagger.runTaggerString(candicateText);

				final Set<Category> categories = generatedNerDictionary.get(convretedToNerText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					final boolean hasIntesection = categories.contains(category);
					if (hasIntesection) {
						precision.addTruePositive();
						recall.addTruePositive();
					} else {
						precision.addFalsePositive();
					}
				}
			}

		}
		LOG.info("DictionaryCompletenessRolePhraseTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}


	public void roleDetectionTestCaseSensitive() {
		resetMetrics();
		final Map<String, Set<Category>> generatedNerDictionary = generateNerDictionary(originalRoleProvider.getRoleMapCaseSensitive());
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			
			groundTruthFile.executeFullContentNer(originalRoleProvider.getOnlyHeadRoleMap().keySet());
			groundTruthFile.executeFullContentNerStatistic(originalRoleProvider.getOnlyHeadRoleMap().keySet());

			final TagPositions tagPositions = new TagPositions();
			final String nerTaggedFullText = groundTruthFile.getFullContentNerTagged();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : generatedNerDictionary
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey();

				final Pattern pattern = Pattern.compile("(?m)" + dictionaryRole);
				final Matcher matcher = pattern.matcher(nerTaggedFullText);

				while (matcher.find()) {
					final String candicate = matcher.group(0);
					final TagPosition candicatePosition = new TagPosition(candicate, matcher.start(), matcher.end());
					if (tagPositions.alreadyExist(candicatePosition)) {
						continue;
					}
					groundTruthFileCopy.clear();
					groundTruthFileCopy.addAll(groundTruthFileCopyTemp);
					boolean found = false;
					TagPosition convertedPosition = convertPosition(candicatePosition,groundTruthFile);
					for (final Role role : groundTruthFileCopy) {
						if (convertedPosition.hasOverlap(role.getRolePhasePosition())) {
							if (candicatePosition.contains(role.getHeadRolePosition())) {
								precision.addTruePositive();
								recall.addTruePositive();
								groundTruthFileCopyTemp.remove(role);
								found = true;
								tagPositions.add(candicatePosition);
								break;
							} else {
								precision.addFalsePositive();
								found = true;
								break;
							}
						}
					}
					if (!found) {
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		LOG.info("roleDetectionTestCaseSensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	private TagPosition convertPosition(TagPosition candicatePosition, GroundTruthFile groundTruthFile) {
		final Map<Integer, NerTag> statistic = groundTruthFile.getFullContentNerTaggedStatistic();
		int offset = 0;
		for(Entry<Integer, NerTag> entry:statistic.entrySet()){
			
			if((entry.getKey()+offset)==candicatePosition.getStartIndex()){				
				final TagPosition result = new TagPosition(entry.getValue().getWord(),entry.getKey(), entry.getValue().getEndPosition());
				return result; 
			}else{
				final NerTag tag = entry.getValue();
				int diff = tag.getEndPosition()-tag.getStartPosition();
				int tagLength = 2+tag.getNerTag().text.length();
				if(diff>=tagLength){
					offset += Math.abs(diff-tagLength);
				}else{
					offset -= Math.abs(diff-tagLength);
				}
			}
			System.err.println(entry.getValue().getStartPosition()+" "+entry.getValue().getEndPosition()+" "+entry.getValue().getWord()+" "+offset);
		}

		return null;
	}

	private Map<String, Set<Category>> generateNerDictionary(Map<String, Set<Category>> originalDictionary) {
		final Map<String, Set<Category>> nerDictinary = new LinkedHashMap<>();

		for (Entry<String, Set<Category>> entry : originalDictionary.entrySet()) {
			final String text = entry.getKey();
			final Set<Category> categories = entry.getValue();
			try {
				final String nerTaggedResult = NERTagger.runTaggerStringWithoutHeadRoleReplacement(text,originalRoleProvider.getOnlyHeadRoleMap().keySet());
				final Set<Category> set = nerDictinary.get(nerTaggedResult);
				if (set == null) {
					nerDictinary.put(nerTaggedResult, new HashSet<>(categories));
				} else {
					Set<Category> newSet = new HashSet<>(set);
					newSet.addAll(categories);
					nerDictinary.put(nerTaggedResult, newSet);
				}
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		return nerDictinary;
	}

	private void resetMetrics() {
		precision.reset();
		recall.reset();
	}
}