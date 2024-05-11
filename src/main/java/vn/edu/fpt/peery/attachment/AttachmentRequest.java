package vn.edu.fpt.peery.attachment;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AttachmentRequest {
	private MultipartFile personalImage;
	private MultipartFile frontImage;
	private MultipartFile backImage;
	private MultipartFile billImage;
	private List<AssetDocument> assets;

	@Data
	@NoArgsConstructor
	public static class AssetDocument {
		private MultipartFile attachment;
		private String description;
	}
}
