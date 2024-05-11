// package vn.edu.fpt.peery.payment;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.time.temporal.ChronoUnit;
// import java.util.Date;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// @Service
// public class PaymentCronJobService {

// 	@Autowired
// 	PaymentRepository paymentRepo;

// 	// @Autowired
// 	// PaymentMail paymentMail;

// 	@Scheduled(cron = "0 45 0 * * ?")
// 	public void sendPaymentReminderEmail() {
// 		// List<Payment> paymentLists = paymentRepo.findAll();

// 		// for (Payment payment : paymentLists) {
// 		// 	LocalDateTime payDate = convertToLocalDateTime(payment.getPayDate());
// 		// 	Long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), payDate);
// 		// 	if (daysRemaining <= 3 && daysRemaining > 0) {
// 		// 		paymentMail.sendMail(payment.getUser().getEmail(), null, "Your payment", "Your payment is going to due in" + daysRemaining + "days.");
// 		// 		System.out.println(payment.getUser().getEmail());
// 		// 	} else if (daysRemaining == 2) {
// 		// 		paymentMail.sendMail(payment.getUser().getEmail(), null, "Your payment", "Your payment is going to due in" + daysRemaining + "days.");
// 		// 		System.out.println(payment.getUser().getEmail());
// 		// 	} else if (daysRemaining == 1) {
// 		// 		paymentMail.sendMail(payment.getUser().getEmail(), null, "Your payment", "Your payment is going to due in" + daysRemaining + "days.");
// 		// 		System.out.println(payment.getUser().getEmail());
// 		// 	}
// 		// }
// 	}

// 	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
// 		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
// 	}
// }
