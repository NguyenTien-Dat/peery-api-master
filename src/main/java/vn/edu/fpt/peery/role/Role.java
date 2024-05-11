package vn.edu.fpt.peery.role;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.peery.user.User;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role  {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	@Column(nullable = false)
	@JsonIgnore
	private Set<User> users;

	public Role(String name) {
		this.name = name;
	}
}
