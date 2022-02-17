package com.iperka.vacations.api.helpers;

import java.text.MessageFormat;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
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

/**
 * Global exception handler. In Spring exception handling like this can be
 * tricky. Just add exceptions to this class (maybe it works ;D).
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> toResponseEntity(final GenericResponse<Object> response) {
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
    public ResponseEntity<GenericResponse<Object>> handleResourceNotFoundException(final ResourceNotFoundException ex) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<GenericResponse<Object>> handleAccessDeniedException(final AccessDeniedException ex) {
        final String errorMessage = "The request requires higher privileges than provided by the access token.";
        final String cause = "The authenticated user has not been granted the required scope(s).";

        final GenericResponse<Object> responseObject = new GenericResponse<>(HttpStatus.FORBIDDEN);
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
    protected ResponseEntity<GenericResponse<Object>> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
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
    protected ResponseEntity<GenericResponse<Object>> handleIllegalArgumentException(
            final IllegalArgumentException ex) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex));
        log.info("Illegal argument provided.", ex);

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex.getClass().getSimpleName(), "Required parameter missing.",
                ex.getParameterName() + " parameter is missing", ex.getParameterName(), 400));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(e -> response.addError(new APIError(ex.getClass().getSimpleName(), "Invalid value provided.",
                        String.format("Field '%s' %s.", e.getField(), e.getDefaultMessage()), e.getField(), 400)));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex.getClass().getSimpleName(), "Cannot deserialize body value.",
                "JSON parse error while parsing body.", 400));
        log.error("HttpMessageNotReadableException", ex);

        return toResponseEntity(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            APIError apiError = new APIError(
                    "ConstraintViolationException",
                    violation.getMessage(),
                    null,
                    violation.getPropertyPath().toString(),
                    400);
            response.addError(apiError);
        }

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        ex.getSupportedMediaTypes().forEach(t -> response.addError(new APIError(ex.getClass().getSimpleName(),
                MessageFormat.format("The content type '{}' is not supported by this resource.", t),
                "The request body content type is not supported by this resource.",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())));

        return toResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        log.info("Exception:", ex);
        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.NOT_FOUND);
        response.addError(
                new APIError(ex.getClass().getSimpleName(), "No endpoint found for the requested method / location.",
                        "Invalid request method and / or location.", 404));

        return toResponseEntity(response);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.NOT_FOUND);
        response.addError(
                new APIError(ex.getClass().getSimpleName(), "No endpoint found for the requested method / location.",
                        "Invalid request method and / or location.", 404));

        return toResponseEntity(response);
    }

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    public ResponseEntity<Object> handleDataAccessResourceFailureException(DataAccessResourceFailureException ex) {
        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("DataAccessResourceFailureException", ex);
        response.addError(
                new APIError("DataAccessResourceFailureException", ex.getMessage(), 500));

        return toResponseEntity(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> handleAll(final Exception ex, final WebRequest request) {

        final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR);
        response.addError(new APIError(ex));

        return response.build();
    }

}