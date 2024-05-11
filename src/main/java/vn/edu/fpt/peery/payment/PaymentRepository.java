package vn.edu.fpt.peery.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.peery.contract.Contract;
import vn.edu.fpt.peery.user.User;

@Repository
@EnableJpaRepositories
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Payment findPaymentByIdAndUser(Long id, User user);

	Payment findPaymentById(Long id);

	List<Payment>findAllByContractId(Long contractId);

	Contract findContractById(Long id);

	List<Payment>findAllByContractIdAndType(Long contractId, PaymentType type);

	Payment findByInstallments_Id(Integer installmentId);
}