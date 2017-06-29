package model;

import java.util.HashMap;
import java.util.Map;

public class Role {

	private String rolePhrase;
	private String headRole;
	private Position rolePhrasePosition;
	private Map<String, String> xmlAttributes;
	
	public Role(final String rolePhrase,final String headRole,final Position rolePhrasePosition,
			final Map<String, String> xmlAttributes) {
		if(rolePhrase == null){
			throw new IllegalArgumentException("RolePhrase is null");
		}
		if(headRole == null){
			throw new IllegalArgumentException("HeadRole is null for role: "+rolePhrase);
		}
		if(xmlAttributes == null || xmlAttributes.isEmpty()){
			throw new IllegalArgumentException("xmlAttributes is null or empty for role: "+rolePhrase);
		}
		if(rolePhrasePosition==null){
			throw new IllegalArgumentException("rolePhasePosition is null for role: "+rolePhrase);
		}
		this.rolePhrase = rolePhrase;
		this.headRole = headRole;
		this.rolePhrasePosition = rolePhrasePosition;
		this.xmlAttributes = new HashMap<>(xmlAttributes);
	}


	public String getRolePhrase() {
		return rolePhrase;
	}


	public String getHeadRole() {
		return headRole;
	}


	public Position getRolePhasePosition() {
		return rolePhrasePosition;
	}


	public void setRolePhasePosition(Position rolePhasePosition) {
		this.rolePhrasePosition = rolePhasePosition;
	}


	public Map<String, String> getXmlAttributes() {
		return xmlAttributes;
	}


	@Override
	public String toString() {
		return "Role [rolePhrase=" + rolePhrase + ", headRole=" + headRole + ", rolePhrasePosition="
				+ rolePhrasePosition + ", xmlAttributes=" + xmlAttributes + "]";
	}

}
