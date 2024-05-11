package vn.edu.fpt.peery.contract;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.contractstatus.ContractStatus;
import vn.edu.fpt.peery.contractstatus.ContractStatusRepository;
import vn.edu.fpt.peery.payment.Payment;
import vn.edu.fpt.peery.payment.PaymentService;
import vn.edu.fpt.peery.request.RequestService;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;

@RestController
public class ContractController {

	@Autowired
	ContractService contractService;

	@Autowired
	RequestService requestService;

	@Autowired
	UserService userService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	ContractStatusRepository ctrSttRepo;

	@Autowired
	ContractRepository ctrRepo;

	@Autowired
	Email email;

	@Value("${link-base}")
	private String urlBase;

	@GetMapping("/peery/contractStatus/list")
	public ResponseEntity<List<ContractStatus>> getContractStatuses() {
		return ResponseEntity.ok(ctrSttRepo.findAll());
	}

	@GetMapping(path = "/peery/contract/contractlist")
	public ResponseEntity<List<Contract>> getAllContract(@RequestParam Long userId) {
		List<Contract> contracts;
		contracts = contractService.getContractByUser(userId);
		return new ResponseEntity<>(contracts, HttpStatus.OK);
	}

	@GetMapping(path = "/peery/contract/contractdetail")
	public ResponseEntity<Contract> getContractDetail(HttpSession session, @RequestParam("contractId") Long contractId,
			@RequestParam(name = "userId", required = false) Long userId) {
		User loggedUser = userService.getUser(userId);
		if (loggedUser.getRole().getName().equalsIgnoreCase("Staff")) {
			Contract contract = contractService.getContractDetail(contractId, loggedUser);
			return new ResponseEntity<>(contract, HttpStatus.OK);
		} else if (!loggedUser.getRole().getName().equalsIgnoreCase("Staff")) {
			Contract contract = contractService.getContractDetail(contractId, loggedUser);
			if (contract != null) {
				return new ResponseEntity<>(contract, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/peery/contract/payment/paymentdetail")
	public ResponseEntity<?> getPaymentDetail(@RequestParam(name = "userId", required = false) Long userId,
			@RequestParam(name = "paymentId") Long paymentId) {
		User loggedUser = userService.getUser(userId);
		try {
			Payment payment = paymentService.getPaymentDetail(paymentId, loggedUser);
			return new ResponseEntity<Payment>(payment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/peery/contract/settle")
	public ResponseEntity<Void> settleContractEarly(@RequestParam Long contractId, @RequestParam Long userId, @RequestParam boolean choice) {
		User user = userService.getUser(userId);
		Contract target = contractService.getContractDetail(contractId, user);

		if (!(target.getLender().getId() == userId)) {
			return ResponseEntity.status(403).build();
		}

		if (choice) {
			target.setAmountRemaining(0L);
			target.setStatus(ctrSttRepo.getByName("Paid in full"));
			target.setCloseDate(new Date());
			ctrRepo.save(target);

			email.sendMail(
				target.getBorrower().getEmail(),
				String.format("[Contract #%d] Request for early settlement has been accepted", target.getId()),
				String.format(
					"Dear %s,\n\nContract number %d has been accepted for early settlement by the lender.",
					target.getBorrower().getFullName(),
					target.getId()
				)
			);
		} else {
			// Khôi phục về trạng thái cũ
			contractService.checkStatus(target);

			email.sendMail(
				target.getBorrower().getEmail(),
				String.format("[Contract #%d] Request for early settlement has been rejected", target.getId()),
				String.format(
					"Dear %s,\n\nThe lender of contract number %d, %s, has rejected your request for early settlement.\n\nPlease proceed to verify your settlement transaction, or contact us for arbitration.",
					target.getBorrower().getFullName(),
					target.getId(),
					target.getLender().getFullName()
				)
			);
		}

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/peery/contract/requestSettle")
	public ResponseEntity<Void> requestForEarlySettlement(@RequestParam Long contractId, @RequestParam Long userId) {
		User user = userService.getUser(userId);
		Contract target = contractService.getContractDetail(contractId, user);
		target.setStatus(ctrSttRepo.findById(6L).get());
		ctrRepo.save(target);

		email.sendMail(
			target.getLender().getEmail(),
			String.format("[Contract #%d] Borrower is requesting for early settlement", target.getId()),
			String.format(
				"Dear %s,\n\nThe borrower of contract number %d, %s, would like to settle it early.\n\nPlease proceed to confirm or reject their request: %s",
				target.getLender().getFullName(),
				target.getId(),
				target.getBorrower().getFullName(),
				String.format("%s/paymentDetails/%d", urlBase, target.getId())
			)
		);

		return ResponseEntity.ok().build();
	}
}