package vn.edu.fpt.peery.request;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.peery.common.CommonAndUtils;
import vn.edu.fpt.peery.contract.Contract;
import vn.edu.fpt.peery.contract.ContractService;
import vn.edu.fpt.peery.requeststatus.RequestStatus;
import vn.edu.fpt.peery.requeststatus.RequestStatusRepository;
import vn.edu.fpt.peery.term.Term;
import vn.edu.fpt.peery.term.TermRepository;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;
import vn.edu.fpt.peery.user.repository.AdminRepository;

@RestController
public class RequestController {

	@Autowired
	RequestService requestService;

	@Autowired
	ContractService contractService;

	@Autowired
	AdminRepository adRepo;

	@Autowired
	UserService userService;

	@Autowired
	TermRepository termRepo;

	@Autowired
	CommonAndUtils cu;

	@Autowired
	RequestStatusRepository rsRepo;

	@PostMapping("peery/request/createrequest")
	public ResponseEntity<Void> createRequest(@RequestBody RequestClass request) {
		try {
			User loggedUser = userService.getUser(request.getUserId());
			requestService.createRequest(request, loggedUser);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/peery/request/terms")
	public ResponseEntity<?> getRequestTerms() {
		List<Term> listOfTerms = termRepo.findAll();
		return new ResponseEntity<>(listOfTerms, HttpStatus.OK);
	}

	@GetMapping(path = "/peery/request/preparerequest", produces = "application/json")
	public ResponseEntity<?> prepareRequest(@RequestParam Long userId, @RequestParam Long termId, @RequestParam Long amount) {
		try {
			User loggeduser = userService.getUser(userId);
			Term term = termRepo.getTermById(termId);
			BigDecimal userAPR = cu.calculateAPR(loggeduser);

			Long totalInterest = BigDecimal.valueOf(amount * userAPR.doubleValue()).setScale(0, RoundingMode.UP).longValue();
			Long totalAmountDue = amount + totalInterest;
			Long monthlyDue = BigDecimal.valueOf((double) totalAmountDue / term.getNumberOfMonth()).setScale(0, RoundingMode.UP).longValue();

			JsonObject resp = new JsonObject();
			resp.addProperty("apr", userAPR);
			resp.addProperty("monthlyDue", monthlyDue);
			resp.addProperty("totalDue", totalAmountDue);

			return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("peery/request/requestlist")
	public ResponseEntity<List<Request>> getRequestList(@RequestParam Long userId) {
		List<Request> offerList;

		if (userId != null) {
			User loggedUser = userService.getUser(userId);

			if (loggedUser.getRole().getName().equals("Borrower")) {
				offerList = requestService.getRequestByUser(loggedUser);
				return new ResponseEntity<>(offerList, HttpStatus.OK);

			} else if (loggedUser.getRole().getName().equals("Staff")) {
				offerList = requestService.getAllRequest();
				return new ResponseEntity<>(offerList, HttpStatus.OK);

			} else if (loggedUser.getRole().getName().equals("Lender")) {
				offerList = requestService.getApprovedRequest();
				return new ResponseEntity<>(offerList, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(requestService.getAllRequest(), HttpStatus.OK);
	}

	@GetMapping("peery/request/requestdetail")
	public ResponseEntity<Request> getRequestDetail(@RequestParam("requestId") Long requestId, @RequestParam("userId") Long userId) {
		User loggedUser = userService.getUser(userId);
		if (loggedUser.getRole().getName().equalsIgnoreCase("Borrower")) {
			Request offer = requestService.getRequestByID(requestId, loggedUser);
			return new ResponseEntity<>(offer, HttpStatus.OK);
		} else if (loggedUser.getRole().getName().equalsIgnoreCase("Lender")) {
			Request offer = requestService.getRequestById(requestId);
			return new ResponseEntity<>(offer, HttpStatus.OK);
		} else {
			Request offer = requestService.getRequestById(requestId);
			return new ResponseEntity<>(offer, HttpStatus.OK);
		}
	}

	@PatchMapping("peery/request/approverequest")
	public ResponseEntity<Request> approveRequest(@RequestParam("requestId") Long requestId, @RequestParam(name = "userId", required = false) Long userId) {
		try {
			requestService.approveRequest(requestId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PatchMapping("peery/request/rejectrequest")
	public ResponseEntity<Request> rejectRequest(@RequestParam("requestId") Long requestId,
			@RequestParam(name = "userId", required = false) Long userId, HttpSession session) {
		User loggedUser = userService.getUser(userId);
		if (loggedUser.getRole().getName().equals("Staff")) {
			requestService.rejectRequest(requestId);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/peery/request/settlerequest")
	public ResponseEntity<Contract> settleRequest(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "requestId", required = true) Long id) {
		try {
			User loggedUser = userService.getUser(userId);
			Contract newContr = contractService.createContract(id, loggedUser);

			return new ResponseEntity<>(newContr, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PatchMapping("/peery/request/cancelrequest")
	public ResponseEntity<Request> cancelRequest(@RequestParam(name = "userId", required = false) Long userId,
			@RequestParam("requestId") Long id) {
		User loggedUser = userService.getUser(userId);
		if (loggedUser.getRole().getName().equals("Borrower")) {
			requestService.cancelRequest(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/peery/requestStatus/list")
	public ResponseEntity<List<RequestStatus>> getRequestStatuses() {
		return ResponseEntity.ok(rsRepo.findAll());
	}
}