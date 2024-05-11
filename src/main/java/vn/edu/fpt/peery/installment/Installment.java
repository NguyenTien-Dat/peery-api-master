package vn.edu.fpt.peery.installment;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import vn.edu.fpt.peery.payment.Payment;
import vn.edu.fpt.peery.paymentmethod.PaymentMethod;


@Entity
@Builder @Data @NoArgsConstructor @AllArgsConstructor
public class Installment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JsonBackReference
	@NonNull
	@ToString.Exclude
	private Payment payment;

	@NonNull
	private LocalDate date;

	@Comment("Số tiền trả trong lần thanh toán")
	@NonNull
	private Long amount;

	@ManyToOne
	@NonNull
	private PaymentMethod paymentMethod;

	private String note;

	@NonNull
	private InstallmentStatus status;
}