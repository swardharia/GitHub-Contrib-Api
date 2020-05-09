package com.sdharia.webapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdharia.model.UserList;
import com.sdharia.webapi.controller.UserController;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class WebapiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;
	private UserList users;
	private String json;

	@BeforeEach
	void init() {
		objectMapper = new ObjectMapper();
		users = new UserList();
	}

	@Test
	@Ignore
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Ignore
	@Order(2)
	public void testToHandleAuthorizationException() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/gitcontrib/users?location=Pune&toplimit=1")
				.contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();

		String error = mvcResult.getResponse().getErrorMessage();
		assertEquals(error, "Unauthorized request to GitHub API. Add credentials to application.properties file");
	}

	@Test
	@Order(3)
	public void testToHandleConstraintException() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/gitcontrib/users?location=Pune&toplimit=151")
				.contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();

		String error = mvcResult.getResponse().getErrorMessage();
		assertEquals(error, "toplimit: must be less than or equal to 150");
	}

	@Test
	@Order(4)
	public void testToGetGithubUsersDefaultLimit() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/gitcontrib/users?location=Mumbai")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		json = mvcResult.getResponse().getContentAsString();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		users = objectMapper.readValue(json, UserList.class);
		assertEquals(users.getUserList().size(), 50);
	}

	@Test
	@Order(5)
	public void testToGetGithubUsersWithLimit() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/gitcontrib/users?location=Mumbai&toplimit=110")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		json = mvcResult.getResponse().getContentAsString();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		users = objectMapper.readValue(json, UserList.class);
		assertEquals(users.getUserList().size(), 110);
	}
}