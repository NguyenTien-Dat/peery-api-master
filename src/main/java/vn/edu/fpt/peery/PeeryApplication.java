package vn.edu.fpt.peery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import jakarta.annotation.PostConstruct;
import vn.edu.fpt.peery.accountstatus.AccountStatusInit;
import vn.edu.fpt.peery.attachmenttype.AttachmentTypeInitialize;
import vn.edu.fpt.peery.banks.BankInitialize;
import vn.edu.fpt.peery.contractstatus.ContractStatusInitalize;
import vn.edu.fpt.peery.paymentmethod.PaymentMethodInit;
import vn.edu.fpt.peery.paymentstatus.PaymentStatusInitialize;
import vn.edu.fpt.peery.requeststatus.RequestStatusInitialize;
import vn.edu.fpt.peery.role.RoleInitialize;
import vn.edu.fpt.peery.setting.SettingInit;
import vn.edu.fpt.peery.term.TermInitialize;
import vn.edu.fpt.peery.user.repository.UserInit;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableWebSecurity
public class PeeryApplication {
	@Autowired
	RoleInitialize roleInitialize;
	@Autowired
	RequestStatusInitialize offerStatusInit;
	@Autowired
	AttachmentTypeInitialize attachmentTypeInitialize;
	@Autowired
	BankInitialize bankInit;
	@Autowired
	ContractStatusInitalize contractStatusInit;
	@Autowired
	TermInitialize termInit;
	@Autowired
	PaymentStatusInitialize paymentStatusInit;
	@Autowired
	UserInit userInit;
	@Autowired
	AccountStatusInit accountStatusInit;
	@Autowired
	PaymentMethodInit pmInit;
	@Autowired
	SettingInit settingInit;

	public static void main(String[] args) {
		SpringApplication.run(PeeryApplication.class, args);
	}

	@PostConstruct
	public void init() {
		settingInit.init();
		pmInit.generatePaymentMethods();
		roleInitialize.initializeRole();
		offerStatusInit.initializeRequestStatus();
		attachmentTypeInitialize.init();
		bankInit.init();
		contractStatusInit.init();
		termInit.init();
		paymentStatusInit.initializePaymentStatus();
		accountStatusInit.init();
		userInit.init();
	}

}