package ch.hearc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.hearc.model.Role;

@Repository("RoleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>
{
	Role findByRole(String role);
}
