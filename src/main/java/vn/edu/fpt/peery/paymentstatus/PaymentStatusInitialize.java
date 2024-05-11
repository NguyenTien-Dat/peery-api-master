package vn.edu.fpt.peery.paymentstatus;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusInitialize {
	@Autowired
	PaymentStatusRepository paymentStatusRepository;

	public void initializePaymentStatus() {
		PaymentStatus status1 = new PaymentStatus();
		status1.setName("Unpaid");
		PaymentStatus status2 = new PaymentStatus();
		status2.setName("Pending");
		PaymentStatus status3 = new PaymentStatus();
		status3.setName("Partially paid");
		PaymentStatus status4 = new PaymentStatus();
		status4.setName("Paid in full");

		paymentStatusRepository.saveAllAndFlush(Arrays.asList(status1, status2, status3, status4));
	}
}