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
import javax.persistence.Transient;

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
	
	@Transient
	private String confirmPassword;
	
	@OneToMany(mappedBy="creator")
	private List<Definition> definitions;
	
	@OneToMany(mappedBy="creator")
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

	
	// GETTERS
	
	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getIsModerator() {
		return isModerator;
	}

	public String getPassword() {
		return password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public Set<Definition> getUpvotedDefinitions() {
		return upvotedDefinitions;
	}

	public Set<Definition> getDownvotedDefinitions() {
		return downvotedDefinitions;
	}

	//SETTERS
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsModerator(Boolean isModerator) {
		this.isModerator = isModerator;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void setUpvotedDefinitions(Set<Definition> upvotedDefinitions) {
		this.upvotedDefinitions = upvotedDefinitions;
	}

	public void setDownvotedDefinitions(Set<Definition> downvotedDefinitions) {
		this.downvotedDefinitions = downvotedDefinitions;
	}
	
	
	
}
