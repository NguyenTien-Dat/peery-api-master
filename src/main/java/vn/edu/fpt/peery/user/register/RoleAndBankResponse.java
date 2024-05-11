package vn.edu.fpt.peery.user.register;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import vn.edu.fpt.peery.banks.Bank;
import vn.edu.fpt.peery.role.Role;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleAndBankResponse {
	private List<Role> roleList;
	private List<Bank> bankList;
}
