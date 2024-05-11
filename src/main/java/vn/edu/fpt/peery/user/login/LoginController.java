package vn.edu.fpt.peery.user.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.peery.attachment.AttachmentRequest;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.common.exceptions.PasswordExceptions;
import vn.edu.fpt.peery.common.exceptions.UserExceptions;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;

@RequestMapping
@RestController
public class LoginController {
	@Autowired
	UserService userService;

	@Autowired
	AttachmentService attachmentService;

	@PostMapping(path = "/peery/user/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
		try {
			User user = userService.userLogin(request);
			try {
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				// System.out.println(user.getAuthorities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (PasswordExceptions | UserExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(path = "/peery/user/forgotpassword")
	public ResponseEntity<?> forgetPass(@RequestBody ForgetPasswordRequest req) {
		try {
			userService.forgetPassword(req.getPhone(), req.getEmail());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("NX_USER");
		}
	}

	@GetMapping(path = "/peery/user/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(path = "/peery/user/attachment/addAsset", consumes = { "multipart/form-data" })
	public ResponseEntity<Void> addAttachment(AttachmentRequest.AssetDocument req, @RequestParam Long userId) {
		User user = userService.getUser(userId);
		attachmentService.addAttachment(req.getAttachment(), req.getDescription(), user);
		userService.setUnconfirmed(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(path = "/peery/user/attachment/getattachment", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getAttachment(@RequestParam("uuid") UUID uuid) {
		try {
			String fileImage = attachmentService.getAttachment(uuid);
			File file = new File(fileImage);
			if (!file.exists()) {
				throw new FileNotFoundException("Image not found");
			}
			InputStream inputStream = new FileInputStream(file);
			byte[] Image = IOUtils.toByteArray(inputStream);
			return new ResponseEntity<>(Image, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path="/peery/user/attachment/deleteattachment")
	public ResponseEntity<?> deleteAttachment(@RequestParam("uuid") UUID uuid, @RequestParam Long userId) {
		try {
			attachmentService.deleteAttachment(uuid);
			User user = userService.getUser(userId);
			userService.setUnconfirmed(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/peery/user/attachment/updateCic", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateCicPhotos(AttachmentRequest req, @RequestParam Long userId) {
		User user = userService.getUser(userId);
		attachmentService.updateCic(req.getFrontImage(), req.getBackImage(), req.getPersonalImage(), user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}