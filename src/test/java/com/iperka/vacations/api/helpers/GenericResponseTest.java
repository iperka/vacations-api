package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class GenericResponseTest {
    @Test
    void shouldSetFields() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK, "Test", "TestVersion", null, null);
        assertEquals(200, response.getStatus());
        assertEquals("Test", response.getMessage());
        assertEquals("TestVersion", response.getVersion());
        assertNull(response.getData());
        assertNull(response.getMetadata());
    }

    @Test
    void shouldReturnCorrectResponseForFromPage() {
        assertEquals(200, GenericResponse.fromPage(HttpStatus.OK, new PageImpl<>(List.of())).getStatus());

        List<String> testList = new ArrayList<>();
        testList.add("Test1");
        assertEquals(testList, GenericResponse.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getData());
        assertEquals(1,
                GenericResponse.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getMetadata().getPage());

        testList.add("Test2");
        assertEquals(testList, GenericResponse.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getData());
        assertEquals(1,
                GenericResponse.fromPage(HttpStatus.ACCEPTED, new PageImpl<>(testList)).getMetadata().getPage());
    }

    @Test
    void shouldReturnHost() throws UnknownHostException {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertEquals(InetAddress.getLocalHost().getHostName(), response.getHost());
        response.setHost("test-2daw");
        assertEquals("test-2daw", response.getHost());
    }

    @Test
    void shouldReturnStatus() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertEquals(200, response.getStatus());
        response.setStatus(HttpStatus.NOT_FOUND);
        assertEquals(404, response.getStatus());
    }

    @Test
    void shouldReturnDefaultMessage() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND);

        assertNotNull(response.build().getBody().getMessage());
        assertNotNull(response.getMessage());
    }

    @Test
    void shouldApplyReasonPhraseIfMessageEmpty() {
        GenericResponse<String> response = new GenericResponse<>(HttpStatus.OK);
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.build().getBody().getMessage());
    }

    @Test
    void shouldReturnMessage() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        response.setMessage("Test");

        assertEquals("Test", response.getMessage());
    }

    @Test
    void shouldReturnVersion() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        response.setVersion("Test");

        assertEquals("Test", response.getVersion());
    }

    @Test
    void shouldReturnTimestamp() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertTrue(response.getTimestamp() > 10000L);
    }

    @Test
    void shouldReturnNullData() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertNull(response.getData());
    }

    @Test
    void shouldReturnData() {
        GenericResponse<String> response = new GenericResponse<>(HttpStatus.OK);
        response.setData("Test");
        assertEquals("Test", response.getData());
    }

    @Test
    void shouldReturnNullMetadata() {
        GenericResponse<?> response = new GenericResponse<>(HttpStatus.OK);
        assertNull(response.getMetadata());
    }

    @Test
    void shouldReturnMetadata() {
        GenericResponse<String> response = new GenericResponse<>(HttpStatus.OK);
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        response.setMetadata(metadata);
        assertEquals(metadata, response.getMetadata());
    }

}