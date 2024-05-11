package vn.edu.fpt.peery.attachmenttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AttachmentTypeRepository extends JpaRepository<AttachmentType, Long> {
	AttachmentType getByType(String type);
}
