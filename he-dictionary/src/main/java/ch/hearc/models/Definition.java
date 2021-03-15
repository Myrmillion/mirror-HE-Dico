package ch.hearc.models;

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
	private Integer nUpvote;
	
	@Column(nullable=false, columnDefinition="integer default 0")
	private Integer nDownvote;
	
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
}
