package vn.edu.fpt.peery.contractstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus, Long> {
	ContractStatus getByName(String name);
}
