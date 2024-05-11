package vn.edu.fpt.peery.attachment;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.peery.attachmenttype.AttachmentType;
import vn.edu.fpt.peery.user.User;

@Entity
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Attachment {

	@Id
	@Column(nullable = false)
	private UUID uuid;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "type_id", nullable = false)
	@Nullable
	private AttachmentType type;

	@Column
	private String description;
}