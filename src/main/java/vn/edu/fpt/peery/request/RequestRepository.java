package vn.edu.fpt.peery.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.peery.requeststatus.RequestStatus;
import vn.edu.fpt.peery.user.User;

@Repository
@EnableJpaRepositories
public interface RequestRepository extends JpaRepository<Request, Long> {
	List<Request> findByBorrower(User borrower);

	Request getOfferDetailByIdAndBorrower(Long id, User borrower);

	Request getOfferByIdAndStatus(Long id, RequestStatus status);

	Request getRequestById(Long id);

	List<Request> findAll();

	List<Request> findByStatus(RequestStatus status);

	Long countByStatus(RequestStatus status);
}