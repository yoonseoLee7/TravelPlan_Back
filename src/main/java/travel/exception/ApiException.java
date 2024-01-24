package travel.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private ApiStatus status;
    private String message;

    public ApiException(ApiStatus status) {
        this.status = status;
        this.message = status.getMessage();
    }

    public ApiException(ApiStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status.getHttpStatus();
    }

    public ApiStatus getApiStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public String getCode() {
        return status.getCode();
    }

    public String getMessage() {
        return message;
    }

}
