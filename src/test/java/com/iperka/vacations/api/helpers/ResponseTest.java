package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

@SpringBootTest
class ResponseTest {
    @Test
    void shouldSetFields() {
        Response<?> response = new Response<>(HttpStatus.OK, "Test", "TestVersion", null, null);
        assertEquals(200, response.getStatus());
        assertEquals("Test", response.getMessage());
        assertEquals("TestVersion", response.getVersion());
        assertNull(response.getData());
        assertNull(response.getMetadata());
    }

    @Test
    void shouldReturnCorrectResponseForFromPage() {
        assertEquals(200, Response.fromPage(HttpStatus.OK, new PageImpl<>(List.of())).getStatus());

        List<String> testList = new ArrayList<>();
        testList.add("Test1");
        assertEquals(testList, Response.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getData());
        assertEquals(1, Response.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getMetadata().getPage());

        testList.add("Test2");
        assertEquals(testList, Response.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getData());
        assertEquals(1, Response.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getMetadata().getPage());
    }

    @Test
    void shouldReturnStatus() {
        Response<?> response = new Response<>(HttpStatus.OK);
        assertEquals(200, response.getStatus());
        response.setStatus(HttpStatus.NOT_FOUND);
        assertEquals(404, response.getStatus());
    }

    @Test
    void shouldReturnDefaultMessage() {
        Response<?> response = new Response<>(HttpStatus.OK);
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND);

        assertNotNull(response.build().getBody().getMessage());
        assertNotNull(response.getMessage());
    }

    @Test
    void shouldApplyReasonPhraseIfMessageEmpty() {
        Response<String> response = new Response<>(HttpStatus.OK);
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.build().getBody().getMessage());
    }

    @Test
    void shouldReturnMessage() {
        Response<?> response = new Response<>(HttpStatus.OK);
        response.setMessage("Test");

        assertEquals("Test", response.getMessage());
    }

    @Test
    void shouldReturnVersion() {
        Response<?> response = new Response<>(HttpStatus.OK);
        response.setVersion("Test");

        assertEquals("Test", response.getVersion());
    }

    @Test
    void shouldReturnTimestamp() {
        Response<?> response = new Response<>(HttpStatus.OK);
        assertNotNull(response.getTimestamp());
    }

    @Test
    void shouldReturnNullData() {
        Response<?> response = new Response<>(HttpStatus.OK);
        assertNull(response.getData());
    }

    @Test
    void shouldReturnData() {
        Response<String> response = new Response<>(HttpStatus.OK);
        response.setData("Test");
        assertEquals("Test", response.getData());
    }

    @Test
    void shouldReturnNullMetadata() {
        Response<?> response = new Response<>(HttpStatus.OK);
        assertNull(response.getMetadata());
    }

    @Test
    void shouldReturnMetadata() {
        Response<String> response = new Response<>(HttpStatus.OK);
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        response.setMetadata(metadata);
        assertEquals(metadata, response.getMetadata());
    }

}
