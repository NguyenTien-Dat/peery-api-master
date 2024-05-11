package vn.edu.fpt.peery.user.register;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import vn.edu.fpt.peery.accountstatus.AccountStatus;

@Data
public class RegisterRequest {
	private String password;
	private String fullname;
	private String email;
	private String phone;
	private String cicNo;
	private String address;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	private Long roleId;
	private String ConfirmToken;
	private AccountStatus status;
}
