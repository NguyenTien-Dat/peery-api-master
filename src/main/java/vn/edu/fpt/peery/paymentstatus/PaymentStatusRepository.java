package vn.edu.fpt.peery.paymentstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
	PaymentStatus getByName(String name);
}
