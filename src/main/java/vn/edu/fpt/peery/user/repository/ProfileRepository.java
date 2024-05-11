package vn.edu.fpt.peery.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.peery.user.User;

@Repository
public interface ProfileRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByEmail(String email);

	User findUserByConfirmToken(String confirmToken);

	void deleteById(Long id);
}
