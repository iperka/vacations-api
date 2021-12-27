package com.iperka.vacations.api.organizations;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/organizations")).andDo(print()).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(post("/organizations")).andDo(print()).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(put("/organizations")).andDo(print()).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(delete("/organizations")).andDo(print()).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
        this.mockMvc.perform(options("/organizations")).andDo(print()).andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("OAuthException")));
    }
}
