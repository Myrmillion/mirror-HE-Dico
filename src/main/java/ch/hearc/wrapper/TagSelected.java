package ch.hearc.wrapper;

import ch.hearc.model.Tag;

public class TagSelected {
	public Tag tag;
	public boolean selected;
	
	public Tag getTag() {
		return tag;
	}
	
	public Integer getId() {
		return tag.getId();
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
