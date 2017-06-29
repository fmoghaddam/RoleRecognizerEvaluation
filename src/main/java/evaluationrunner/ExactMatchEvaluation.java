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
import dictionary.RoleListProviderFileBased;
import groundtruth.GroundTruthFile;
import groundtruth.GroundTruthProvider;
import groundtruth.GroundTruthProviderFileBased;
import metrics.FMeasure;
import metrics.Precision;
import metrics.Recall;
import model.Category;
import model.Role;
import model.TagPosition;
import model.TagPositions;

public class ExactMatchEvaluation {

	private static Logger LOG = Logger.getLogger(ExactMatchEvaluation.class);

	final Precision precision;
	final Recall recall;

	public ExactMatchEvaluation() {
		precision = new Precision();
		recall = new Recall();

		groundTruthProvider = new GroundTruthProviderFileBased();
		originalRoleProvider = new RoleListProviderFileBased();

		originalRoleProvider.loadRoles();
		groundTruthProvider.loadData();

		groundTruthProvider.printStatistic();
		groundTruthProvider.printStatisticRolePhrase();
	}

	final RoleListProvider originalRoleProvider;
	final GroundTruthProvider groundTruthProvider;

	/**
	 * Test dictionary completeness by checking all the roles from ground truth
	 * against the dictionary. This function use CASE SENSITIVE dictionary.
	 */
	public void DictionaryCompletenessTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			for (Role entry : groundTruthFile.getRoles()) {
				final Category category = Category.resolve(entry.getXmlAttributes().get("type"));
				final String candicateText = entry.getHeadRole();
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
		LOG.info("DictionaryCompletenessTest case sensitive:");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	/**
	 * Test dictionary completeness by checking all the roles from ground truth
	 * against the dictionary. This function use CASE INSENSITIVE dictionary.
	 */
	public void DictionaryCompletenessTestCaseInsensitive() {
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
		LOG.info("DictionaryCompletenessTest case INsensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	/**
	 * Just try to find a role and does not check it's category Case sensitive
	 */
	public void roleDetectionTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey();

				final Pattern pattern = Pattern.compile("(?im)" + dictionaryRole);
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
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							if (candicate.contains(role.getHeadRole())) {
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

	/**
	 * First try to find a role and then check it's category. Case sensitive
	 */
	public void roleDitectionAndCategorizationTestCaseSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseSensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey();
				final Set<Category> dictionaryCategories = roleEntity.getValue();

				final Pattern pattern = Pattern.compile("(?im)" + dictionaryRole);
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
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							if (candicate.contains(role.getHeadRole())) {
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
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		LOG.info("roleDitectionAndCategorizationTestCaseSensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	/**
	 * Just try to find a role and does not check it's category Case Insensitive
	 */
	public void roleDetectionTestCaseInSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseInsensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey();

				final Pattern pattern = Pattern.compile("(?im)" + dictionaryRole);
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
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							if (candicate.toLowerCase().contains(role.getHeadRole().toLowerCase())) {
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
		LOG.info("roleDetectionTestCaseInSensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
		LOG.info("--------------------------------------------");
	}

	/**
	 * First try to find a role and then check it's category. Case Insensitive
	 */
	public void roleDitectionAndCategorizationTestCaseInSensitive() {
		resetMetrics();
		for (final GroundTruthFile groundTruthFile : groundTruthProvider.getDocumnets()) {
			final TagPositions tagPositions = new TagPositions();
			final String originalFullText = groundTruthFile.getFullContentPlain();
			final List<Role> groundTruthFileCopy = groundTruthFile.getRoles();
			final List<Role> groundTruthFileCopyTemp = groundTruthFile.getRoles();
			for (final Entry<String, Set<Category>> roleEntity : originalRoleProvider.getRoleMapCaseInsensitive()
					.entrySet()) {

				final String dictionaryRole = roleEntity.getKey();
				final Set<Category> dictionaryCategories = roleEntity.getValue();

				final Pattern pattern = Pattern.compile("(?im)" + dictionaryRole);
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
					for (final Role role : groundTruthFileCopy) {
						if (candicatePosition.hasOverlap(role.getRolePhasePosition())) {
							if (candicate.toLowerCase().contains(role.getHeadRole().toLowerCase())) {
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
						precision.addFalsePositive();
					}
				}
			}
			for (int i = 0; i < groundTruthFileCopyTemp.size(); i++) {
				recall.addFalseNegative();
			}
		}
		LOG.info("roleDitectionAndCategorizationTestCaseInSensitive :");
		LOG.info("Precision= " + precision.getValue());
		LOG.info("Recall= " + recall.getValue());
		LOG.info("FMeasure= " + new FMeasure(precision.getValue(), recall.getValue()).getValue());
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
