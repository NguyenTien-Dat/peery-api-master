package vn.edu.fpt.peery.paymentmethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodInit {
	@Autowired
	private PaymentMethodRepository pmRepo;

	public void generatePaymentMethods() {
		PaymentMethod pm1 = new PaymentMethod("Cash");
		pmRepo.save(pm1);

		PaymentMethod pm3 = new PaymentMethod("Electronic funds transfer (NAPAS 247)");
		pmRepo.save(pm3);

		PaymentMethod pm4 = new PaymentMethod("E-wallet");
		pmRepo.save(pm4);

		PaymentMethod pm2 = new PaymentMethod("Wire transfer");
		pmRepo.save(pm2);

		PaymentMethod pm5 = new PaymentMethod("Cheque");
		pmRepo.save(pm5);

		PaymentMethod pm6 = new PaymentMethod("Cryptocurrency");
		pmRepo.save(pm6);

		PaymentMethod pm7 = new PaymentMethod("Other");
		pmRepo.save(pm7);
	}
}