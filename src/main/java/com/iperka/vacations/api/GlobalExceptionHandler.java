package com.iperka.vacations.api;

import java.text.MessageFormat;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 
     * /** Handle Resource Not Found Exception: That way, we tell Spring Boot to
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

        return response.build();
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

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final Response response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError("RequestParameterException", "Required parameter missing.",
                ex.getParameterName() + " parameter is missing", ex.getParameterName(), 400));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        final Response response = new Response<>(HttpStatus.BAD_REQUEST);
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(e -> response.addError(new APIError("Validation", "Invalid value provided.",
                        String.format("Field '%s' %s.", e.getField(), e.getDefaultMessage()), e.getField(), 400)));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        final Response response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError("InvalidBody", "Cannot deserialize body value.", ex.getLocalizedMessage(), 400));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response response = new Response<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        ex.getSupportedMediaTypes().forEach(t -> response.addError(new APIError("UnsupportedMediaType",
                MessageFormat.format("The content type '{}' is not supported by this resource.", t),
                "The request body content type is not supported by this resource.",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final Response response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(new APIError("NotFound", "No endpoint found for the requested method / location.",
                "Invalid request method and / or location.", 404));

        return response.build();

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        final Response response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(new APIError("NotFound", "No endpoint found for the requested method / location.",
                "Invalid request method and / or location.", 404));

        return response.build();
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Response<Object>> handleAll(final Exception ex, final WebRequest request) {

        final Response<Object> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR);
        response.addError(new APIError(ex));

        return response.build();
    }

}
