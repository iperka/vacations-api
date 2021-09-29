package com.iperka.vacations.api.helpers;

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

            default:
                this.type = exception.getClass().getName();
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
