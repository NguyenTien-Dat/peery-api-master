package vn.edu.fpt.peery.contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import lombok.Setter;
import lombok.ToString;
import vn.edu.fpt.peery.contractstatus.ContractStatus;
import vn.edu.fpt.peery.payment.Payment;
import vn.edu.fpt.peery.request.Request;
import vn.edu.fpt.peery.term.Term;
import vn.edu.fpt.peery.user.User;

@Entity @Table(name = "contracts")
@Getter @Setter @Builder @ToString @NoArgsConstructor @AllArgsConstructor
public class Contract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User borrower;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Request request;

	@Column
	private Long amountPrincipal;

	@Column
	private Long amountRemaining;

	private Long amountInterest;

	@ManyToOne(fetch = FetchType.EAGER)
	private Term term;

	@Column(precision = 5, scale = 4)
	private BigDecimal apr;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User lender;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;

	@Column
	private Date closeDate;

	@ManyToOne
	@JoinColumn(nullable = false)
	private ContractStatus status;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "contract")
	private List<Payment> payments;
}