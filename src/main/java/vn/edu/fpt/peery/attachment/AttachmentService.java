package vn.edu.fpt.peery.attachment;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.ServletContext;
import vn.edu.fpt.peery.accountstatus.AccountStatusRepository;
import vn.edu.fpt.peery.attachment.AttachmentRequest.AssetDocument;
import vn.edu.fpt.peery.attachmenttype.AttachmentTypeRepository;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;
import vn.edu.fpt.peery.user.repository.AuthRepository;

@Service
public class AttachmentService {

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	AttachmentTypeRepository repo;

	@Autowired
	ServletContext context;

	@Autowired
	AccountStatusRepository asRepo;

	@Autowired
	AuthRepository authRepo;

	@Autowired
	UserService usrSrv;

	@Value("${attachments-path}")
	private String attachmentsPath;

	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		if (original_width > bound_width) {
			new_width = bound_width;
			new_height = (new_width * original_height) / original_width;
		}

		if (new_height > bound_height) {
			new_height = bound_height;
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	public byte[] resizeImage(BufferedImage image) throws IOException {
		Dimension newMaxSize = getScaledDimension(new Dimension(image.getWidth(), image.getHeight()), new Dimension(1440, 900));
		BufferedImage resizedImg = Scalr.resize(image, Method.AUTOMATIC, newMaxSize.width, newMaxSize.height);

		BufferedImage newBufferedImage = new BufferedImage(resizedImg.getWidth(), resizedImg.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.getGraphics().drawImage(resizedImg, 0, 0, null);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(newBufferedImage, "JPEG", outputStream);

		byte[] imageBytes = outputStream.toByteArray();
		return imageBytes;
	}

	public void getMultipleImage(AttachmentRequest request, User user) throws IOException {
		// create files at target directory
		Files.createDirectories(Paths.get(attachmentsPath));

		List<AttachmentRequest.AssetDocument> assets = request.getAssets();
		for (AssetDocument a : assets) {
			String desc = a.getDescription();
			MultipartFile atch = a.getAttachment();
			UUID uuid = UUID.randomUUID();

			byte[] resizedJpeg = resizeImage(ImageIO.read(atch.getInputStream()));
			OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + uuid + ".jpg"));
			os.write(resizedJpeg);
			os.close();

			Attachment atchObj = Attachment.builder().uuid(uuid).description(desc).user(user).type(repo.getByType("Income_Proof")).build();
			attachmentRepository.save(atchObj);
		}

		// get file from request
		MultipartFile frontImageFile = request.getFrontImage();
		MultipartFile backImageFile = request.getBackImage();
		MultipartFile personalImageFile = request.getPersonalImage();

		// Generate UUID for attachments
		UUID frontUuid = UUID.randomUUID();
		UUID backUuid = UUID.randomUUID();
		UUID personUuid = UUID.randomUUID();

		BufferedImage frontImage = ImageIO.read(frontImageFile.getInputStream());
		byte[] resizedFrontImage = resizeImage(frontImage);
		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + frontUuid + ".jpg"))) {
			os.write(resizedFrontImage);
		}
		BufferedImage backImage = ImageIO.read(backImageFile.getInputStream());
		byte[] resizedBackImage = resizeImage(backImage);
		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + backUuid + ".jpg"))) {
			os.write(resizedBackImage);
		}
		BufferedImage personalImage = ImageIO.read(personalImageFile.getInputStream());
		byte[] resizedPersonalImage = resizeImage(personalImage);
		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + personUuid + ".jpg"))) {
			os.write(resizedPersonalImage);
		}

		// Save file path to database
		Attachment a1 = Attachment.builder().uuid(personUuid).user(user).type(repo.getByType("Personal_Image")).build();
		Attachment a2 = Attachment.builder().uuid(frontUuid).user(user).type(repo.getByType("Cic_Front_Image")).build();
		Attachment a3 = Attachment.builder().uuid(backUuid).user(user).type(repo.getByType("Cic_Back_Image")).build();

		attachmentRepository.save(a1);
		attachmentRepository.save(a2);
		attachmentRepository.save(a3);
	}

	public List<Attachment> getInfoImageByUser(User user) {
		List<Attachment> imageList = attachmentRepository.getByUser(user);
		return imageList;
	}

	public String getAttachment(UUID uid) {
		Attachment attachment = attachmentRepository.getById(uid);
		String fileImage = attachmentsPath + "/" + attachment.getUuid().toString() + ".jpg";
		return fileImage;
	}

	public void deleteAttachment(UUID uuid) {
		Attachment attachment = attachmentRepository.getById(uuid);
		String fileImage = attachmentsPath + "/" + attachment.getUuid().toString() + ".jpg";
		File imageFile = new File(fileImage);
		try {
			Files.delete(imageFile.toPath());
			attachmentRepository.deleteById(uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAttachment(MultipartFile atch, String desc, User user) {
		UUID uuid = UUID.randomUUID();

		try {
			byte[] image = resizeImage(ImageIO.read(atch.getInputStream()));
			OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + uuid + ".jpg"));
			os.write(image);
			os.close();

			Attachment atchObj = Attachment.builder().uuid(uuid).description(desc).user(user).type(repo.getByType("Income_Proof")).build();
			attachmentRepository.save(atchObj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateCic(MultipartFile front, MultipartFile back, MultipartFile person, User user) {
		usrSrv.setUnconfirmed(user);

		List<Attachment> atchs = user.getAttachments();

		for (Iterator<Attachment> iterator = atchs.iterator(); iterator.hasNext();) {
			Attachment i = iterator.next();

			switch (i.getType().getType()) {
				case "Cic_Front_Image":
					break;
				case "Cic_Back_Image":
					break;
				case "Personal_Image":
					break;
				default:
					continue;
			}

			deleteAttachment(i.getUuid());
			// iterator.remove();
		}

		UUID frontUuid = UUID.randomUUID();
		UUID backUuid = UUID.randomUUID();
		UUID pUuid = UUID.randomUUID();

		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + frontUuid + ".jpg"))) {
			os.write(resizeImage(ImageIO.read(front.getInputStream())));
			os.close();
			attachmentRepository.save(Attachment.builder().user(user).uuid(frontUuid).type(repo.getByType("Cic_Front_Image")).build());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + backUuid + ".jpg"))) {
			os.write(resizeImage(ImageIO.read(back.getInputStream())));
			os.close();
			attachmentRepository.save(Attachment.builder().user(user).uuid(backUuid).type(repo.getByType("Cic_Back_Image")).build());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (OutputStream os = Files.newOutputStream(Paths.get(attachmentsPath + "/" + pUuid + ".jpg"))) {
			os.write(resizeImage(ImageIO.read(person.getInputStream())));
			os.close();
			attachmentRepository.save(Attachment.builder().user(user).uuid(pUuid).type(repo.getByType("Personal_Image")).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
