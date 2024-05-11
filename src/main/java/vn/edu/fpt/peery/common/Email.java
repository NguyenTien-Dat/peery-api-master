package vn.edu.fpt.peery.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class Email {

	@Autowired
	private JavaMailSender javaMailSender;

	private static String signature = "Best regards,\nPeery team";

	public void sendMail(String email, String subject, String text) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text + "\n\n" + signature);

			javaMailSender.send(mimeMessage);

			log.info(String.format("Sending an email to: %s, subject: %s", email, subject));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}