package com.serviciotecnico.cliente;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ClienteApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainTest(){
		ClienteApplication.main(new String[] {"--spring.profiles.active=test"});
	}
}
