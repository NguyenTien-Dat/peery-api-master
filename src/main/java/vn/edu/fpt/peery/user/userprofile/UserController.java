package vn.edu.fpt.peery.user.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.common.exceptions.PasswordExceptions;
import vn.edu.fpt.peery.user.ProfileService;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;
import vn.edu.fpt.peery.user.repository.AuthRepository;

@Controller
@RequestMapping
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ProfileService profileService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	AuthRepository repo;

	@GetMapping(path = "/peery/user/userprofile")
	public ResponseEntity<User> getProfile(@RequestParam("userId")Long userId) {
		User user = userService.getUser(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PatchMapping(path = "/peery/user/userprofile")
	public ResponseEntity<Void> updateProfile(@RequestBody User user, @RequestParam Long userId) {
		try {
			User target = userService.getUser(userId);
			target.setFullName(user.getFullName());
			target.setPhone(user.getPhone());
			target.setAddress(user.getAddress());
			target.setEmail(user.getEmail());
			repo.save(target);

			userService.setUnconfirmed(target);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/peery/user/changepassword")
	public ResponseEntity<User> changePassword(@RequestBody ChangePasswordRequest request) {
		try {
			userService.changePassword(request.getUserId(), request.getNewPassword());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (PasswordExceptions e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/peery/user/checkResetToken")
	public ResponseEntity<Long> getUserIdByPassword(@RequestParam Long userId, @RequestParam String resetToken) {
		User target = repo.getUserById(userId);

		if (target != null && target.getPassword().equals(resetToken)) {
			return ResponseEntity.ok(target.getId());
		}

		return ResponseEntity.ok(null);
	}

	@PostMapping(path = "/peery/user/changeemail/{email}")
	public ResponseEntity<String> changeEmail(@PathVariable("email") String newEmail, @RequestParam("userId") Long userId ) {
		try {
			User olduser = userService.getUser(userId);
			profileService.changeEmail(newEmail, olduser);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
