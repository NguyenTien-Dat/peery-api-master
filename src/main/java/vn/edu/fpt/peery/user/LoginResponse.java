package vn.edu.fpt.peery.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import vn.edu.fpt.peery.attachment.Attachment;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private User user;
	private List<Attachment> attachments;
}
