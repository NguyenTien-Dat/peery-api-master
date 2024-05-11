package vn.edu.fpt.peery.bankaccounts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
//	List<BankAccount> findAllByUser(User user);
	BankAccount getBankAccountById(Long id);
}
