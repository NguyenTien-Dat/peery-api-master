package vn.edu.fpt.peery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import vn.edu.fpt.peery.user.UserService;

@SpringBootTest
class PeeryApplicationTests {
	@Autowired
	UserService userService;
	@Test
	void contextLoads() {
//		userService.resetConfirmToken("hungcl09@gmail.com");
	}

}
