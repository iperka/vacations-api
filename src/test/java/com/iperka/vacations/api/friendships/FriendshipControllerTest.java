package com.iperka.vacations.api.friendships;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.iperka.vacations.api.friendships.dto.FriendshipDTO;
import com.iperka.vacations.api.security.Scopes;
import com.iperka.vacations.api.vacations.Vacation;
import com.iperka.vacations.api.vacations.VacationRepository;
import com.iperka.vacations.api.vacations.VacationStatus;
import com.iperka.vacations.api.vacations.dto.VacationDTO;

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

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private VacationRepository vacationRepository;

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
	@WithMockUser(username = "test2", authorities = { Scopes.SCOPE_FRIENDSHIPS_WRITE })
	void shouldHandleScopeFriendshipsWrite() throws Exception {
		this.mockMvc.perform(get("/friendships")).andExpect(status().isOk())
				.andExpect(content().string(containsString("OK")));
		this.mockMvc
				.perform(post("/friendships").contentType(MediaType.APPLICATION_JSON)
						.content("{\"user\": \"iperka|wad1213d\",\"status\": \"accepted\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("Created")));
	}

	@Test
	@WithMockUser(username = "test", authorities = { Scopes.SCOPE_FRIENDSHIPS_WRITE, Scopes.SCOPE_VACATIONS_READ })
	void shouldAllowNextEndpointForFriends() throws Exception {
		Vacation vacation = new VacationDTO("Summer Vacation", new Date(), new Date(), 1, VacationStatus.ACCEPTED)
				.toObject();
		vacation.setOwner("test2");
		vacationRepository.save(vacation);

		this.mockMvc
				.perform(post("/friendships").contentType(MediaType.APPLICATION_JSON)
						.content("{\"user\": \"test2\",\"status\": \"accepted\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("Created")));

		this.mockMvc
				.perform(get("/vacations/next?owner=test2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(content().string(containsString("OAuthException")));

		Friendship friendship = new FriendshipDTO("test", FriendshipStatus.ACCEPTED).toObject();
		friendship.setOwner("test2");
		friendshipRepository.save(friendship);

		this.mockMvc
				.perform(get("/vacations/next?owner=test2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Summer Vacation")));
	}
}