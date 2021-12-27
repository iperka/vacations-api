package com.iperka.vacations.api;

import java.text.MessageFormat;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.Response;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> toResponseEntity(Response<Object> response) {
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * 
     * Handle Resource Not Found Exception: That way, we tell Spring Boot to
     * return an HTTP response with NOT_FOUND status code when
     * ResourceNotFoundException is raised!
     * 
     * @param ex {@link ResourceNotFoundException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Object>> handleResourceNotFoundException(final ResourceNotFoundException ex) {

        final Response<Object> response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(new APIError(ex));
        log.info("Resource not found.", ex);

        return response.build();
    }

    /**
     * 
     * Handle authorization Exceptions raised by method level.
     * 
     * @param ex {@link AccessDeniedException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<Object>> handleAccessDeniedException(final AccessDeniedException ex) {
        final String errorMessage = "The request requires higher privileges than provided by the access token.";
        final String cause = "The authenticated user has not been granted the required scope(s).";

        final Response<Object> responseObject = new Response<>(HttpStatus.FORBIDDEN);
        responseObject.addError(new APIError("OAuthException", errorMessage, cause, 403));

        return responseObject.build();
    }

    /**
     * Handle MethodArgumentTypeMismatchException: This exception is thrown when a
     * method parameter has the wrong type!
     * 
     * @param ex      {@link MethodArgumentTypeMismatchException}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Response<Object>> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex));
        log.info("Method Argument mismatch.", ex);

        return response.build();
    }

    /**
     * Handle IllegalArgumentException: This exception is thrown when a
     * method parameter has the wrong type!
     * 
     * @param ex      {@link IllegalArgumentException}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Response<Object>> handleIllegalArgumentException(final IllegalArgumentException ex) {

        final Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex));
        log.info("Illegal argument provided.", ex);

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex.getClass().getSimpleName(), "Required parameter missing.",
                ex.getParameterName() + " parameter is missing", ex.getParameterName(), 400));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(e -> response.addError(new APIError(ex.getClass().getSimpleName(), "Invalid value provided.",
                        String.format("Field '%s' %s.", e.getField(), e.getDefaultMessage()), e.getField(), 400)));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex.getClass().getSimpleName(), "Cannot deserialize body value.",
                ex.getLocalizedMessage(), 400));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        ex.getSupportedMediaTypes().forEach(t -> response.addError(new APIError(ex.getClass().getSimpleName(),
                MessageFormat.format("The content type '{}' is not supported by this resource.", t),
                "The request body content type is not supported by this resource.",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(
                new APIError(ex.getClass().getSimpleName(), "No endpoint found for the requested method / location.",
                        "Invalid request method and / or location.", 404));

        return toResponseEntity(response);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(
                new APIError(ex.getClass().getSimpleName(), "No endpoint found for the requested method / location.",
                        "Invalid request method and / or location.", 404));

        return toResponseEntity(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleAll(final Exception ex, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR);
        response.addError(new APIError(ex));

        return response.build();
    }

}
