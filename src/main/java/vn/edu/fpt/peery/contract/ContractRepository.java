package vn.edu.fpt.peery.contract;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.peery.request.Request;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.contractstatus.ContractStatus;


@Repository
@EnableJpaRepositories
public interface ContractRepository extends JpaRepository<Contract, Long> {
	List<Contract> findAll();

	List<Contract> findByBorrower(User borrower);

	List<Contract> findByLender(User Lender);

	Contract getByIdAndBorrower(Long Id, User borrower);

	Contract getByIdAndLender(Long Id, User lender);

	Contract getDetailById(Long Id);

	List<Contract> getByRequest(Request request);

	Contract findByPayments_Id(Long id);

	Contract findByPayments_Installments_Id(Integer installmentId);

	Long countByStatus(ContractStatus status);

	@Query("SELECT sum(amountPrincipal) FROM Contract")
	Long totalAmountLent();

	@Query("SELECT sum(amountPrincipal + amountInterest - amountRemaining) FROM Contract")
	Long totalAmountRepaid();
}