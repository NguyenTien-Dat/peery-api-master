package vn.edu.fpt.peery.accountstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountStatusInit {
	@Autowired
	AccountStatusRepository accountStatusRepo;

	public void init() {
		AccountStatus as1 = new AccountStatus();
		as1.setName("New");
		accountStatusRepo.save(as1);

		AccountStatus as2 = new AccountStatus();
		as2.setName("Unconfirmed");
		accountStatusRepo.save(as2);

		AccountStatus as4 = new AccountStatus();
		as4.setName("Confirmed");
		accountStatusRepo.save(as4);

		AccountStatus as5 = new AccountStatus();
		as5.setName("Terminated");
		accountStatusRepo.save(as5);
	}
}
