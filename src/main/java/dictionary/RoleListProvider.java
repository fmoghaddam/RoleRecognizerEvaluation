package dictionary;

import java.util.LinkedHashMap;
import java.util.Map;
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
