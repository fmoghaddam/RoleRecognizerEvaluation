package evaluationrunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import dictionary.RoleListProvider;
import groundtruth.GroundTruthFile;
import groundtruth.GroundTruthProvider;
import metrics.FMeasure;
import metrics.Precision;
import metrics.Recall;
import model.Category;
import model.Position;
import model.Role;
import model.TagPosition;
import model.TagPositions;

public class ExactMatchEvaluation {

	private static Logger LOG = Logger.getLogger(ExactMatchEvaluation.class);

	private final Precision precision;
	private final Recall recall;

	private final RoleListProvider originalRoleProvider;
	private final GroundTruthProvider groundTruthProvider;

	public ExactMatchEvaluation(final RoleListProvider rlp, final GroundTruthProvider gtp) {
		if(rlp == null){
			throw new IllegalArgumentException("RoleListProvider is null");
		}
		if(gtp == null){
			throw new IllegalArgumentException("GroundTruthProvider is null");
		}
		precision = new Precision();
		recall = new Recall();
		groundTruthProvider = gtp;
		originalRoleProvider = rlp;
	}

	/**
	 * Test dictionary completeness by checking all the roles from ground truth
	 * against the dictionary. This function use CASE SENSITIVE dictionary.
	 */
	public void dictionaryCompletenessHeadRoleTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final String candicateText = entry.getHeadRole();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseSensitive().get(candicateText);
				if (categories == null) {
					recall.addFalseNegative();
					//LOG.info(candicateText);
				} else {
					precision.addTruePositive();
					recall.addTruePositive();
				}
			}

		}
		//LOG.info("DictionaryCompletenessHeadRoleTest case sensitive:");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessHeadRoleCategoryTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getHeadRole();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseSensitive().get(candicateText);
				if (categories == null) {
					recall.addFalseNegative();
					//System.err.println(candicateText);
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
		//LOG.info("DictionaryCompletenessHeadRoleCategoryTest case sensitive:");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessRolePhraseTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final String candicateText = entry.getRolePhrase();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseSensitive().get(candicateText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					precision.addTruePositive();
					recall.addTruePositive();
				}
			}

		}
		//LOG.info("DictionaryCompletenessRolePhrase Test case sensitive:");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessRolePhraseCategoryTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getRolePhrase();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseSensitive().get(candicateText);
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
		//LOG.info("DictionaryCompletenessRolePhraseCategoryTest case sensitive:");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}
	
	public void dictionaryCompletenessRolePhraseCategoryTestCaseInsensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getRolePhrase();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseInsensitive().get(candicateText);
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
		LOG.info("DictionaryCompletenessRolePhraseCategoryTest case INsensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		LOG.info("FMeasure= " + fmeasure);
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	/**
	 * Test dictionary completeness by checking all the roles from ground truth
	 * against the dictionary. This function use CASE INSENSITIVE dictionary.
	 */
	public void dictionaryCompletenessHeadRoleTestCaseInsensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final String candicateText = entry.getHeadRole();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseInsensitive().get(candicateText);
				if (categories == null) {
					recall.addFalseNegative();
				} else {
					precision.addTruePositive();
					recall.addTruePositive();
				}
			}

		}
		LOG.info("DictionaryCompletenessHeadRoleTest case INsensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		//LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessHeadRoleCategoryTestCaseInsensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getHeadRole();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseInsensitive().get(candicateText);
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
		LOG.info("DictionaryCompletenessHeadRoleCategoryTest case INsensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		//LOG.info("--------------------------------------------");
	}

	public void dictionaryCompletenessRolePhraseTestCaseInsensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getRolePhrase();
				final Set<Category> categories = originalRoleProvider.getRoleMapCaseInsensitive().get(candicateText);
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
		LOG.info("DictionaryCompletenessRolePhrase Test case INsensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		//LOG.info("--------------------------------------------");
	}

	/**
	 * Just try to find a role and does not check it's category Case sensitive
	 */
	public void roleDetectionTestCaseSensitive(final boolean rolePhrase) {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey().replaceAll("\\.", "\\\\.");

				final Pattern pattern = Pattern.compile("(?m)" + "\\b"+dictionaryRole+ "\\b");
				final Matcher matcher = pattern.matcher(originalFullText);

				while (matcher.find()) {
					final String candicate = matcher.group(0);
					final TagPosition candicatePosition = new TagPosition(candicate, matcher.start(), matcher.end());
					if (tagPositions.alreadyExist(candicatePosition)) {
						continue;
					}
					groundTruthFileCopy.clear();
					groundTruthFileCopy.addAll(groundTruthFileCopyTemp);
					boolean found = false;
					tagPositions.add(candicatePosition);
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							Position position;
							if(rolePhrase){
								position=role.getRolePhasePosition();
							}else{
								position=role.getHeadRolePosition();
							}
							if (candicatePosition.contains(position)) {
								precision.addTruePositive();
								recall.addTruePositive();
								groundTruthFileCopyTemp.remove(role);
								found = true;
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
		//LOG.info("roleDetectionTestCaseSensitive :");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		//LOG.info(precision.getFalsePositive());
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	/**
	 * First try to find a role and then check it's category. Case sensitive
	 */
	public void roleDetectionAndCategorizationTestCaseSensitive(final boolean rolePhrase) {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey().replaceAll("\\.", "\\\\.");
				final Set<Category> dictionaryCategories = roleEntity.getValue();

				final Pattern pattern = Pattern.compile("(?m)" + "\\b"+dictionaryRole+ "\\b");
				final Matcher matcher = pattern.matcher(originalFullText);

				while (matcher.find()) {
					final String candicate = matcher.group(0);
					final TagPosition candicatePosition = new TagPosition(candicate, matcher.start(), matcher.end());
					if (tagPositions.alreadyExist(candicatePosition)) {
						continue;
					}
					groundTruthFileCopy.clear();
					groundTruthFileCopy.addAll(groundTruthFileCopyTemp);
					boolean found = false;
					tagPositions.add(candicatePosition);
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							Position position;
							if(rolePhrase){
								position=role.getRolePhasePosition();
							}else{
								position=role.getHeadRolePosition();
							}
							if (candicatePosition.contains(position)) {
								final Category category = Category.resolve(role.getXmlAttributes().get("type"));
								final Set<Category> intesection = hasIntersection(
										new HashSet<>(Arrays.asList(category)), dictionaryCategories);
								if (intesection != null && !intesection.isEmpty()) {
									precision.addTruePositive();
									recall.addTruePositive();
									groundTruthFileCopyTemp.remove(role);
									found = true;
									
									for(int i=0;i<dictionaryCategories.size()-1;i++){
										precision.addFalsePositive();
									}
									
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
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		//LOG.info("roleDitectionAndCategorizationTestCaseSensitive :");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		//LOG.info(precision.getFalsePositive());
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");
	}

	/**
	 * Just try to find a role and does not check it's category Case Insensitive
	 */
	public void roleDetectionTestCaseInSensitive(final boolean rolePhrase) {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			//I have no idea here, which dictionary should be used? case sensitive or case insensitive
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey().replaceAll("\\.", "\\\\.");

				final Pattern pattern = Pattern.compile("(?im)" + "\\b"+dictionaryRole+ "\\b");
				final Matcher matcher = pattern.matcher(originalFullText);

				while (matcher.find()) {
					final String candicate = matcher.group(0);
					final TagPosition candicatePosition = new TagPosition(candicate, matcher.start(), matcher.end());
					if (tagPositions.alreadyExist(candicatePosition)) {
						continue;
					}
					groundTruthFileCopy.clear();
					groundTruthFileCopy.addAll(groundTruthFileCopyTemp);
					boolean found = false;
					tagPositions.add(candicatePosition);
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							Position position;
							if(rolePhrase){
								position=role.getRolePhasePosition();
							}else{
								position=role.getHeadRolePosition();
							}
							if (candicatePosition.contains(position)) {
								precision.addTruePositive();
								recall.addTruePositive();
								groundTruthFileCopyTemp.remove(role);
								found = true;
								
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
		//LOG.info("roleDetectionTestCaseInSensitive :");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		//LOG.info(precision.getFalsePositive());
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		//LOG.info("--------------------------------------------");		
	}

	/**
	 * First try to find a role and then check it's category. Case Insensitive
	 */
	public void roleDetectionAndCategorizationTestCaseInSensitive(final boolean rolePhrase) {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			//I have no idea here, which dictionary should be used? case sensitive or case insensitive
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey().replaceAll("\\.", "\\\\.");
				final Set<Category> dictionaryCategories = roleEntity.getValue();

				final Pattern pattern = Pattern.compile("(?im)" + "\\b"+dictionaryRole+ "\\b");
				final Matcher matcher = pattern.matcher(originalFullText);

				while (matcher.find()) {
					final String candicate = matcher.group(0);
					final TagPosition candicatePosition = new TagPosition(candicate, matcher.start(), matcher.end());
					if (tagPositions.alreadyExist(candicatePosition)) {
						continue;
					}
					groundTruthFileCopy.clear();
					groundTruthFileCopy.addAll(groundTruthFileCopyTemp);
					boolean found = false;
					tagPositions.add(candicatePosition);
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							Position position;
							if(rolePhrase){
								position=role.getRolePhasePosition();
							}else{
								position=role.getHeadRolePosition();
							}
							if (candicatePosition.contains(position)) {
								final Category category = Category.resolve(role.getXmlAttributes().get("type"));
								final Set<Category> intesection = hasIntersection(
										new HashSet<>(Arrays.asList(category)), dictionaryCategories);
								if (intesection != null && !intesection.isEmpty()) {
									precision.addTruePositive();
									recall.addTruePositive();
									groundTruthFileCopyTemp.remove(role);
									found = true;
									for(int i=0;i<dictionaryCategories.size()-1;i++){
										precision.addFalsePositive();
									}
									
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
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		//LOG.info("roleDitectionAndCategorizationTestCaseInSensitive :");
		//LOG.info("Precision= " + precision.getValue());
		//LOG.info("Recall= " + recall.getValue());
		final double fmeasure = new FMeasure(precision.getValue(), recall.getValue()).getValue();
		//LOG.info("FMeasure= " + fmeasure);
		//LOG.info(precision.getFalsePositive());
		LOG.info("=SPLIT(\""+precision.getValue()+","+recall.getValue()+","+fmeasure+"\",\",\""+")");
		LOG.info("--------------------------------------------");
	}

	private void resetMetrics() {
		precision.reset();
		recall.reset();
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
}
