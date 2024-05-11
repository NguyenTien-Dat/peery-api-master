package vn.edu.fpt.peery.installment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.contract.Contract;
import vn.edu.fpt.peery.contract.ContractRepository;
import vn.edu.fpt.peery.contract.ContractService;
import vn.edu.fpt.peery.payment.Payment;
import vn.edu.fpt.peery.payment.PaymentRepository;
import vn.edu.fpt.peery.payment.PaymentType;
import vn.edu.fpt.peery.paymentstatus.PaymentStatusRepository;

@RestController
public class InstallmentController {

	@Autowired
	private InstallmentRepository repo;

	@Autowired
	private ContractRepository contrRepo;

	@Autowired
	private PaymentRepository pmRepo;

	@Autowired
	private PaymentStatusRepository pmSttRepo;

	@Autowired
	private ContractService contrSrv;

	@Value("${link-base}")
	private String urlBase;

	@Autowired
	Email email;

	@GetMapping("/peery/installment/list")
	public ResponseEntity<List<Installment>> getInstallmentsByPayment(@RequestParam Long paymentId) {
		List<Installment> installments =  repo.findAllByPaymentId(paymentId);
		return new ResponseEntity<>(installments, HttpStatus.OK);
	}

	@PostMapping("/peery/installment/add")
	public ResponseEntity<Void> addInstallment(@RequestBody Installment installment) {
		installment.setStatus(InstallmentStatus.PENDING);
		repo.save(installment);

		// Đánh dấu kỳ thanh toán là chờ xác nhận
		Payment pm = pmRepo.findByInstallments_Id(installment.getId());
		pm.setStatus(pmSttRepo.getByName("Pending"));
		pmRepo.save(pm);

		if (pm.getType().equals(PaymentType.DISBURSE)) {
			email.sendMail(
				pm.getContract().getBorrower().getEmail(),
				String.format("[Contract #%d] Lender has marked the contract as disbursed", pm.getContract().getId()),
				String.format(
					"Dear %s,\n\nContract number %d has been marked as disbursed by the lender, %s.\n\nPlease proceed to verify the disbursement: %s",
					pm.getContract().getBorrower().getFullName(),
					pm.getContract().getId(),
					pm.getContract().getLender().getFullName(),
					String.format("%s/paymentDetails/%d", urlBase, pm.getId())
				)
			);
		} else {
			email.sendMail(
				pm.getContract().getLender().getEmail(),
				String.format("[Contract #%d] Borrower has updated a payment term", pm.getContract().getId()),
				String.format(
					"Dear %s,\n\nA payment term in contract number %d has been updated by the borrower, %s.\n\nPlease proceed to verify the payment: %s",
					pm.getContract().getLender().getFullName(),
					pm.getContract().getId(),
					pm.getContract().getBorrower().getFullName(),
					String.format("%s/paymentDetails/%d", urlBase, pm.getId())
				)
			);
		}

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/peery/installment/updateStatus")
	public ResponseEntity<Void> updateInstallmentStatus(@RequestParam Integer installmentId, @RequestParam Boolean confirm) {
		Installment ins = repo.findById(installmentId).orElseThrow();

		if (confirm) {
			ins.setStatus(InstallmentStatus.CONFIRMED);
		} else {
			ins.setStatus(InstallmentStatus.REJECTED);
		}

		repo.save(ins);

		// Thông báo
		Payment pm = pmRepo.findByInstallments_Id(ins.getId());
		if (pm.getType().equals(PaymentType.DISBURSE)) {
			if (confirm) {
				email.sendMail(
					pm.getContract().getLender().getEmail(),
					String.format("[Contract #%d] Borrower has accepted the disbursement", pm.getContract().getId()),
					String.format(
						"Dear %s,\n\nThe disbursement of contract number %d has been accepted by the borrower, %s.\n\nReview it here: %s",
						pm.getContract().getLender().getFullName(),
						pm.getContract().getId(),
						pm.getContract().getBorrower().getFullName(),
						String.format("%s/paymentDetails/%d", urlBase, pm.getId())
					)
				);
			} else {
				email.sendMail(
					pm.getContract().getLender().getEmail(),
					String.format("[Contract #%d] Borrower has rejected the disbursement", pm.getContract().getId()),
					String.format(
						"Dear %s,\n\nThe disbursement of contract number %d has been declined by the borrower, %s.\n\nPlease proceed to verify the disbursement again or contact us for arbitration. You can review it here: %s",
						pm.getContract().getLender().getFullName(),
						pm.getContract().getId(),
						pm.getContract().getBorrower().getFullName(),
						String.format("%s/paymentDetails/%d", urlBase, pm.getId())
					)
				);
			}
		} else {
			if (confirm) {
				email.sendMail(
					pm.getContract().getBorrower().getEmail(),
					String.format("[Contract #%d] Lender has accepted changes to a payment term", pm.getContract().getId()),
					String.format(
						"Dear %s,\n\nChanges to a payment term in contract number %d has been accepted by the lender, %s.\n\nYou can review it here: %s",
						pm.getContract().getBorrower().getFullName(),
						pm.getContract().getId(),
						pm.getContract().getLender().getFullName(),
						String.format("%s/paymentDetails/%d", urlBase, pm.getId())
					)
				);
			} else {
				email.sendMail(
					pm.getContract().getBorrower().getEmail(),
					String.format("[Contract #%d] Lender has rejected changes to a payment term", pm.getContract().getId()),
					String.format(
						"Dear %s,\n\nChanges to a payment term in contract number %d has been rejected by the lender, %s.\n\nPlease proceed to verify the payment again or contact us for arbitration. You can review it here: %s",
						pm.getContract().getBorrower().getFullName(),
						pm.getContract().getId(),
						pm.getContract().getLender().getFullName(),
						String.format("%s/paymentDetails/%d", urlBase, pm.getId())
					)
				);
			}
		}

		// Cập nhật các số tiền liên quan (kỳ thanh toán, hợp đồng)
		Contract owningContract = contrRepo.findByPayments_Installments_Id(ins.getId());
		contrSrv.checkStatus(owningContract);

		return ResponseEntity.ok().build();
	}
}