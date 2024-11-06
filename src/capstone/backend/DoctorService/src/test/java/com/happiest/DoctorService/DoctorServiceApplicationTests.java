package com.happiest.DoctorService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class DoctorServiceApplicationTests {

	@Test
	void contextLoads() {
//		DoctorServiceApplication.main(new String[]{});
	}
	@Test
	void testMain() {
		String[] args = {};
		assertDoesNotThrow(() -> DoctorServiceApplication.main(args));
	}

}
