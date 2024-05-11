package vn.edu.fpt.peery.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.edu.fpt.peery.user.User;

@Repository
@Transactional
public interface AuthRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByPhone(String phone);

	Optional<User> findUserByEmail(String email);

	User findUserByConfirmToken(String confirmToken);

	User getUserById(Long id);
}