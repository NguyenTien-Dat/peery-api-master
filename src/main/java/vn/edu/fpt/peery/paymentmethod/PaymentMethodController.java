package vn.edu.fpt.peery.paymentmethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentMethodController {
	@Autowired
	private PaymentMethodRepository pmRepo;

	@GetMapping("/peery/paymentMethods/list")
	public ResponseEntity<List<PaymentMethod>> getPMs() {
		List<PaymentMethod> pms = pmRepo.findAll();
		return new ResponseEntity<>(pms, HttpStatus.OK);
	}
}
