package vn.edu.fpt.peery.user.login;

import lombok.Data;

@Data
public class LoginRequest {
	private String phone;
	private String password;
}
