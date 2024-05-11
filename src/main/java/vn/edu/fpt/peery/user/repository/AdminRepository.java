package vn.edu.fpt.peery.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.edu.fpt.peery.accountstatus.AccountStatus;
import vn.edu.fpt.peery.role.Role;
import vn.edu.fpt.peery.user.User;

@Repository
@Transactional
@EnableJpaRepositories
public interface AdminRepository extends JpaRepository<User, Long> {
	List<User> findByRole(Role role);

	Optional<User> findById(Long Id);

	List<User> findByStatus(AccountStatus status);

	Optional<User> findByEmail(String email);

	Optional<User> findByPhone(String phone);

	Optional<User> findByCicNo(String cicNo);

	Long countByRole(Role role);
}