package com.iperka.vacations.api.helpers;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The {@link com.iperka.vacations.api.helpers.Metadata} class defines the
 * structure of a metadata object. This object is mostly used for
 * {@link org.springframework.data.domain.Page} objects and describe their
 * metadata. Use the special constructor to create a object from page object.
 * 
 * Will be serialized as: <code>
 * "metadata": {
 *   "totalElements": 0,
 *   "totalPages": 0,
 *   "page": 1,
 *   "perPage": 20
 * }
 * </code>
 * 
 * @author Michael Beutler
 * @version 0.0.4
 * @since 2021-05-14
 */
public class Metadata {
    private long totalElements = 0;
    private long totalPages = 0;
    private long page = 1L;
    private long perPage = 0;
    private String query;

    public Metadata(long totalElements, long totalPages, long page, long perPage) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.perPage = perPage;
    }

    public Metadata(long totalElements, long totalPages, long page, long perPage, String query) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.perPage = perPage;
        this.query = query;
    }

    public Metadata(Page<?> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Add one to slice number to achieve page 1
        this.page = page.getNumber() + 1L;
        this.perPage = page.getSize();
    }

    public Metadata(Page<?> page, String query) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Add one to slice number to achieve page 1
        this.page = page.getNumber() + 1L;
        this.perPage = page.getSize();
        this.query = query;
    }

    @Schema(description = "Total amount of items matching request.", example = "200", required = true)
    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    @Schema(description = "Maximum amount of pages relative to page size.", example = "2", required = true)
    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    @Schema(description = "Page according to page request.", example = "1", required = true)
    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    @Schema(description = "Response object page size.", example = "20", required = true)
    public long getPerPage() {
        return perPage;
    }

    public void setPerPage(long perPage) {
        this.perPage = perPage;
    }

    @Schema(description = "If the request uses a specific filter query it will be represented.", example = "name=Test", required = true)
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
