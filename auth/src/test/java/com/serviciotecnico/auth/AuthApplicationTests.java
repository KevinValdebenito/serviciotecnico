package com.serviciotecnico.auth;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTests {

    @Test
    void contextLoads() {
        
    }

    @Test
    void mainMethodTest() {

        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
        
            mocked.when(() -> SpringApplication.run(AuthApplication.class, new String[] {}))
                  .thenReturn(null);
            
            AuthApplication.main(new String[] {});
            
            mocked.verify(() -> SpringApplication.run(AuthApplication.class, new String[] {}));
        }
    }
}