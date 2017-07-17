package dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import model.Category;
import model.Order;

public class RoleListProviderFileBased extends RoleListProvider {

	private static final String DATA_FOLDER = "data";

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.RoleListProvider#loadRoles()
	 */
	@Override
	public void loadRoles() {
		try {
			final File[] listOfFiles = new File(DATA_FOLDER).listFiles();
			for (int j = 0; j < listOfFiles.length; j++) {
				final String file = listOfFiles[j].getName();
				BufferedReader br = new BufferedReader(new FileReader(DATA_FOLDER + File.separator + file));
				String line;
				while ((line = br.readLine()) != null) {
					final Set<Category> categorySet = roleMapCaseSensitive.get(line);
					if (categorySet == null || categorySet.isEmpty()) {
						final Set<Category> catSet = new HashSet<>();
						catSet.add(Category.resolve(file));
						roleMapCaseSensitive.put(line, catSet);
					} else {
						categorySet.add(Category.resolve(file));
						roleMapCaseSensitive.put(line, categorySet);
					}
				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sortBasedOnLength(Order.DESC);
		
		for(Entry<String, Set<Category>> a:roleMapCaseSensitive.entrySet()){
			final String role = a.getKey();
			final Set<Category> set = a.getValue();
			final Set<Category> newSet = roleMapCaseInsensitive.get(role);
			if(newSet==null){
				roleMapCaseInsensitive.put(role, set);
			}else{
				newSet.addAll(set);
				roleMapCaseInsensitive.put(role, set);
			}
		}
		
		sortBasedOnLengthCaseInsensitive(Order.DESC);
		
		for(Entry<String, Set<Category>> entry:roleMapCaseSensitive.entrySet()){
			if(entry.getKey().split(" ").length==1){
				onlyHeadRoleMap.put(entry.getKey(), entry.getValue());
			}
		}
		onlyHeadRoleMapInsensitive.putAll(new TreeMap<>(onlyHeadRoleMap));
	}
}
