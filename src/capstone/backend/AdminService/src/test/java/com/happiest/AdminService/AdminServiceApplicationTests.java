package com.happiest.AdminService;

import com.happiest.AdminService.utility.RBundle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceApplicationTests {

	@Test
	void contextLoads() {
//		AdminServiceApplication.main(new String[]{});
	}

//	@Test
//	void testMain() {
//		String[] args = {};
//		assertDoesNotThrow(() -> AdminServiceApplication.main(args));
//	}



//	@Test
//	void testGetKey_success() {
//		// Assuming there's a "constant.properties" file with a key "someKey=someValue"
//		String expectedValue = "someValue"; // Update this to the expected value in your properties file
//		String actualValue = RBundle.getKey("someKey");
//		assertEquals(expectedValue, actualValue);
//	}

	@Test
	void testGetKey_keyNotFound() {
		// Assert that the MissingResourceException is thrown when the key does not exist
		assertThrows(MissingResourceException.class, () -> {
			RBundle.getKey("nonExistentKey");
		});
	}


}
