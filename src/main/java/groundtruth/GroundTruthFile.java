package groundtruth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.NerTag;
import model.Position;
import model.Role;
import tagger.NERTagger;

public class GroundTruthFile {

	private String time;
	private String title;
	private List<Role> roles = new ArrayList<>();
	private String fullContentPlain;
	private String fullContentNerTagged;
	private Map<Integer, NerTag> fullContentNerTaggedStatistic;

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public void addRole(String role,String headRole,Position rolePhrasePosition,Map<String,String> xmlAttributes){
		roles.add(new Role(role,headRole,rolePhrasePosition,xmlAttributes));
	}

	public List<Role> getRoles() {
		return new ArrayList<>(roles);
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullContentPlain() {
		return fullContentPlain;
	}

	public void setFullContentPlain(String fullContent) {
		this.fullContentPlain = fullContent;
	}

	public String getFullContentNerTagged() {
		return fullContentNerTagged;
	}

	public Map<Integer, NerTag> getFullContentNerTaggedStatistic() {
		return fullContentNerTaggedStatistic;
	}

	@Override
	public String toString() {
		return "GroundTruthFile [time=" + time + ", title=" + title + ", roles=" + roles + ", fullContentPlain="
				+ fullContentPlain + ", fullContentNerTagged=" + fullContentNerTagged + "]";
	}

	public void executeFullContentNer(Set<String> headRoles) {
		if(headRoles==null){
			if(fullContentPlain==null){
				throw new IllegalArgumentException("fullContentPlain is null");
			}
			fullContentNerTagged = NERTagger.runTaggerString(fullContentPlain);
		}else{
			if(fullContentPlain==null){
				throw new IllegalArgumentException("fullContentPlain is null");
			}
			fullContentNerTagged = NERTagger.runTaggerStringWithoutHeadRoleReplacement(fullContentPlain,headRoles);
		}


	}

	public void executeFullContentNerStatistic(Set<String> headRoles) {
		if(headRoles==null){
			if(fullContentPlain==null){
				throw new IllegalArgumentException("fullContentPlain is null");
			}
			fullContentNerTaggedStatistic = NERTagger.nerXmlParser(NERTagger.runTaggerXML(fullContentPlain));
		}else{
			if(fullContentPlain==null){
				throw new IllegalArgumentException("fullContentPlain is null");
			}
			//fullContentNerTaggedStatistic = NERTagger.nerXmlParserWithoutHeadRoleReplacement(NERTagger.runTaggerXML(fullContentPlain),headRoles);
			fullContentNerTaggedStatistic = NERTagger.nerXmlParserWithoutHeadRoleReplacement(NERTagger.runTaggerXML(fullContentPlain),headRoles);
		}
	}
}
