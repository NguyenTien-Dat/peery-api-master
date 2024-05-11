package vn.edu.fpt.peery.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleInitialize {
	@Autowired
	RoleRepository roleRepository;

//	@PostConstruct
	public void initializeRole() {
		Role role = new Role("Staff");
		roleRepository.save(role);
		Role role2 = new Role("Borrower");
		roleRepository.save(role2);
		Role role3 = new Role("Lender");
		roleRepository.save(role3);
	}

}
