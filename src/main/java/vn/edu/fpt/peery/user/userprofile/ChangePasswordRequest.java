package vn.edu.fpt.peery.user.userprofile;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	private Long userId;
	private String newPassword;
}
