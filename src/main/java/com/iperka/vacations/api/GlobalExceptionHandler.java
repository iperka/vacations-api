package com.iperka.vacations.api;

import java.text.MessageFormat;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.Response;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle Resource Not Found Exception: That way, we tell Spring Boot to return
     * an HTTP response with NOT_FOUND status code when ResourceNotFoundException is
     * raised!
     * 
     * @param ex {@link ResourceNotFoundException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Response<Object> response = new Response<>(HttpStatus.NOT_FOUND);
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
    protected ResponseEntity<Response<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.addError(new APIError(ex));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Response response = new Response<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        ex.getSupportedMediaTypes()
                .forEach(t -> response.addError(new APIError("UnsupportedMediaType",
                        MessageFormat.format("The content type '{}' is not supported by this resource.", t.toString()),
                        "The request body content type is not supported by this resource.",
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())));

        return response.build();
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Response response = new Response<>(HttpStatus.NOT_FOUND);
        response.addError(new APIError("NotFound", "No endpoint found for the requested method / location.",
                "Invalid request method and / or location.", 404));

        return response.build();

    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Response<Object>> handleAll(Exception ex, WebRequest request) {

        Response<Object> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR);
        response.addError(new APIError(ex));

        return response.build();
    }
}
