package ch.hearc.models;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private Integer id;
	
	@Column(nullable=false,length=50)
	private String username;
	
	@Column(nullable=false,length=100)
	private String email;
	
	@Column(nullable=false,columnDefinition="boolean default false")
	private Boolean isModerator;
	
	@Column(nullable=false)
	private String password;
	
	@OneToMany(mappedBy="definition")
	private List<Definition> definitions;
	
	@OneToMany(mappedBy="tag")
	private List<Tag> tags;
	
	@ManyToMany
	@JoinTable(
			name= "upvote",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="definition_id"))
	private Set<Definition> upvotedDefinitions;
	
	@ManyToMany
	@JoinTable(
			name= "downvote",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="definition_id"))
	private Set<Definition> downvotedDefinitions;
	
}
