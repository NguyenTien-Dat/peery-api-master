package vn.edu.fpt.peery.user.repository;

import java.sql.Date;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.edu.fpt.peery.accountstatus.AccountStatusRepository;
import vn.edu.fpt.peery.bankaccounts.BankAccount;
import vn.edu.fpt.peery.bankaccounts.BankAccountRepository;
import vn.edu.fpt.peery.banks.BankRepository;
import vn.edu.fpt.peery.common.CommonAndUtils;
import vn.edu.fpt.peery.contract.ContractRepository;
import vn.edu.fpt.peery.contract.ContractService;
import vn.edu.fpt.peery.contractstatus.ContractStatusRepository;
import vn.edu.fpt.peery.payment.PaymentRepository;
import vn.edu.fpt.peery.payment.PaymentService;
import vn.edu.fpt.peery.paymentstatus.PaymentStatusRepository;
import vn.edu.fpt.peery.request.Request;
import vn.edu.fpt.peery.request.RequestRepository;
import vn.edu.fpt.peery.requeststatus.RequestStatusRepository;
import vn.edu.fpt.peery.role.RoleRepository;
import vn.edu.fpt.peery.term.TermRepository;
import vn.edu.fpt.peery.user.User;

@Component
public class UserInit {
	@Autowired
	AuthRepository authRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	BankAccountRepository bankAccountRepo;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	TermRepository termRepo;
	@Autowired
	RequestStatusRepository requestStatusRepo;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	ContractService contractService;
	@Autowired
	CommonAndUtils cu;
	@Autowired
	AccountStatusRepository accountStatusRepository;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	ContractStatusRepository contractStatusRepo;
	@Autowired
	PaymentStatusRepository paymentStatusRepo;
	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	PaymentService pmServ;

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public void init() {
		User user1 = new User(DigestUtils.sha256Hex("1"), "Khanh Le Cao", "khanh@gmail.com", "113", "976272548",
				"23 ABC", Date.valueOf("2002-01-11"), roleRepo.findRoleById((Long.valueOf(1))), accountStatusRepository.findByName("Confirmed"),
				60);

		User user2 = new User(DigestUtils.sha256Hex("1"), "Hieu Van Trung", "cinipit483@lanxi8.com", "114", "1728361982",
				"22 DEF", Date.valueOf("2002-11-22"), roleRepo.findRoleById(Long.valueOf(2)), accountStatusRepository.findByName("Confirmed"),
				72);

		User user3 = new User(DigestUtils.sha256Hex("1"), "Dat Nguyen Tien", "caokhanhle145@gmail.com", "115",
				"12831728", "31A HHH", Date.valueOf("2001-11-23"), roleRepo.findRoleById(Long.valueOf(3)),
				accountStatusRepository.findByName("Confirmed"), 83);

		authRepo.saveAll(Arrays.asList(user1, user2, user3));

		BankAccount bank1 = new BankAccount(bankRepository.getReferenceById(Long.valueOf(1)), "1892346782261", user2);
		BankAccount bank2 = new BankAccount(bankRepository.getReferenceById(Long.valueOf(2)), "97872497", user2);
		BankAccount bank3 = new BankAccount(bankRepository.getReferenceById(Long.valueOf(1)), "912739168", user3);
		BankAccount bank4 = new BankAccount(bankRepository.getReferenceById(Long.valueOf(2)), "2013764181", user3);
		bankAccountRepo.saveAll(Arrays.asList(bank1, bank2, bank3, bank4));

		Request request1 = new Request(user2, Long.valueOf(1000000), termRepo.getTermById(Long.valueOf(1)), cu.calculateAPR(user2), requestStatusRepo.findStatusByName("Approved"), null);
		Request request2 = new Request(user2, Long.valueOf(5000000), termRepo.getTermById(Long.valueOf(4)), cu.calculateAPR(user2), requestStatusRepo.findStatusByName("Approved"), "Transfer to ACB");
		requestRepository.saveAll(Arrays.asList(request1, request2));
		contractService.createContract(Long.valueOf(1), user3);
	}
}