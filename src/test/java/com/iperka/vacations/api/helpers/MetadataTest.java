package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetadataTest {
    @Test
    void shouldReturnCorrectFields() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        assertEquals(1L, metadata.getTotalElements());
        assertEquals(1L, metadata.getTotalPages());
        assertEquals(1L, metadata.getPage());
        assertEquals(1L, metadata.getPerPage());

        metadata = new Metadata(1L, 1L, 1L, 1L, "test");
        assertEquals(1L, metadata.getTotalElements());
        assertEquals(1L, metadata.getTotalPages());
        assertEquals(1L, metadata.getPage());
        assertEquals(1L, metadata.getPerPage());
        assertEquals("test", metadata.getQuery());
    }

    @Test
    void shouldReturnTotalElements() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        assertEquals(1L, metadata.getTotalElements());

        metadata.setTotalElements(2L);
        assertEquals(2L, metadata.getTotalElements());
    }

    @Test
    void shouldReturnTotalPages() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        assertEquals(1L, metadata.getTotalPages());

        metadata.setTotalPages(2L);
        assertEquals(2L, metadata.getTotalPages());
    }

    @Test
    void shouldReturnPage() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        assertEquals(1L, metadata.getPage());

        metadata.setPage(2L);
        assertEquals(2L, metadata.getPage());
    }

    @Test
    void shouldReturnPerPage() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L);
        assertEquals(1L, metadata.getPerPage());

        metadata.setPerPage(2L);
        assertEquals(2L, metadata.getPerPage());
    }

    @Test
    void shouldReturnQuery() {
        Metadata metadata = new Metadata(1L, 1L, 1L, 1L, "test2");
        assertEquals("test2", metadata.getQuery());

        metadata.setQuery("test3");
        assertEquals("test3", metadata.getQuery());
    }
}
