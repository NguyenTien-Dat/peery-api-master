package vn.edu.fpt.peery.user;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.fpt.peery.accountstatus.AccountStatusRepository;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.common.exceptions.UserExceptions;
import vn.edu.fpt.peery.user.repository.ProfileRepository;
import vn.edu.fpt.peery.user.userprofile.UpdateRequest;


@Service
@RequiredArgsConstructor
public class ProfileService {

	@Autowired
	private final ProfileRepository profileRepository;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	AccountStatusRepository accountStatusRepository;

	@Autowired
	Email verifyMail;

	public User confirmUserExist(String email) {
		return profileRepository.findUserByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
	}

	public User saveUser(UpdateRequest request, String email) {
		User user = confirmUserExist(email);
		if (user.getAddress() != request.getAddress()) {
			// System.out.println(request.getAddress());
			user.setAddress(request.getAddress());
		}
		if (user.getPhone() != request.getPhone()) {
			// System.out.println(request.getPhone());
			user.setPhone(request.getPhone());
		}
		if (user.getDob() != request.getDob()) {
			user.setDob(request.getDob());
		}
		if (user.getFullName() != request.getFullname()) {
			user.setFullName(request.getFullname());
		}
		user.setStatus(accountStatusRepository.findByName("UPDATING"));
		profileRepository.save(user);
		return user;
	}

	public User changeEmail(String email, User user) {
		int verificationToken = new SecureRandom().nextInt(999999);
		if (user.getEmail() != email) {
			user.setEmail(email);
			user.setConfirmToken(String.valueOf(verificationToken));
			user.setStatus(accountStatusRepository.findByName("UPDATING"));
			// verifyMail.sendMail(email, String.valueOf(verificationToken),
					// "Update profile verification step", "Please use this token to verify your profile update: ");
		}
		profileRepository.save(user);
		return user;
	}

	public void userVerify(String confirmToken) {
		int counter = 0;
		User user = profileRepository.findUserByConfirmToken(confirmToken);
		if (user.getStatus() == accountStatusRepository.findByName("New")) {
			if (counter >= 2) {
//				mailExist(user.getEmail());
			}
			if (!confirmToken.equals(user.getConfirmToken())) {
				counter++;
				return;
			}
			user.setStatus(accountStatusRepository.findByName("Unconfirmed"));
			user.setConfirmToken(null);
		}
		profileRepository.save(user);
	}

	public User findUserById(Long id) {
		User user = profileRepository.findById(id).orElseThrow(() -> new UserExceptions("User Not Found"));
		return user;
	}

//	private String fullImagePath(String path) {
//		File image = new File(path);
//		String fullPath = image.getAbsolutePath();
//		return fullPath;
//	}

	public void confirmUserProfile(Long id, Long cScore) {
		User user = profileRepository.findById(id).orElseThrow(() -> new UserExceptions("User Not Found"));
		int creditScore = cScore.intValue();

		Boolean newUser = false;
		if (user.getStatus().getName().equals("New")) {
			newUser = true;
		}

		user.setStatus(accountStatusRepository.findByName("Confirmed"));
		user.setCreditScore((int) creditScore);

		profileRepository.save(user);

		verifyMail.sendMail(
			user.getEmail(),
			newUser ? "Your registration has been confirmed" : "Your profile has been re-evaluated",
			String.format(
				"Dear %s,\n\nWe want to inform you that your profile has been %s by our staff. Taking into account the information provided and the assets declared by you, we have determined to assign a credit score of %d to your profile.",
				user.getFullName(),
				newUser ? "confirmed" : "re-evaluated",
				creditScore
			)
		);
	}

	public void rejectUserProfile(Long id, String reason) {
		User user = profileRepository.findById(id).orElseThrow(() -> new UserExceptions("User Not Found"));

		if (user.getStatus().getName().equals("New")) {
			user.setStatus(accountStatusRepository.findByName("Terminated"));
			verifyMail.sendMail(
				user.getEmail(),
				"Your registration has been rejected",
				"Hello,\n\nAfter reviewing your submission, it has been determined that you do not meet the criteria for registering at Peery.\n\nThe reason for this decision is as follows: " + reason + "\n\nWe apologize for any inconvenience this may have caused. Nevertheless, you have the option to create a new account at any time. Thank you for placing your trust in us."
			);
		} else if (user.getStatus().getName().equals("Unconfirmed")) {
			verifyMail.sendMail(
				user.getEmail(),
				"Your new profile information has been rejected",
				"Hello,\n\nWe have received your new profile information. After careful consideration, we regret to inform you that we are unable to fulfill your request.\n\nThe reason for this decision is as follows: " + reason + "\n\nKindly take a moment to review your profile information and submit any relevant documents that accurately reflect the updated information. We apologize for any inconvenience and appreciate your trust in us."
			);
		} else {
			user.setStatus(accountStatusRepository.findByName("Terminated"));
			verifyMail.sendMail(
				user.getEmail(),
				"Your account has been terminated",
				"Hello,\n\nWe regret to inform you that your account at Peery has been terminated. As a part of our duties, we regularly check all profiles for inactivity or false information.\n\nThe reason for this decision is as follows: " + reason + "\n\nWe apologize for any inconvenience this may have caused. Nevertheless, you have the option to create a new account at any time. Thank you for placing your trust in us."
			);
		}

		profileRepository.save(user);
	}
}