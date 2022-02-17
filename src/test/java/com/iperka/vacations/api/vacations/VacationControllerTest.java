package com.iperka.vacations.api.vacations;

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
class VacationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/vacations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(post("/vacations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(put("/vacations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(delete("/vacations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(options("/vacations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
    }

    @Test
    @WithMockUser(username = "test", authorities = { Scopes.SCOPE_VACATIONS_READ })
    void shouldHandleScopeVacationsRead() throws Exception {
        this.mockMvc.perform(get("/vacations")).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));

        this.mockMvc.perform(post("/vacations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(put("/vacations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(delete("/vacations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
    }

    @Test
    @WithMockUser(username = "test", authorities = { Scopes.SCOPE_VACATIONS_WRITE })
    void shouldHandleScopeVacationsWrite() throws Exception {
        this.mockMvc.perform(get("/vacations")).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
        this.mockMvc
                .perform(post("/vacations").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"name\": \"Summer Vacation\",\"startDate\": \"2022-02-17T18:32:22.418Z\",\"endDate\": \"2022-02-17T18:32:22.418Z\",\"days\": 1.5,\"status\": \"requested\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Created")));
    }
}