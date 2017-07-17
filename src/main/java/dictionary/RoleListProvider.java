package dictionary;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import model.Category;
import model.Order;
import util.MapUtil;

public abstract class RoleListProvider {
	private static Logger LOG = Logger.getLogger(RoleListProvider.class);
	protected Map<String, Set<Category>> roleMapCaseSensitive = new LinkedHashMap<>();
	protected Map<String, Set<Category>> onlyHeadRoleMap = new LinkedHashMap<>();
	protected Map<String, Set<Category>> roleMapCaseInsensitive = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	protected Map<String, Set<Category>> onlyHeadRoleMapInsensitive = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * This function should first load the roles into the map, then sort the map
	 * in a descending mode regards to the length of the text
	 */
	public void loadRoles() {
	};

	public void print() {
		for (String s : roleMapCaseSensitive.keySet()) {
			LOG.info(s);
		}
	}

	public void printFullStatistic() {
		LOG.info("Size of Dictionary = "+roleMapCaseSensitive.size());
		int ceoCount = 0;
		int popeCount = 0;
		int presidentCount = 0;
		int kingCount = 0;
		int presidentPopeCount = 0;
		int presidentCEOCount = 0;
		int presidentKingCount = 0;
		int ceoKingCount = 0;
		int popeCeoCount = 0;
		int popeKingCount = 0;
		int presdientPopeCeo = 0;
		int presdientPopeKing = 0;
		int popeCeoKing = 0;
		int presdientCeoKing = 0;
		int size = 0;
		for (Entry<String, Set<Category>> entry : roleMapCaseSensitive.entrySet()) {
			Set<Category> categories = entry.getValue();
			if(categories.size()>size){
				size= categories.size();
			}
			if(categories.contains(Category.CEO_TAG)){
				ceoCount++;
			}
			if(categories.contains(Category.KING_TAG)){
				kingCount++;
			}
			if(categories.contains(Category.POPE_TAG)){
				popeCount++;
			}
			if(categories.contains(Category.PRESIDENT_TAG)){
				presidentCount++;
			}

			if(categories.size()==2){
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.POPE_TAG)){
					presidentPopeCount++;
					System.err.println("PRESDIENT POPE " +entry.getKey());
				}
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.CEO_TAG)){
					presidentCEOCount++;
					System.err.println("PRESIDENT CEO " +entry.getKey());
				}
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.KING_TAG)){
					presidentKingCount++;
					System.err.println("PRESDIDENT KING " +entry.getKey());
				}
				if(categories.contains(Category.POPE_TAG) && categories.contains(Category.CEO_TAG)){
					popeCeoCount++;
					System.err.println("POEP CEO " +entry.getKey());
				}
				if(categories.contains(Category.POPE_TAG) && categories.contains(Category.KING_TAG)){
					popeKingCount++;
					System.err.println("POPE KING " +entry.getKey());
				}
				if(categories.contains(Category.CEO_TAG) && categories.contains(Category.KING_TAG)){
					ceoKingCount++;
					System.err.println("CEO KING " +entry.getKey());
				}
			}
			if(categories.size()==3){
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.POPE_TAG) && categories.contains(Category.CEO_TAG)){
					presdientPopeCeo++;
					System.err.println("PRESIDENT POPE CEO " +entry.getKey());
				}
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.POPE_TAG) && categories.contains(Category.KING_TAG)){
					System.err.println("PRESIDENT POPE KING " +entry.getKey());
					presdientPopeKing++;
				}
				if(categories.contains(Category.POPE_TAG) && categories.contains(Category.CEO_TAG) && categories.contains(Category.KING_TAG)){
					System.err.println("POPE CEO KING " +entry.getKey());
					popeCeoKing++;
				}
				if(categories.contains(Category.PRESIDENT_TAG) && categories.contains(Category.CEO_TAG) && categories.contains(Category.KING_TAG)){
					System.err.println("PRESIDENT CEO KING" +entry.getKey());
					presdientCeoKing++;
				}
			}
		}
		LOG.info("MAX OVERLAP SIZE = "+size);
		LOG.info("Total values realted to CEO category = "+ceoCount);
		LOG.info("Total values realted to KING category = "+kingCount);
		LOG.info("Total values realted to PRESIDENT category = "+presidentCount);
		LOG.info("Total values realted to POPE category = "+popeCount);
		LOG.info("Total values realted to PRESDIENT AND POPE category = "+presidentPopeCount);
		LOG.info("Total values realted to PRESDIENT AND CEO category = "+presidentCEOCount);
		LOG.info("Total values realted to PRESDIENT AND KING category = "+presidentKingCount);
		LOG.info("Total values realted to POPE AND CEO category = "+popeCeoCount);
		LOG.info("Total values realted to POPE AND KING category = "+popeKingCount);
		LOG.info("Total values realted to CEO AND KING category = "+ceoKingCount);
		LOG.info("Total values realted to PRESIDENT AND POPE AND CEO category = "+presdientPopeCeo);
		LOG.info("Total values realted to PRESIDENT AND POPE AND KING  category = "+presdientPopeKing);
		LOG.info("Total values realted to POPE AND CEO AND KING  category = "+popeCeoKing);
		LOG.info("Total values realted to PRESDIENT AND CEO AND KING  category = "+presdientCeoKing);
	}

	public Map<String, Set<Category>> getRoleMapCaseSensitive() {
		return roleMapCaseSensitive;
	}

	protected void sortBasedOnLength(Order order) {
		switch (order) {
		case ASC:
			roleMapCaseSensitive = MapUtil.sortByKeyAscending(roleMapCaseSensitive);
			break;
		case DESC:
			roleMapCaseSensitive = MapUtil.sortByKeyDescending(roleMapCaseSensitive);
			break;
		default:
			break;
		}
	}

	protected void sortBasedOnLengthCaseInsensitive(Order order) {
		switch (order) {
		case ASC:
			roleMapCaseInsensitive = MapUtil.sortByKeyAscendingTreeMap(roleMapCaseInsensitive);
			break;
		case DESC:
			roleMapCaseInsensitive = MapUtil.sortByKeyDescendingTreeMap(roleMapCaseInsensitive);
			break;
		default:
			break;
		}
	}

	public Map<String, Set<Category>> getOnlyHeadRoleMap() {
		return onlyHeadRoleMap;
	}

	public Map<String, Set<Category>> getRoleMapCaseInsensitive() {
		return roleMapCaseInsensitive;
	}

	public Map<String, Set<Category>> getOnlyHeadRoleMapInsensitive() {
		return onlyHeadRoleMapInsensitive;
	}

}
