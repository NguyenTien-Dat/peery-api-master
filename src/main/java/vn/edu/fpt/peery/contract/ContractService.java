package vn.edu.fpt.peery.contract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.edu.fpt.peery.common.CommonAndUtils;
import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.common.exceptions.StatusExceptions;
import vn.edu.fpt.peery.contractstatus.ContractStatusRepository;
import vn.edu.fpt.peery.installment.InstallmentRepository;
import vn.edu.fpt.peery.payment.Payment;
import vn.edu.fpt.peery.payment.PaymentRepository;
import vn.edu.fpt.peery.payment.PaymentService;
import vn.edu.fpt.peery.payment.PaymentType;
import vn.edu.fpt.peery.request.Request;
import vn.edu.fpt.peery.request.RequestRepository;
import vn.edu.fpt.peery.request.RequestService;
import vn.edu.fpt.peery.requeststatus.RequestStatusRepository;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;

@Service
@Slf4j
public class ContractService {

	@Autowired
	ContractRepository contractRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ContractStatusRepository contractStatusRepo;

	@Autowired
	PaymentService paymentService;

	@Autowired
	UserService userService;

	@Autowired
	RequestStatusRepository requestStatusRepo;

	@Autowired
	RequestService requestService;

	@Autowired
	CommonAndUtils common;

	@Autowired
	PaymentRepository pmRepo;

	@Autowired
	InstallmentRepository insRepo;

	@Autowired
	Email email;

	@Value("${link-base}")
	private String urlBase;

	public List<Contract> getContractByUser(Long userId) {
		List<Contract> listContract = null;
		User user = userService.getUser(userId);
		// System.out.println(user.getFullName() + "|" + user.getEmail());
		if (user.getRole().getName().equalsIgnoreCase("Borrower")) {
			listContract = contractRepository.findByBorrower(user);
		} else if (user.getRole().getName().equalsIgnoreCase("Lender")) {
			listContract = contractRepository.findByLender(user);
		} else {
			listContract = contractRepository.findAll();
		}
		return listContract;
	}

	public Contract getContractDetail(Long Id, User user) {
		if (user.getRole().getName().equalsIgnoreCase("Borrower")) {
			Contract contract = contractRepository.getByIdAndBorrower(Id, user);
			return contract;
		} else if (user.getRole().getName().equalsIgnoreCase("Lender")) {
			Contract contract = contractRepository.getByIdAndLender(Id, user);
			return contract;
		} else {
			Contract contract = contractRepository.getDetailById(Id);
			return contract;
		}
	}

	public Contract createContract(Long id, User user) {
		Request request = requestRepository.getRequestById(id);

		if (request.getStatus().getName().equalsIgnoreCase("Approved")) {
			Long timesToPayDebt = request.getTerm().getNumberOfMonth();
			Long totalInterest = BigDecimal.valueOf(request.getAmount() * request.getApr().doubleValue()).setScale(0, RoundingMode.UP).longValue();

			Contract contract = Contract.builder()
				.lender(user)
				.request(request)
				.borrower(request.getBorrower())
				.term(request.getTerm())
				.amountPrincipal(request.getAmount())
				.amountInterest(totalInterest)
				.amountRemaining(request.getAmount() + totalInterest)
				.apr(request.getApr())
				.status(contractStatusRepo.findById(Long.valueOf(1)).orElseThrow())
				.build();
			Contract saved = contractRepository.save(contract);

			email.sendMail(
				request.getBorrower().getEmail(),
				"A contract has been created for your loan request",
				String.format("Hello %s,\n\nWe are pleased to inform you that your recent loan request has been accepted by a lender.\n\nYou can review the contract in more details here: %s\n\nIt is now your responsibility to follow the terms outlined when you applied for the loan. Thanks for using our service.", request.getBorrower().getFullName(), String.format("%s/contractDetails/%s", urlBase, saved.getId()))
			);

			paymentService.createPayment(timesToPayDebt, contract);
			requestService.settleRequest(request);

			return contract;
		} else {
			throw new StatusExceptions("Loan request has not been approved");
		}
	}

	/**
	 * Kiểm tra lại trạng thái của hợp đồng
	 * Cần chạy hàm này khi xác nhận lần thanh toán (installment)
	 */
	public void checkStatus(Contract target) {
		log.info("Đang kiểm tra hợp đồng số " + target.getId());

		Boolean disbursed = false;
		Boolean allTermUnpaid = true;
		Boolean allTermPaid = true;

		List<Payment> pms = target.getPayments();
		pms.sort(Comparator.comparing(Payment::getPaymentNum));

		Iterator<Payment> iterator = pms.iterator();
		while (iterator.hasNext()) {
			Payment x = iterator.next();
			Payment refreshed = paymentService.selfUpdate(x);
			x = refreshed;
		}

		Long totalPaid = 0L;

		for (Payment pm : pms) {
			if (pm.getType() == PaymentType.DISBURSE) {
				// Kiểm tra lần giải ngân

				if (pm.getAmountPaid() == 0) {
					target.setStatus(contractStatusRepo.findById(Long.valueOf(1)).orElseThrow());	// Undisbursed
					break;
				} else if (pm.getAmountPaid() < pm.getTotalDue()) {
					target.setStatus(contractStatusRepo.findById(Long.valueOf(2)).orElseThrow());	// Partially disbursed
					break;
				} else {
					disbursed = true;
				}
			} else {
				// Nếu đã giải ngân đủ, đi tới kiểm tra các giao dịch trả nợ
				totalPaid += pm.getAmountPaid();

				if (!(pm.getStatus().getId() == 1)) {		// Unpaid
					allTermUnpaid = false;
				}

				if (!(pm.getStatus().getId() == 4)) {		// Paid in full
					allTermPaid = false;
				}
			}
		}

		target.setAmountRemaining(target.getAmountPrincipal() + target.getAmountInterest() - totalPaid);

		if (disbursed) {
			if (allTermPaid) {
				target.setStatus(contractStatusRepo.findById(Long.valueOf(5)).orElseThrow());	// Paid
				target.setCloseDate(new Date());
		   	} else if (allTermUnpaid) {
				target.setStatus(contractStatusRepo.findById(Long.valueOf(3)).orElseThrow());	// Disbursed, not yet repaid
		   	} else {
				target.setStatus(contractStatusRepo.findById(Long.valueOf(4)).orElseThrow());	// Partially repaid
		   	}
		}

		pmRepo.saveAllAndFlush(pms);
		contractRepository.saveAndFlush(target);
		// log.error(String.format("Thông tin hợp đồng số %d sau khi cập nhật: %s", target.getId(), target.toString()));
	}
}
