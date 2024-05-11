package vn.edu.fpt.peery.user.login;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
	private String email;
	private String phone;
}