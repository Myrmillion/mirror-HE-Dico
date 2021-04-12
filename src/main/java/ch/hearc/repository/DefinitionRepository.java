package ch.hearc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.hearc.models.Definition;

@Repository("DefinitionRepository")
public interface DefinitionRepository extends JpaRepository<Definition, Long> {
	
	List<Definition> findByWord(String word);
	List<Definition> findByWordOrderByNupvoteDesc(String word);
	

}
