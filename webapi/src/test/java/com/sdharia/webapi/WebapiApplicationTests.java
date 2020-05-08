package com.sdharia.webapi;

import com.sdharia.model.UserList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebapiApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	public void testToGetGithubUsersDefaultLimit() {
		ResponseEntity<UserList> response = testRestTemplate.
				getForEntity("/api/gitcontrib/users?location=Mumbai", UserList.class);
		assertEquals(response.getStatusCode(), (HttpStatus.OK));
		assertEquals(response.getBody().getUserList().size(), 50);
	}

	@Test
	public void testToGetGithubUsersWithLimit() {
		ResponseEntity<UserList> response = testRestTemplate.
				getForEntity("/api/gitcontrib/users?location=Pune&toplimit=110", UserList.class);
		assertEquals(response.getStatusCode(), (HttpStatus.OK));
		assertEquals(response.getBody().getUserList().size(), 110);
	}

}
