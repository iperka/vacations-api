package com.iperka.vacations.api.helpers;

import org.springframework.data.domain.Page;

/**
 * The {@link com.iperka.vacations.api.helpers.Metadata} class defines the
 * structure of a metadata object. This object is mostly used for
 * {@link org.springframework.data.domain.Page} objects and describe their
 * metadata. Use the special constructor to create a object from page object.
 * 
 * Will be serialized as:
 * <code>
 * "metadata": {
 *   "totalElements": 0,
 *   "totalPages": 0,
 *   "page": 1,
 *   "perPage": 20
 * }
 * </code>
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-05-14
 */
public class Metadata {
    private long totalElements = 0;
    private long totalPages = 0;
    private long page = 1L;
    private long perPage = 0;

    public Metadata(long totalElements, long totalPages, long page, long perPage) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.perPage = perPage;
    }

    public Metadata(Page<?> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Add one to slice number to achieve page 1
        this.page = page.getNumber() + 1L;
        this.perPage = page.getSize();
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPerPage() {
        return perPage;
    }

    public void setPerPage(long perPage) {
        this.perPage = perPage;
    }

}
