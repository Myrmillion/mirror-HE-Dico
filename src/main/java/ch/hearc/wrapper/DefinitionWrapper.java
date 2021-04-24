package ch.hearc.wrapper;

import java.util.ArrayList;
import java.util.List;

import ch.hearc.model.Definition;
import ch.hearc.model.Tag;

public class DefinitionWrapper {
	
	private Definition definition;
	private List<TagSelected> tags;

    public List<TagSelected> getTags() {
        return tags;
    }

    public void setTags(List<TagSelected> tags) {
        this.tags = tags;
    }
    
    public Definition getDefinition() {
    	return definition;
    }
    
    public void setDefinition(Definition definition) {
    	this.definition = definition;
    }
}
