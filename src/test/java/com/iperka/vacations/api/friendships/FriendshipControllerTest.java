package com.iperka.vacations.api.friendships;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.iperka.vacations.api.security.Scopes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FriendshipControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnUnauthorized() throws Exception {
		this.mockMvc.perform(get("/friendships")).andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(post("/friendships")).andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(put("/friendships")).andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(delete("/friendships")).andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(options("/friendships")).andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString("OAuthException")));
	}

	@Test
	@WithMockUser(username = "test", authorities = { Scopes.SCOPE_FRIENDSHIPS_READ })
	void shouldHandleScopeFriendshipsRead() throws Exception {
		this.mockMvc.perform(get("/friendships")).andExpect(status().isOk())
				.andExpect(content().string(containsString("OK")));

		this.mockMvc.perform(post("/friendships")).andExpect(status().isForbidden())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(put("/friendships")).andExpect(status().isForbidden())
				.andExpect(content().string(containsString("OAuthException")));
		this.mockMvc.perform(delete("/friendships")).andExpect(status().isForbidden())
				.andExpect(content().string(containsString("OAuthException")));
	}

	@Test
	@WithMockUser(username = "test", authorities = { Scopes.SCOPE_FRIENDSHIPS_WRITE })
	void shouldHandleScopeFriendshipsWrite() throws Exception {
		this.mockMvc.perform(get("/friendships")).andExpect(status().isOk())
				.andExpect(content().string(containsString("OK")));
		this.mockMvc
				.perform(post("/friendships").contentType(MediaType.APPLICATION_JSON)
						.content(
								"{\"user\": \"iperka|wad1213d\",\"status\": \"requested\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("Created")));
	}
}