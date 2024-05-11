package vn.edu.fpt.peery.user.register;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import vn.edu.fpt.peery.attachment.AttachmentRequest;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.attachment.BankAccountRequest;
import vn.edu.fpt.peery.bankaccounts.BankAccountServices;
import vn.edu.fpt.peery.banks.Bank;
import vn.edu.fpt.peery.banks.BankService;
import vn.edu.fpt.peery.role.Role;
import vn.edu.fpt.peery.role.RoleService;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;
import vn.edu.fpt.peery.user.repository.AdminRepository;


@RestController
public class RegistrationController {

	@Autowired
	UserService userService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	BankAccountServices bankAccountService;

	@Autowired
	BankService bankService;

	@Autowired
	RoleService roleService;

	@Autowired
	AdminRepository usrRepo;

	@GetMapping("/peery/user/registration")
	public ResponseEntity<RoleAndBankResponse> getAllBank() {
		List<Bank> bankList = bankService.getAllBanks();
		List<Role> roleList = roleService.getRoles();
		RoleAndBankResponse responseList = new RoleAndBankResponse(roleList, bankList);
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}

	@PostMapping(path = "peery/user/registration", consumes = { "multipart/form-data" })
	public ResponseEntity<?> registerUser(RegisterRequest request, AttachmentRequest imgRq,
			BankAccountRequest bankRq, MultipartHttpServletRequest servletRequest, HttpSession session) {
		try {
			User user = userService.userRegister(request);
			bankAccountService.addBankAccount(bankRq, user);
			attachmentService.getMultipleImage(imgRq, user);

			return new ResponseEntity<User>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			System.err.println("An error occurred while registering user:");
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("peery/user/verify")
	public ResponseEntity<Void> verifyAccount(@RequestBody VerifyAccountRequest req) {
		try {
			Boolean result = userService.userVerify(req.getUserId(), req.getConfirmToken());
			if (result)
				return new ResponseEntity<>(HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/peery/user/doVerify")
	public ResponseEntity<String> getVerifyCode(@RequestParam Long userId) {
		try {
			userService.resetConfirmToken(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("TOO_MANY_REQUESTS");
		}

		return ResponseEntity.ok().build();
	}

	@Data
	static class VerifyAccountRequest {
		Long userId;
		String confirmToken;
	}

	@GetMapping("/peery/regCheck")
	public ResponseEntity<Boolean> regCheck(@RequestParam(name = "field") String what, @RequestParam String value) {
		switch (what) {
			case "email":
				return ResponseEntity.ok(usrRepo.findByEmail(value).isPresent());
			case "phone":
				return ResponseEntity.ok(usrRepo.findByPhone(value).isPresent());
			case "cicNo":
				return ResponseEntity.ok(usrRepo.findByCicNo(value).isPresent());
			default:
				return ResponseEntity.internalServerError().build();
		}
	}
}