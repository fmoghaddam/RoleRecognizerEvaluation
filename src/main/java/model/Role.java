package model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Role {

	private final String rolePhrase;
	private final String headRole;
	private final Position rolePhrasePosition;
	private final Position headRolePosition;
	private final Map<String, String> xmlAttributes;
	
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
		this.headRolePosition = calculateHeadRolePosition(headRole,rolePhrase);
		if(this.headRolePosition==null){
			throw new IllegalArgumentException("headRolePosition is null for role: "+rolePhrase+" and headrole: "+headRole);
		}
	}


	private Position calculateHeadRolePosition(String headRole, String rolePhrase) {
		final Pattern pattern = Pattern.compile("(?im)" + headRole);
		final Matcher matcher = pattern.matcher(rolePhrase);

		while (matcher.find()) {
			return new Position(matcher.start()+rolePhrasePosition.getStartIndex(), matcher.end()+rolePhrasePosition.getStartIndex());
		}
		return null;
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

	public Map<String, String> getXmlAttributes() {
		return xmlAttributes;
	}

	

	public Position getHeadRolePosition() {
		return headRolePosition;
	}


	@Override
	public String toString() {
		return "Role [rolePhrase=" + rolePhrase + ", headRole=" + headRole + ", rolePhrasePosition="
				+ rolePhrasePosition + ", headRolePosition=" + headRolePosition + ", xmlAttributes=" + xmlAttributes
				+ "]";
	}

}
