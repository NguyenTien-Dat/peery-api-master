package vn.edu.fpt.peery.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepo;

	public List<Role> getRoles() {
		List<Role> roleList = roleRepo.findAll().stream().filter(
				role -> role.getName().equalsIgnoreCase("Borrower") || role.getName().equalsIgnoreCase("Lender"))
				.collect(Collectors.toList());
		return roleList;
	}
}
