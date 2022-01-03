package com.iperka.vacations.api.organizations;

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
class OrganizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/organizations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(post("/organizations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(put("/organizations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(delete("/organizations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(options("/organizations")).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
    }

    @Test
    @WithMockUser(username = "test", authorities = { Scopes.SCOPE_ORGANIZATIONS_READ })
    void shouldHandleScopeOrganizationsRead() throws Exception {
        this.mockMvc.perform(get("/organizations")).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));

        this.mockMvc.perform(post("/organizations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(put("/organizations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(delete("/organizations")).andExpect(status().isForbidden())
                .andExpect(content().string(containsString("OAuthException")));
    }

    @Test
    @WithMockUser(username = "test", authorities = { Scopes.SCOPE_ORGANIZATIONS_WRITE })
    void shouldHandleScopeOrganizationsWrite() throws Exception {
        this.mockMvc.perform(get("/organizations")).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
        this.mockMvc
                .perform(post("/organizations").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"iperkaTest\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Created")));
    }
}
