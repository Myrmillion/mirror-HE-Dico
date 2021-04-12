package ch.hearc.models;

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
	
	@Column(nullable=false,length=6)
	private String color;
	
	@Column(nullable=false, length=50)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User creator;
	
	@ManyToMany(mappedBy="containingTags")
	private Set<Definition> containedTags;
}
