package vn.edu.fpt.peery.request;

import lombok.Data;

@Data
public class RequestClass {
	private Long amount;
	private Long termId;
	private Long userId;
	private String note;
}
