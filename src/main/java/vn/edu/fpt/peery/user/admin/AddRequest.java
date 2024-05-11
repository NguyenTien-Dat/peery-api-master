package vn.edu.fpt.peery.user.admin;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import vn.edu.fpt.peery.accountstatus.AccountStatus;

@Data
public class AddRequest {
	private String username;
	private String password;
	private String fullname;
	private String email;
	private String phone;
	private String cicNo;
	private String address;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	private String role;
	private AccountStatus status;
}
