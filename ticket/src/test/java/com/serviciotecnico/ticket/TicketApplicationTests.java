package com.serviciotecnico.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TicketApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainTest(){
		TicketApplication.main(new String[] {"--spring.profiles.active=test"});
	}

}
