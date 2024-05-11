package vn.edu.fpt.peery.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import vn.edu.fpt.peery.bankaccounts.BankAccountRepository;
import vn.edu.fpt.peery.common.CommonAndUtils;
import vn.edu.fpt.peery.common.Email;
import vn.edu.fpt.peery.common.exceptions.UserExceptions;
import vn.edu.fpt.peery.requeststatus.RequestStatus;
import vn.edu.fpt.peery.requeststatus.RequestStatusRepository;
import vn.edu.fpt.peery.term.Term;
import vn.edu.fpt.peery.term.TermRepository;
import vn.edu.fpt.peery.user.User;
import vn.edu.fpt.peery.user.UserService;

@Service
public class RequestService {

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RequestStatusRepository requestStatusRepository;

	@Autowired
	BankAccountRepository bankAccountRepo;

	@Autowired
	TermRepository termRepo;

	@Autowired
	UserService userService;

	@Autowired
	CommonAndUtils cu;

	@Autowired
	Email email;

	@Value("${link-base}")
	private String urlBase;

	LocalDate today = LocalDate.now();
	Month currentMonth = today.getMonth();

	public void createRequest(RequestClass request, User user) {
		BigDecimal userAPR = cu.calculateAPR(user);

		if (user.getRole().getName().equalsIgnoreCase("Borrower")) {
			RequestStatus status = requestStatusRepository.findStatusByName("New");
			Term requestTerm = termRepo.getTermById(request.getTermId());
			Request offer = Request.builder()
				.borrower(user)
				.amount(request.getAmount())
				.term(requestTerm)
				.apr(userAPR)
				.status(status)
				.note(request.getNote())
				.build();
			requestRepository.save(offer);
		} else {
			throw new UserExceptions("User do not have authorities to create new loan request");
		}
	}

	public Request getRequestByID(Long id, User lender) {
		Request offer = requestRepository.getOfferDetailByIdAndBorrower(id, lender);
		return offer;
	}

	public Request getRequestById(Long id) {
		Request request = requestRepository.getRequestById(id);
		return request;
	}

	public void approveRequest(Long id) {
		Request request = requestRepository.getRequestById(id);
		RequestStatus status = requestStatusRepository.findStatusByName("Approved");
		request.setStatus(status);
		requestRepository.save(request);

		email.sendMail(
			request.getBorrower().getEmail(),
			"Your loan request has been approved",
			String.format("Hello %s,\n\nWe are pleased to inform you that your recent loan request has been successfully approved by our system.\n\nYou can review your loan request here: %s\n\nYou will receive notification upon a lender's acceptance of your loan request. Subsequently, a contract will be promptly generated.", request.getBorrower().getFullName(), String.format("%s/requestDetails/%d", urlBase, request.getId()))
		);
	}

	public void rejectRequest(Long id) {
		Request request = requestRepository.getRequestById(id);
		RequestStatus status = requestStatusRepository.findStatusByName("Cancelled");
		request.setStatus(status);

		requestRepository.save(request);

		email.sendMail(
			request.getBorrower().getEmail(),
			"Your request has been rejected",
			"Hello,\n\nWe regret to inform you that your loan request has been rejected by us. Please try to adjust the loan amount, or specify more clearly why you need the loan and how you will pay it back.\n\nWe are sorry for any inconvenience and truly appreciate your understanding."
		);
	}

	public void settleRequest(Request request) {
		request.setStatus(requestStatusRepository.findStatusByName("Settled"));
		requestRepository.save(request);
	}

	public void cancelRequest(Long id) {
		Request request = requestRepository.getRequestById(id);
		request.setStatus(requestStatusRepository.findStatusByName("Cancelled"));
		requestRepository.save(request);
	}

	public void deleteRequest(Long id) {
		Request request = requestRepository.getRequestById(id);
		requestRepository.delete(request);
	}

	public List<Request> getRequestByUser(User user) {
		List<Request> offerList = requestRepository.findByBorrower(user);
		return offerList;
	}

	public List<Request> getAllRequest() {
		List<Request> offerList = requestRepository.findAll();
		return offerList;
	}

	public List<Request> getApprovedRequest() {
		List<Request> requestList = requestRepository.findByStatus(requestStatusRepository.findStatusByName("Approved"));
		return requestList;
	}
}
