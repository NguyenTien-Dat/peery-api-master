package vn.edu.fpt.peery.requeststatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, Long> {
	RequestStatus findStatusByName(String name);
}
