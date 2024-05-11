package vn.edu.fpt.peery.setting;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Setting {
	@Id
	private String key;

	@NonNull
	private String description;

	@NonNull
	private String value;
}