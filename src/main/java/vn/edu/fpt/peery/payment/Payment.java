package vn.edu.fpt.peery.payment;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder.Default;
import vn.edu.fpt.peery.contract.Contract;
import vn.edu.fpt.peery.installment.Installment;
import vn.edu.fpt.peery.paymentstatus.PaymentStatus;
import vn.edu.fpt.peery.user.User;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contract_id")
	@ToString.Exclude
	private Contract contract;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@CreationTimestamp
	private Date createDate;

	@UpdateTimestamp
	private Date updateDate;

	private Date dueDate;

	@NonNull
	private Long principalDue;

	private Long interestDue;

	@NonNull
	private Long totalDue;

	@NonNull
	@Default
	private Long amountPaid = 0L;

	@ManyToOne
	@JoinColumn(nullable = false)
	private PaymentStatus status;

	@Column
	private int paymentNum;

	@Column(nullable = false)
	private PaymentType type;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "payment")
	@JsonManagedReference
	private List<Installment> installments;
}