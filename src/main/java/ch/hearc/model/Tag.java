package ch.hearc.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tag")
public class Tag {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private Integer id;
	
	@Column(nullable=false,length=7)
	private String color;
	
	@Column(nullable=false, unique=true, length=50)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User creator;
	
	@ManyToMany(mappedBy="containingTags")
	private Set<Definition> containedTags;
	
	
	public boolean validate()
	{
		return (!creator.getUsername().isEmpty() && !color.isEmpty() && !name.isEmpty());
	}
	
	public Integer getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public User getCreator() {
		return creator;
	}

	public Set<Definition> getContainedTags() {
		return containedTags;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setContainedTags(Set<Definition> containedTags) {
		this.containedTags = containedTags;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.name.hashCode();
	}
}
