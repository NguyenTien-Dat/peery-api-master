package vn.edu.fpt.peery.user.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.peery.accountstatus.AccountStatus;
import vn.edu.fpt.peery.accountstatus.AccountStatusRepository;
import vn.edu.fpt.peery.attachment.AttachmentService;
import vn.edu.fpt.peery.contract.ContractRepository;
import vn.edu.fpt.peery.contractstatus.ContractStatusRepository;
import vn.edu.fpt.peery.payment.PaymentRepository;
import vn.edu.fpt.peery.request.RequestRepository;
import vn.edu.fpt.peery.requeststatus.RequestStatusRepository;
import vn.edu.fpt.peery.role.Role;
import vn.edu.fpt.peery.role.RoleRepository;
import vn.edu.fpt.peery.user.ProfileService;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;
import vn.edu.fpt.peery.user.repository.AdminRepository;

@RestController
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	ProfileService profileService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	ContractRepository ctrRepo;

	@Autowired
	ContractStatusRepository ctrSttRepo;

	@Autowired
	AdminRepository uRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	RequestRepository reqRepo;

	@Autowired
	RequestStatusRepository reqSttRepo;

	@Autowired
	PaymentRepository pmRepo;

	@Autowired
	AccountStatusRepository accSttRepo;

	@GetMapping(path = "/peery/admin/userlist")
	public ResponseEntity<List<User>> getAllUsers(Model model, HttpSession session) {
//		User loggedUser = (User) session.getAttribute("user");
//		if (loggedUser.getRole().getName().equalsIgnoreCase("Staff")) {
		List<User> users;
		users = userService.listOfUser();
		model.addAttribute("users", users);
		return new ResponseEntity<>(users, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}

	@GetMapping(path = "/peery/admin/adminlist")
	public ResponseEntity<List<User>> getAllAdmin(Model model, HttpSession session) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Staff"))) {
		List<User> users;
		users = userService.getAllAdmin();
		model.addAttribute("users", users);
		return new ResponseEntity<>(users, HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}

	}

	@GetMapping(path = "/peery/admin/userdetail")
	public ResponseEntity<User> getUserDetail(@RequestParam("userId") Long userId, Model model, HttpSession session) {

//		User loggedUser = (User) session.getAttribute("user");
//		if (loggedUser.getRole().getName().equalsIgnoreCase("Staff")) {
		User user = profileService.findUserById(userId);
		model.addAttribute("userdetail", user);

		return new ResponseEntity<>(user, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}

	@PostMapping(path = "/peery/admin/addadmin", consumes = { "multipart/form-data" })
	public ResponseEntity<Void> addNewAdmin(AddRequest request) {
		try {
			userService.userRegister(request);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping(path = "/peery/admin/confirmaccount")
	public ResponseEntity<String> confirmAccount(@RequestParam("userId") Long userId, @RequestParam("creditScore") Long cScore) {
		profileService.confirmUserProfile(userId, cScore);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping(path = "/peery/admin/rejectaccount")
	public ResponseEntity<String> rejectAccount(@RequestParam("userId") Long userId, @RequestParam String reason) {
		profileService.rejectUserProfile(userId, reason);
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@GetMapping(path = "/peery/admin/attachment/getattachment", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getAttachment(@RequestParam("Uuid") UUID Uuid) {
		try {
			String fileImage = attachmentService.getAttachment(Uuid);
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

	@GetMapping(path = "/peery/stats", produces = "application/json")
	public ResponseEntity<String> getStats() {
		JsonObject resp = new JsonObject();

		resp.addProperty("totalContracts", ctrRepo.count());
		resp.addProperty("totalContractsSettled", ctrRepo.countByStatus(ctrSttRepo.getByName("Paid in full")));
		resp.addProperty("totalBorrowers", uRepo.countByRole(roleRepo.findRoleById(2L)));
		resp.addProperty("totalLenders", uRepo.countByRole(roleRepo.findRoleById(3L)));
		resp.addProperty("unsettledLoanRequests", reqRepo.countByStatus(reqSttRepo.findStatusByName("Approved")));
		resp.addProperty("totalLoanRequests", reqRepo.count());
		resp.addProperty("totalAmountLent", ctrRepo.totalAmountLent());
		resp.addProperty("totalAmountRepaid", ctrRepo.totalAmountRepaid());

		return ResponseEntity.ok(resp.toString());
	}

	@GetMapping("/peery/accountStatus/list")
	public ResponseEntity<List<AccountStatus>> accSttList() {
		return ResponseEntity.ok(accSttRepo.findAll());
	}

	@GetMapping("/peery/role/list")
	public ResponseEntity<List<Role>> roleList() {
		return ResponseEntity.ok(roleRepo.findAll());
	}
}

