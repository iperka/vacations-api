package com.iperka.vacations.api.helpers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class APIError {
    private String type = "unknown";
    private String message = "n/a";
    private String cause;
    private int code = -1;
    private String field;

    public APIError(Exception exception) {

        switch (exception.getClass().getName()) {
            case "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException":
                this.type = "IllegalMethodArgument";
                this.cause = "Invalid type provided in URL argument.";
                this.code = 400;
                this.field = "URL";
                break;
            case "org.springframework.dao.DataIntegrityViolationException":
                this.type = "DataIntegrityViolationException";
                this.cause = "Invalid type provided in URL argument.";
                this.message = "An unhandled exception has been occurred.";
                this.code = 500;
                break;

            default:
                this.type = exception.getClass().getSimpleName();
                break;
        }

        this.message = exception.getLocalizedMessage();
    }

    public APIError(String type, String message, int code) {
        this.type = type;
        this.message = message;
        this.code = code;
    }

    public APIError(String type, String message, String cause, int code) {
        this.type = type;
        this.message = message;
        this.cause = cause;
        this.code = code;
    }

    public APIError(String type, String message, String cause, String field, int code) {
        this.type = type;
        this.message = message;
        this.cause = cause;
        this.field = field;
        this.code = code;
    }
}
