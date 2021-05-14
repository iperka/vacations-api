package com.iperka.vacations.api.helpers;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The {@link com.iperka.vacations.api.helpers.Response} class defines the
 * default structure for responses. It includes the status, a message,
 * timestamp, version string, data and metadata. This class is a helper function
 * and to get a {@link org.springframework.http.ResponseEntity} call
 * <code>build()</code>. The data field is generic and can be passed within the
 * <>. Some often used use cases are implemented as shortcut method. Like
 * <code>fromPage()</code>. The setter methods are designed to be chained and
 * return the object itself.
 * 
 * @author Michael Beutler
 * @version 0.0.3
 * @since 2021-05-14
 */
public class Response<T> {
    private HttpStatus status;
    private String message;
    private String version = "v0.0.1";
    private final Date timestamp = new Date();
    private T data = null;
    private Metadata metadata = null;

    public Response(HttpStatus status, String message, String version, T data, Metadata metadata) {
        this.status = status;
        this.message = message;
        this.version = version;
        this.data = data;
        this.metadata = metadata;
    }

    public Response(HttpStatus status) {
        this.status = status;
        this.message = status.getReasonPhrase();
    }

    public static <T> Response<List<T>> fromPage(HttpStatus status, Page<T> page) {
        Response<List<T>> response = new Response<>(status);
        response.data = page.getContent();
        response.metadata = new Metadata(page);

        return response;
    }

    public ResponseEntity<Response<T>> build() {
        // Apply reason phrase to message field if not set
        if (this.message == null) {
            this.message = status.getReasonPhrase();
        }

        return ResponseEntity.status(status).body(this);
    }

    public int getStatus() {
        return status.value();
    }

    public Response<T> setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Response<T> setVersion(String version) {
        this.version = version;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Response<T> setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

}
