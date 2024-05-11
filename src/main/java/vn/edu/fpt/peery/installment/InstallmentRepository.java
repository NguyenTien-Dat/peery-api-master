package vn.edu.fpt.peery.installment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, Integer> {
	List<Installment> findAllByPaymentId(Long paymentId);
}