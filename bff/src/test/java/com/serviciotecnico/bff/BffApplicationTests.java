package com.serviciotecnico.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BffApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainShouldStartWithoutThrowing() {
		assertDoesNotThrow(() -> BffApplication.main(new String[] {}));
	}

}
