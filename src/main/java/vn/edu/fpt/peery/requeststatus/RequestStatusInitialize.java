package vn.edu.fpt.peery.requeststatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestStatusInitialize {
	@Autowired
	RequestStatusRepository requestStatusRepository;

	public void initializeRequestStatus() {

		RequestStatus status1 = new RequestStatus();
		status1.setName("New");
		requestStatusRepository.save(status1);

		RequestStatus status2 = new RequestStatus();
		status2.setName("Approved");
		requestStatusRepository.save(status2);

		RequestStatus status3 = new RequestStatus();
		status3.setName("Settled");
		requestStatusRepository.save(status3);

		RequestStatus status5 = new RequestStatus();
		status5.setName("Cancelled");
		requestStatusRepository.save(status5);
	}
}