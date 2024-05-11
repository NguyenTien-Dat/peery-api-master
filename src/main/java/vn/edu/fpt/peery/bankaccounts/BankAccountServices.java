package vn.edu.fpt.peery.bankaccounts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.edu.fpt.peery.attachment.BankAccountRequest;
import vn.edu.fpt.peery.banks.BankRepository;
import vn.edu.fpt.peery.user.User;

@Service
public class BankAccountServices {
	@Autowired
	BankAccountRepository bankAccountRepo;
	@Autowired
	BankRepository bankRepository;

	public void addBankAccount(BankAccountRequest request, User User) {
		JSONArray jsonResp = new JSONArray(request.getAccounts());

		for (int i = 0; i < jsonResp.length(); ++i) {
			JSONObject accObj = jsonResp.getJSONObject(i);
			Long bankId = accObj.getLong("bankId");
			String accountNo = accObj.getString("accountNo");

			BankAccount account = BankAccount.builder().bank(bankRepository.findById(bankId).orElseThrow()).accountNo(accountNo).user(User).build();
			bankAccountRepo.save(account);
		}
	}
}