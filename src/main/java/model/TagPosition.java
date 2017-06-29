package model;

public class TagPosition extends Position {
	private final String tag;

	public TagPosition(String tag, int startIndex, int endIndex) {
		super(startIndex, endIndex);
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return "TagPostion [tag=" + tag + ", toString()=" + super.toString() + "]";
	}

	public boolean hasOverlap(Position rolePhasePosition) {
		if (this.getStartIndex() <= rolePhasePosition.getStartIndex()
				&& this.getEndIndex() >= rolePhasePosition.getEndIndex()) {
			return true;
		} else if (rolePhasePosition.getStartIndex() >= this.getStartIndex()
				&& rolePhasePosition.getStartIndex() <= this.getEndIndex()) {
			return true;
		} else if (rolePhasePosition.getEndIndex() >= this.getStartIndex()
				&& rolePhasePosition.getEndIndex() <= this.getEndIndex()) {
			return true;
		}
		return false;
	}
}
