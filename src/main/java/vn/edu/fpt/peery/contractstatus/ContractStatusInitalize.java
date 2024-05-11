package vn.edu.fpt.peery.contractstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractStatusInitalize {
	@Autowired
	ContractStatusRepository contractStatusRepo;

	public void init() {
		ContractStatus c1 = new ContractStatus();
		c1.setName("Undisbursed");
		contractStatusRepo.save(c1);

		ContractStatus c2 = new ContractStatus();
		c2.setName("Partially disbursed");
		contractStatusRepo.save(c2);

		ContractStatus c3 = new ContractStatus();
		c3.setName("Disbursed");
		contractStatusRepo.save(c3);

		ContractStatus c4 = new ContractStatus();
		c4.setName("Partially paid");
		contractStatusRepo.save(c4);

		ContractStatus c5 = new ContractStatus();
		c5.setName("Paid in full");
		contractStatusRepo.save(c5);

		ContractStatus c6 = new ContractStatus();
		c6.setName("Request for early settlement");
		contractStatusRepo.save(c6);
	}
}
