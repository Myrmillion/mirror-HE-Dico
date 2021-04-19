package ch.hearc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.hearc.model.User;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}
