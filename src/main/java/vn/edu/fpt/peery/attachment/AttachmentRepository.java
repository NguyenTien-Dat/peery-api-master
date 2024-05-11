package vn.edu.fpt.peery.attachment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.peery.user.User;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

	List<Attachment> getByUser(User user);

	Attachment getById(UUID Uuid);
}
