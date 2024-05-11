package vn.edu.fpt.peery.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.edu.fpt.peery.attachment.AttachmentRepository;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.contract.Contract;
import vn.edu.fpt.peery.contract.ContractRepository;
import vn.edu.fpt.peery.contractstatus.ContractStatusRepository;
import vn.edu.fpt.peery.installment.Installment;
import vn.edu.fpt.peery.installment.InstallmentRepository;
import vn.edu.fpt.peery.installment.InstallmentStatus;
import vn.edu.fpt.peery.paymentstatus.PaymentStatusRepository;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.repository.AdminRepository;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	PaymentStatusRepository paymentStatusRepo;

	@Autowired
	ContractRepository contractRepository;

	@Autowired
	ContractStatusRepository contractStatusRepo;

	@Autowired
	AttachmentRepository attachmentRepo;

	@Autowired
	AdminRepository userRepo;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	InstallmentRepository insRepo;

	public void createPayment(Long numberOfTerms, Contract contract) {
		Date dueDate = new Date();

		Payment disburse = Payment.builder()
			.contract(contract)
			.user(contract.getLender())
			.principalDue(contract.getAmountPrincipal())
			.totalDue(contract.getAmountPrincipal())
			.status(paymentStatusRepo.getByName("Unpaid"))
			.type(PaymentType.DISBURSE)
			.dueDate(dueDate)
			.build();
		paymentRepo.save(disburse);

		Long principalPerTerm = BigDecimal.valueOf((double) contract.getAmountPrincipal() / numberOfTerms).setScale(0, RoundingMode.UP).longValue();
		Long interestPerTerm = BigDecimal.valueOf((double) contract.getAmountInterest() / numberOfTerms).setScale(0, RoundingMode.UP).longValue();
		for (int i = 1; i <= numberOfTerms; i++) {
			dueDate = addMonths(dueDate, 1);
			Payment repayment = Payment.builder()
				.contract(contract)
				.dueDate(dueDate)
				.user(contract.getBorrower())
				.principalDue(principalPerTerm)
				.interestDue(interestPerTerm)
				.totalDue(principalPerTerm + interestPerTerm)
				.paymentNum(i)
				.status(paymentStatusRepo.getByName("Unpaid"))
				.type(PaymentType.REPAY)
				.build();
			paymentRepo.save(repayment);
		}
	}

	public static Date addMonths(Date date, int monthsToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthsToAdd);
		return calendar.getTime();
	}

	public Payment getPaymentDetail(Long paymentId, User user) {
		Payment paymentDetail = paymentRepo.findPaymentById(paymentId);
		return paymentDetail;
	}

	// Cần chạy hàm này sau khi cập nhật trạng thái lần thanh toán (installment)
	public Payment selfUpdate(Payment pm) {
		Long totalPaid = 0L;
		Boolean isPending = false;

		List<Installment> insList = pm.getInstallments();
		insList.sort(Comparator.comparing(Installment::getId));

		for (Installment i : insList) {
			if (i.getStatus() == InstallmentStatus.PENDING) {
				// Đánh dấu kỳ thanh toán là chờ xác nhận và dừng xử lý
				isPending = true;
				break;
			} else if (i.getStatus() == InstallmentStatus.CONFIRMED) {
				totalPaid += i.getAmount();
			}
		}
		pm.setAmountPaid(totalPaid);

		if (isPending) {
			pm.setStatus(paymentStatusRepo.getByName("Pending"));
		} else if (pm.getAmountPaid() == 0) {
			pm.setStatus(paymentStatusRepo.getByName("Unpaid"));
		} else if (pm.getAmountPaid() < pm.getTotalDue()) {
			pm.setStatus(paymentStatusRepo.getByName("Partially paid"));
		} else {
			pm.setStatus(paymentStatusRepo.getByName("Paid in full"));
		}

		return pm;
	}
}