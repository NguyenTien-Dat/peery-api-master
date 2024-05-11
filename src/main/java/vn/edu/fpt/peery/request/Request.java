package vn.edu.fpt.peery.request;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.peery.requeststatus.RequestStatus;
import vn.edu.fpt.peery.term.Term;
import vn.edu.fpt.peery.user.User;

@Entity
@Table(name = "requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User borrower;

	private Long amount;

	@ManyToOne(fetch = FetchType.EAGER)
	private Term term;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;

	@Column(precision = 5, scale = 4)
	private BigDecimal apr;

	@ManyToOne
	@JoinColumn(nullable = false)
	private RequestStatus status;

	private String note;

	public Request(User borrower, Long amount, Term term, BigDecimal apr, RequestStatus status, String note) {
		this.borrower = borrower;
		this.amount = amount;
		this.term = term;
		this.apr = apr;
		this.status = status;
		this.note = note;
	}
}