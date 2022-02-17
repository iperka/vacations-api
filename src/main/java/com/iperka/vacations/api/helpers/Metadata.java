package com.iperka.vacations.api.helpers;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Metadata object indicating paging and sorting. Simplified Page object
 * provided by Spring Boot.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class Metadata {
    private long totalElements = 0;
    private long totalPages = 0;
    private long page = 1L;
    private long perPage = 0;
    private String query = null;
    private boolean firstPage = false;
    private boolean lastPage = false;

    public Metadata(Page<?> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Add one to slice number to achieve page 1
        this.page = page.getNumber() + 1L;
        this.perPage = page.getSize();
        this.firstPage = page.isFirst();
        this.lastPage = page.isLast();
    }

    public Metadata(Page<?> page, String query) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Add one to slice number to achieve page 1
        this.page = page.getNumber() + 1L;
        this.perPage = page.getSize();
        this.query = query;
        this.firstPage = page.isFirst();
        this.lastPage = page.isLast();
    }

    @Schema(description = "Total amount of items matching request.", example = "200", required = true)
    public long getTotalElements() {
        return totalElements;
    }

    @Schema(description = "Maximum amount of pages relative to page size.", example = "2", required = true)
    public long getTotalPages() {
        return totalPages;
    }

    @Schema(description = "Page according to page request.", example = "1", required = true)
    public long getPage() {
        return page;
    }

    @Schema(description = "Response object page size.", example = "20", required = true)
    public long getPerPage() {
        return perPage;
    }

    @Schema(description = "If the request uses a specific filter query it will be represented.", example = "name=Test", required = true)
    public String getQuery() {
        return query;
    }

    @Schema(description = "If requested page is the first, it will be true.", example = "true", required = true)
    public boolean isFirstPage() {
        return firstPage;
    }

    @Schema(description = "If requested page is the last, it will be true.", example = "false", required = true)
    public boolean isLastPage() {
        return lastPage;
    }
}
