package com.serviciotecnico.empleado;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmpleadoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainTest(){
		EmpleadoApplication.main(new String[] {"--spring.profiles.active=test"});
	}

}
