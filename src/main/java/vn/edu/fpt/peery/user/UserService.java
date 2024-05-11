package vn.edu.fpt.peery.user;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import lombok.RequiredArgsConstructor;
import vn.edu.fpt.peery.bankaccounts.BankAccountServices;
import vn.edu.fpt.peery.accountstatus.AccountStatus;
import vn.edu.fpt.peery.accountstatus.AccountStatusRepository;
import vn.edu.fpt.peery.bankaccounts.BankAccount;
import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.common.exceptions.PasswordExceptions;
import vn.edu.fpt.peery.common.exceptions.UserExceptions;
import vn.edu.fpt.peery.role.Role;
import vn.edu.fpt.peery.role.RoleRepository;
import vn.edu.fpt.peery.user.admin.AddRequest;
import vn.edu.fpt.peery.user.login.LoginRequest;
import vn.edu.fpt.peery.user.register.RegisterRequest;
import vn.edu.fpt.peery.user.repository.AdminRepository;
import vn.edu.fpt.peery.user.repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	AuthRepository authRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	Email verifyMail;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	BankAccountServices bankAccountService;

	@Autowired
	AccountStatusRepository accountStatusRepository;

	@Value("${link-base}")
	private String urlBase;

	public String generateCode() {
		int verificationCode = new SecureRandom().nextInt(999999);
		return String.format("%06d", verificationCode);
	}

	// check for user exist with phone
	public User confirmUserExist(String phone) {
		return authRepository.findUserByPhone(phone).orElseThrow(() -> new UserExceptions("User Not Found"));
	}

	// confirm user existed
	private void userConfirm(String phone) {
		Boolean userExisted = authRepository.findUserByPhone(phone).isPresent();
		if (userExisted) {
			throw new UserExceptions("Number has already been used");
		}
	}

	// save User info into a reference
	public User userSave(RegisterRequest request) {
		Role role = roleRepository.findRoleById(request.getRoleId());

		User user = User.builder().password(DigestUtils.sha256Hex(request.getPassword()))
				.fullName(request.getFullname()).email(request.getEmail()).phone(request.getPhone())
				.cicNo(request.getCicNo()).address(request.getAddress()).dob(request.getDob())
				.status(accountStatusRepository.findByName("New")).role(role).build();

		verifyMail.sendMail(
			request.getEmail(),
	"You have registered an account at Peery",
			"Hello " + user.getFullName() + ",\n\nThank you for registering an account at the P2P lending platform Peery. You can now proceed to verify your email address. Kindly be aware that some features won't be available until we've approved your profile, which usually takes 1 to 3 business days."
		);

		return user;
	}

	// save User reference into database
	public User userRegister(RegisterRequest request) {
		userConfirm(request.getPhone());
		User user = authRepository.save(userSave(request));
		return user;
	}

	// save BankAccount info to user
	public User saveBankAccount(BankAccount account, User user) {
		user.setAccounts(List.of(account));
		authRepository.save(user);
		return user;
	}

	public Boolean userVerify(Long userId, String confirmToken) {
		User user = authRepository.getUserById(userId);

		if (user.getStatus().getName().equals("New") && user.getConfirmToken().equals(confirmToken)) {
			user.setStatus(accountStatusRepository.findByName("Unconfirmed"));
			user.setConfirmToken(null);
			authRepository.save(user);

			return true;
		} else {
			return false;
		}
	}

	public void resetConfirmToken(Long userId) throws Exception {
		User user = authRepository.getUserById(userId);
		String email = user.getEmail();

		Date now = new Date();

		if (user.getConfirmToken() != null && now.getTime() - user.getUpdatedDate().getTime() < 60000) {
			throw new Exception("Wait at least 1 minute before requesting a new code!");
		}

		String code = generateCode();
		user.setConfirmToken(code);
		authRepository.save(user);

		verifyMail.sendMail(
			email,
			"Please verify your account at Peery",
			"Hello,\n\nPlease enter this code to verify your account:\n" + code
		);
	}

	public void forgetPassword(String phone, String email) throws Exception {
		User target = authRepository.findUserByPhone(phone).orElseThrow();

		if (!target.getEmail().equals(email)) {
			throw new Exception("No user with such credentials exists!");
		}

		// Dùng luôn mật khẩu đã hash làm token reset
		String resetToken = target.getPassword();
		final String pwResetLink = String.format("%s/changePassword?userId=%s&resetToken=%s", urlBase, target.getId(), resetToken);

		verifyMail.sendMail(
			email,
	"Reset your password at Peery",
			"Hello,\n\nPlease visit the link below to reset your password.\n" + pwResetLink
		);
	}

	public void changePassword(Long userId, String newPassword) {
		User user = authRepository.getUserById(userId);
		user.setPassword(DigestUtils.sha256Hex(newPassword));
		authRepository.save(user);
	}

	public User userLogin(LoginRequest request) {
		User user = confirmUserExist(request.getPhone());

		if (!DigestUtils.sha256Hex(request.getPassword()).equals(user.getPassword())) {
			throw new PasswordExceptions("Password does not match");
		}

		if (user.getStatus() == accountStatusRepository.findByName("New")) {
			throw new UserExceptions("Acount had not been verified");
		}

		return user;
	}

	public User getUser(Long userId) {
		User user = authRepository.getUserById(userId);
		return user;
	}

	public User saveAdmin(AddRequest request) {
		Role role = roleRepository.findRoleByName("Staff");
		User user = User.builder().password(DigestUtils.sha256Hex(request.getPassword()))
				.fullName(request.getFullname()).email(request.getEmail()).phone(request.getPhone())
				.cicNo(request.getCicNo()).address(request.getAddress()).dob(request.getDob())
				.status(accountStatusRepository.findByName("Confirmed")).role(role).build();
		return user;
	}

	public User userRegister(AddRequest request) {
		userConfirm(request.getEmail());
		User user = authRepository.save(saveAdmin(request));
		// verifyMail.sendMail(request.getEmail(), "", "Registration Status", "You have successfully registered./n Please check your email.");
		return user;
	}

	public List<User> getUsersByStatus(AccountStatus status) {
		List<User> users = adminRepository.findByStatus(status);
		users = users.stream().filter(user -> !user.getRole().getName().equalsIgnoreCase("Staff"))
				.collect(Collectors.toList());
		return users;
	}

	public List<User> listOfUser() {
		List<User> users = adminRepository.findAll();
		users = users.stream().collect(Collectors.toList());
		return users;
	}

	public List<User> getUsersByRole(Role role) {
		List<User> users = adminRepository.findByRole(role);
		users = users.stream().filter(user -> !user.getRole().getName().equalsIgnoreCase("Staff"))
				.collect(Collectors.toList());
		return users;
	}

	public List<User> getAllAdmin() {
		List<User> users = adminRepository.findAll();
		users = users.stream().filter(user -> user.getRole().getName().equalsIgnoreCase("Staff"))
				.collect(Collectors.toList());
		return users;
	}

	public void setUnconfirmed(User user) {
		user.setStatus(accountStatusRepository.findByName("Unconfirmed"));
		authRepository.save(user);
	}
}