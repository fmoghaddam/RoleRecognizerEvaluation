package dictionary;

import java.util.Arrays;
import java.util.HashSet;

import model.Category;
import model.Order;

public class RoleListProviderDummy extends RoleListProvider {

	@Override
	public void loadRoles() {
		// roleMap.put("mr. president", Category.PRESIDENT_TAG);
		// roleMap.put("President of US", Category.PRESIDENT_TAG);
		// roleMap.put("President of United States", Category.PRESIDENT_TAG);
		// roleMap.put("President", Category.PRESIDENT_TAG);
		// roleMap.put("office of president", Category.PRESIDENT_TAG);
		// roleMap.put("40th president of the united states",
		// Category.PRESIDENT_TAG);
		// roleMap.put("44th president of the united states",
		// Category.PRESIDENT_TAG);
		// roleMap.put("1st president of the united states",
		// Category.PRESIDENT_TAG);
		roleMapCaseSensitive.put("co-founders", new HashSet<>(Arrays.asList(Category.CEO_TAG)));
		roleMapCaseSensitive.put("president", new HashSet<>(Arrays.asList(Category.PRESIDENT_TAG)));
		roleMapCaseSensitive.put("The king", new HashSet<>(Arrays.asList(Category.KING_TAG, Category.CEO_TAG, Category.POPE_TAG)));
		roleMapCaseSensitive.put("AB", new HashSet<>(Arrays.asList(Category.KING_TAG, Category.CEO_TAG, Category.POPE_TAG)));
		roleMapCaseSensitive.put("king", new HashSet<>(Arrays.asList(Category.KING_TAG, Category.CEO_TAG)));
		// roleMap.put("queen", new
		// HashSet<>(Arrays.asList(Category.KING_TAG)));
		// roleMap.put("CEO", new HashSet<>(Arrays.asList(Category.CEO_TAG)));
		// roleMap.put("pope", new HashSet<>(Arrays.asList(Category.POPE_TAG)));
		// roleMap.put("POTOS", new
		// HashSet<>(Arrays.asList(Category.TOPIC_TAG)));

		sortBasedOnLength(Order.DESC);
	}
}
