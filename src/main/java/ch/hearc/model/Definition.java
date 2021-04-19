package ch.hearc.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="definition")
public class Definition {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private Integer id;
	
	@Column(nullable = false, length=80)
	private String word;
	
	@Column(nullable=false,length=600)
	private String description;
	
	@Column(nullable=false, columnDefinition="integer default 0")
	private Integer nupvote = 0;
	
	@Column(nullable=false, columnDefinition="integer default 0")
	private Integer ndownvote = 0;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User creator;
	
	@ManyToMany(mappedBy="upvotedDefinitions")
	private Set<User> upvotes;
	
	@ManyToMany(mappedBy="downvotedDefinitions")
	private Set<User> downvotes;
	
	@ManyToMany
	@JoinTable(
			name= "category",
			joinColumns = @JoinColumn(name="definition_id"),
			inverseJoinColumns = @JoinColumn(name="tag_id"))
	private Set<Tag> containingTags;

	
	public boolean validate()
	{
		return (!creator.getUsername().isEmpty() && !word.isEmpty() && !description.isEmpty());
	}
	
	
	//GETTERS
	
	public Integer getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

	public String getDescription() {
		return description;
	}

	public Integer getNupvote() {
		return nupvote;
	}

	public Integer getNdownvote() {
		return ndownvote;
	}

	public User getCreator() {
		return creator;
	}

	public Set<User> getUpvotes() {
		return upvotes;
	}

	public Set<User> getDownvotes() {
		return downvotes;
	}

	public Set<Tag> getContainingTags() {
		return containingTags;
	}

	
	/* SETTERS */
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setNupvote(Integer nupvote) {
		this.nupvote = nupvote;
	}

	public void setNdownvote(Integer ndownvote) {
		this.ndownvote = ndownvote;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setUpvotes(Set<User> upvotes) {
		this.upvotes = upvotes;
	}

	public void setDownvotes(Set<User> downvotes) {
		this.downvotes = downvotes;
	}

	public void setContainingTags(Set<Tag> containingTags) {
		this.containingTags = containingTags;
	}

	
	
}
