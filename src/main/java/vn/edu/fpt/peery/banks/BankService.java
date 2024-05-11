package vn.edu.fpt.peery.banks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
	@Autowired
	BankRepository bankRepo;
	
	public List<Bank> getAllBanks(){
		List<Bank> bankList = bankRepo.findAll();
		return bankList;
	}
}
