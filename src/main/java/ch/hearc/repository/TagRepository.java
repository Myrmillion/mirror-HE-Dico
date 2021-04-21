package ch.hearc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.hearc.model.Tag;

@Repository("TagRepository")
public interface TagRepository extends JpaRepository<Tag, Integer>{
	Tag findById(int id);
}
