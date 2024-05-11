package vn.edu.fpt.peery.user;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.peery.accountstatus.AccountStatus;
import vn.edu.fpt.peery.attachment.Attachment;
import vn.edu.fpt.peery.bankaccounts.BankAccount;
import vn.edu.fpt.peery.role.Role;

@Entity @Table(name = "users")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// Mật khẩu ở dạng SHA-256
	@Column(length = 64, nullable = false)
	private String password;

	@Column(nullable = false)
	private String fullName;

	@Column(unique = true, length = 254, nullable = false)
	private String email;

	@Column(unique = true, length = 31, nullable = false)
	private String phone;

	// Số thẻ căn cước
	@Column(unique = true, length = 12, nullable = false)
	private String cicNo;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@Nullable
	private Role role;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;

	@ManyToOne
	@JoinColumn
	private AccountStatus status;

	@Column
	private String confirmToken;

	@Column
	private int creditScore;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<BankAccount> accounts;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	@JsonManagedReference
	private List<Attachment> attachments;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
	}

	public User(String password, String fullName, String email, String phone, String cicNo, String address, Date dob, Role role, AccountStatus status, int creditScore) {
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.cicNo = cicNo;
		this.address = address;
		this.dob = dob;
		this.role = role;
		this.status = status;
		this.creditScore = creditScore;
	}

}