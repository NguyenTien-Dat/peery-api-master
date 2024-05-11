package vn.edu.fpt.peery.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findRoleByName(String name);
	Role findRoleById(Long id);
}
