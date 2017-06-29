package groundtruth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import model.Category;

public abstract class GroundTruthProvider {
	private static Logger LOG = Logger.getLogger(GroundTruthProvider.class);
	protected  List<GroundTruthFile> document = new ArrayList<>();
	
	public void loadData() {
	
	};
	
	protected Map<String, Set<Category>> extractInformation(final String dummyData) {
		return null;
	}
	
	protected void print() {
		for(GroundTruthFile entry: document){
			LOG.info(entry);
		}
	}

	public List<GroundTruthFile> getDocumnets() {
		return new ArrayList<>(document);
	}
	
	public void printStatistic(){
		final Map<Category, Integer> statistic = new HashMap<>();
		for (final GroundTruthFile groundTruth : document) {
			groundTruth.getRoles().forEach(p -> {
				final Category resolveCategory = Category.resolve(p.getXmlAttributes().get("type").toLowerCase());
				final Integer value = statistic.get(resolveCategory);
				if (value == null) {
					statistic.put(resolveCategory, 1);
				} else {
					statistic.put(resolveCategory, value + 1);
				}
			});
		}
		LOG.info("-------------------------------------");
		LOG.info("Number of Roles Statistic");
		LOG.info("-------------------------------------");
		long sum = 0;
		for (Entry<Category, Integer> entry : statistic.entrySet()) {
			LOG.info(entry.getKey() + "==" + entry.getValue());
			sum+=entry.getValue();
		}
		LOG.info("SUM=="+sum);
		LOG.info("-------------------------------------");
	}

	public void printStatisticRolePhrase(){
		final Map<Category, Map<String,Integer>> statistic = new HashMap<>();
		for (final GroundTruthFile groundTruth : document) {
			groundTruth.getRoles().forEach(p -> {
				final Category resolveCategory = Category.resolve(p.getXmlAttributes().get("type").toLowerCase());
				final Map<String, Integer> value = statistic.get(resolveCategory);
				final String rolePhrase = p.getRolePhrase().toLowerCase();
				if (value == null) {
					final Map<String,Integer> newMap = new HashMap<>();
					newMap.put(rolePhrase,1);
					statistic.put(resolveCategory, newMap);
				} else {
					
					final Integer integer = value.get(rolePhrase);
					if(integer==null){
						value.put(rolePhrase, 1);
					}else{
						value.put(rolePhrase, integer+1);	
					}
					statistic.put(resolveCategory, value);
				}
			});
		}
		LOG.info("-------------------------------------");
		LOG.info("Unique RolePhrase Statistic");
		LOG.info("-------------------------------------");
		for (Entry<Category, Map<String, Integer>> entry : statistic.entrySet()) {
			LOG.info(entry.getKey() + "==" + entry.getValue().size());
//			for(Entry<String, Integer> ent:entry.getValue().entrySet()){
//				LOG.info(ent.getKey() + "--" + ent.getValue());
//			}
		}
		LOG.info("-------------------------------------");
	}
}
