package com.iperka.vacations.api.helpers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iperka.vacations.api.VacationsApiApplication;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Due to better client conversation all API responses can be parsed with the
 * GenericResponse class. The class extends the response with some useful
 * properties like the hostname, timestamp and version. Bare in mind that the
 * version will be null if the API will be ran on the local computer. (Must be
 * packaged as jar in order to function)
 * 
 * TODO: Add deprecation warning.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Data
public class GenericResponse<T> {
    private String version = "v" + VacationsApiApplication.class.getPackage().getImplementationVersion();
    private String host = getHostname();
    private HttpStatus status;
    private String message;
    private final Date timestamp = new Date();
    private T data;
    private Metadata metadata;
    private List<APIError> errors = new ArrayList<>();

    private static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            log.error("Unable to determine hostname.", e);
            return "unknown";
        }
    }

    public GenericResponse(final HttpStatus status, final String message, final String version, final T data,
            final Metadata metadata) {
        this.status = status;
        this.message = message;
        this.version = version;
        this.data = data;
        this.metadata = metadata;
    }

    public GenericResponse(final HttpStatus status) {
        this.status = status;
        this.message = status.getReasonPhrase();
    }

    public GenericResponse(final HttpStatus status, final T data) {
        this.status = status;
        this.message = status.getReasonPhrase();
        this.data = data;
    }

    public static <T> GenericResponse<List<T>> fromPage(final HttpStatus status, final Page<T> page) {
        final GenericResponse<List<T>> response = new GenericResponse<>(status);

        response.data = page.getContent();
        response.metadata = new Metadata(page);

        return response;
    }

    public static <T> GenericResponse<List<T>> fromPage(final HttpStatus status, final Page<T> page,
            final String query) {
        final GenericResponse<List<T>> response = new GenericResponse<>(status);

        response.data = page.getContent();
        response.metadata = new Metadata(page, query);

        return response;
    }

    public static <T> GenericResponse<T> notFound(final String message) {
        final GenericResponse<T> response = new GenericResponse<>(HttpStatus.NOT_FOUND);

        response.addError(new APIError("NotFound", message, 404));

        return response;
    }

    public GenericResponse<T> fromError(final HttpStatus status, final APIError error) {
        final GenericResponse<T> response = new GenericResponse<>(status);
        response.addError(error);
        return response;
    }

    public ResponseEntity<GenericResponse<T>> build() {
        // Apply reason phrase to message field if not set
        if (this.message == null) {
            this.message = status.getReasonPhrase();
        }

        log.debug("Status: {}; Message: {}; Errors: {}", this.getStatus(), this.getMessage(), this.getErrors().size());
        return ResponseEntity.status(status).body(this);
    }

    public void addError(final APIError error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public void addError(final Exception exception) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new APIError(exception));
    }

    @Schema(description = "Hostname serving the response. (Defines Kubernetes pod.)", example = "vacations-api-xxxxxxxxx-xxxxx", required = true)
    public String getHost() {
        return host;
    }

    @Schema(description = "HTTP Status code.", example = "200", required = true)
    public int getStatus() {
        return status.value();
    }

    @Schema(description = "HTTP Status message.", example = "OK", required = true)
    public String getMessage() {
        return message;
    }

    @Schema(description = "API version.", example = "v1.0.2", required = true)
    public String getVersion() {
        return version;
    }

    @Schema(description = "Response generation time.", example = "1639640722592", required = true)
    public long getTimestamp() {
        return timestamp.getTime();
    }

    @Schema(description = "Response data.", required = true)
    public T getData() {
        return data;
    }

    @Schema(description = "Array of error objects containing more information about errors.", example = "[]", required = true)
    public List<APIError> getErrors() {
        return errors;
    }
}
