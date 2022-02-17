package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MetadataTest {
    @Test
    void shouldReturnCorrectFields() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertEquals(1L, metadata.getTotalElements());
        assertEquals(1L, metadata.getTotalPages());
        assertEquals(1L, metadata.getPage());
        assertEquals(1L, metadata.getPerPage());
        assertEquals(null, metadata.getQuery());
        assertTrue(metadata.isFirstPage());
        assertFalse(metadata.isLastPage());

        metadata = new Metadata(1L, 1L, 1L, 1L, "test", true, false);
        assertEquals(1L, metadata.getTotalElements());
        assertEquals(1L, metadata.getTotalPages());
        assertEquals(1L, metadata.getPage());
        assertEquals(1L, metadata.getPerPage());
        assertEquals("test", metadata.getQuery());
    }

    @Test
    void shouldReturnTotalElements() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertEquals(1L, metadata.getTotalElements());

        metadata.setTotalElements(2L);
        assertEquals(2L, metadata.getTotalElements());
    }

    @Test
    void shouldReturnTotalPages() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertEquals(1L, metadata.getTotalPages());

        metadata.setTotalPages(2L);
        assertEquals(2L, metadata.getTotalPages());
    }

    @Test
    void shouldReturnPage() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertEquals(1L, metadata.getPage());

        metadata.setPage(2L);
        assertEquals(2L, metadata.getPage());
    }

    @Test
    void shouldReturnPerPage() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertEquals(1L, metadata.getPerPage());

        metadata.setPerPage(2L);
        assertEquals(2L, metadata.getPerPage());
    }

    @Test
    void shouldReturnQuery() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, "test2", true, false);
        assertEquals("test2", metadata.getQuery());

        metadata.setQuery("test3");
        assertEquals("test3", metadata.getQuery());
    }

    @Test
    void shouldReturnIsFirst() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, true, false);
        assertTrue(metadata.isFirstPage());

        metadata.setFirstPage(false);
        assertFalse(metadata.isFirstPage());
    }

    @Test
    void shouldReturnIsLast() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, null, false, true);
        assertTrue(metadata.isLastPage());

        metadata.setLastPage(false);
        assertFalse(metadata.isLastPage());
    }
}