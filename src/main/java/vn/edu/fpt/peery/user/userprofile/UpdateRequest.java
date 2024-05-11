package vn.edu.fpt.peery.user.userprofile;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateRequest {
	private String phone;
	private String address;
	private Date dob;
	private String fullname;
}
