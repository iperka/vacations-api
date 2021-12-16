package com.iperka.vacations.api.helpers;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iperka.vacations.api.VacationsApiApplication;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

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
 * @version 0.0.6
 * @since 2021-05-14
 */
public class Response<T> {
    private String version = "v" + VacationsApiApplication.class.getPackage().getImplementationVersion();
    private String host = getHostname();
    private HttpStatus status;
    private String message;
    private final Date timestamp = new Date();
    private T data;
    private Metadata metadata;
    private List<APIError> errors = List.of();

    private static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

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

    public Response(HttpStatus status, T data) {
        this.status = status;
        this.message = status.getReasonPhrase();
        this.data = data;
    }

    public static <T> Response<List<T>> fromPage(HttpStatus status, Page<T> page) {
        Response<List<T>> response = new Response<>(status);

        response.data = page.getContent();
        response.metadata = new Metadata(page);

        return response;
    }

    public static <T> Response<List<T>> fromPage(HttpStatus status, Page<T> page, String query) {
        Response<List<T>> response = new Response<>(status);

        response.data = page.getContent();
        response.metadata = new Metadata(page, query);

        return response;
    }

    public static <T> Response<T> notFound(String message) {
        Response<T> response = new Response<T>(HttpStatus.NOT_FOUND);

        response.addError(new APIError("NotFound", message, 404));

        return response;
    }

    public ResponseEntity<Response<T>> build() {
        // Apply reason phrase to message field if not set
        if (this.message == null) {
            this.message = status.getReasonPhrase();
        }

        return ResponseEntity.status(status).body(this);
    }

    public void addError(APIError error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public void addError(Exception exception) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new APIError(exception));
    }

    @Schema(description = "HTTP Status code.", example = "200", required = true)
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

    @Schema(description = "API version.", example = "v1.0.2", required = true)
    public String getVersion() {
        return version;
    }

    public Response<T> setVersion(String version) {
        this.version = version;
        return this;
    }

    @Schema(description = "Response generation time.", example = "1639640722592", required = true)
    public long getTimestamp() {
        return timestamp.getTime();
    }

    @Schema(description = "Response data.", required = true)
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

    @Schema(description = "Response error objects.", example = "[]", required = true)
    public List<APIError> getErrors() {
        return errors;
    }

    public void setErrors(List<APIError> errors) {
        this.errors = errors;
    }

    @Schema(description = "Kubernetes pod name or API Host hostname.", example = "vacations-api-xxxxxxxxxx-xxxxx", required = true)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
