package evaluationrunner;

import java.util.Arrays;
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
import model.Order;
import model.Role;
import model.TagPosition;
import model.TagPositions;
import tagger.NERTagger;
import util.MapUtil;

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

	public void dictionaryCompletenessHeadRoleCategoryTestCaseSensitive() {
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
		LOG.info("DictionaryCompletenessHeadRoleCategoryTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessHeadRoleTestCaseSensitive() {
		resetMetrics();
		final Map<String, Set<Category>> generatedNerDictionary = generateNerDictionary(originalRoleProvider.getRoleMapCaseSensitive());
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final String candicateText = entry.getHeadRole();

				final String convretedToNerText = NERTagger.runTaggerString(candicateText);

				final Set<Category> categories = generatedNerDictionary.get(convretedToNerText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					precision.addTruePositive();
					recall.addTruePositive();
				}
			}

		}
		LOG.info("DictionaryCompletenessHeadRoleTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessRolePhraseCategoryTestCaseSensitive() {
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
		LOG.info("DictionaryCompletenessRolePhraseCategoryTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessRolePhraseTestCaseSensitive() {
		resetMetrics();
		final Map<String, Set<Category>> generatedNerDictionary = generateNerDictionary(originalRoleProvider.getRoleMapCaseSensitive());
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final String candicateText = entry.getRolePhrase();

				final String convretedToNerText = NERTagger.runTaggerString(candicateText);

				final Set<Category> categories = generatedNerDictionary.get(convretedToNerText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					precision.addTruePositive();
					recall.addTruePositive();
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

				final Pattern pattern = Pattern.compile("(?m)" +dictionaryRole);
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
							if (convertedPosition.contains(role.getHeadRolePosition())) {
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
	
	public void roleDetectionTestCategoryCaseSensitive() {
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

				final String dictionaryRole = roleEntity.getKey().replaceAll("\\.", "\\\\.");
				if(dictionaryRole.charAt(0)=='<' && dictionaryRole.charAt(dictionaryRole.length()-1)=='>'){
					continue;
				}
				if(dictionaryRole.equalsIgnoreCase("the <LOCATION>") || 
						dictionaryRole.equalsIgnoreCase("The <ORGANIZATION>")){
					
					
					continue;
				}
				String regexPattern = "(?m)";
				if(dictionaryRole.charAt(0)!='<'){
					regexPattern +="\\b";
				}
				regexPattern +=dictionaryRole;
				if(dictionaryRole.charAt(dictionaryRole.length()-1)!='>'){
					regexPattern +="\\b";
				}
				
				final Set<Category> dictionaryCategories = roleEntity.getValue();
				
				final Pattern pattern = Pattern.compile("(?m)" +regexPattern);
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
							if (convertedPosition.contains(role.getHeadRolePosition())) {
								final Category category = Category.resolve(role.getXmlAttributes().get("type"));
								final Set<Category> intesection = hasIntersection(
										new HashSet<>(Arrays.asList(category)), dictionaryCategories);
								if (intesection != null && !intesection.isEmpty()) {
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
							} else {
								precision.addFalsePositive();
								found = true;
								break;
							}
						}
					}
					if (!found) {
//						if(candicate.contains("<")){
//							System.err.println(candicate+"---"+dictionaryRole+"--"+dictionaryCategories+"--"+groundTruthFile.getTitle());
//						}
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		LOG.info("roleDetectionTestCategoryCaseSensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	private Set<Category> hasIntersection(Set<Category> collect, Set<Category> dictionaryCategories) {
		Set<Category> intersection = new HashSet<>();
		for (Category cat : collect) {
			if (dictionaryCategories.contains(cat)) {
				intersection.add(cat);
			}
		}
		return intersection;
	}
	
	private TagPosition convertPosition(TagPosition candicatePosition, GroundTruthFile groundTruthFile) {
		final Map<Integer, NerTag> statistic = groundTruthFile.getFullContentNerTaggedStatistic();
		final int staticOffset = candicatePosition.getTag().indexOf('<'); 
		int offset = 0;
		if(staticOffset==-1){
			for(Entry<Integer, NerTag> entry:statistic.entrySet()){
				if((entry.getKey()-offset)>candicatePosition.getStartIndex()){				
					final int start = candicatePosition.getStartIndex()+offset;
					final TagPosition result = new TagPosition(candicatePosition.getTag(),start, start+candicatePosition.getLength());
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
			}
			final int start = candicatePosition.getStartIndex()+offset;
			final TagPosition result = new TagPosition(candicatePosition.getTag(),start, start+candicatePosition.getLength());
			return result;
		}
		else{
			for(Entry<Integer, NerTag> entry:statistic.entrySet()){
				//System.err.println((entry.getKey()+offset)+"--"+(candicatePosition.getStartIndex()+staticOffset));
				//System.err.println(entry.getKey()-offset+"--"+(entry.getKey()-offset+entry.getValue().getNerTag().text.length()+2)+"--"+entry.getValue().getNerTag());
				if((entry.getKey()-offset)==candicatePosition.getStartIndex()+staticOffset){				
					final int start = entry.getKey()-staticOffset;
					final TagPosition result = new TagPosition(candicatePosition.getTag(),start, start+candicatePosition.getLength()-1);
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
				//System.err.println(entry.getValue().getStartPosition()+" "+entry.getValue().getEndPosition()+" "+entry.getValue().getWord()+" "+offset);
			}
		}

		System.err.println(candicatePosition);
		System.err.println(groundTruthFile.getTitle());
		return null;
	}

	private Map<String, Set<Category>> generateNerDictionary(Map<String, Set<Category>> originalDictionary) {
		Map<String, Set<Category>> nerDictinary = new LinkedHashMap<>();

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
		
		nerDictinary = MapUtil.sortByKeyDescendingNumberOfWords(nerDictinary);
		
		return nerDictinary;
	}

	private void resetMetrics() {
		precision.reset();
		recall.reset();
	}
}
