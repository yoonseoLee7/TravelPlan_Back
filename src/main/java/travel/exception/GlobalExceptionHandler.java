package travel.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    /**
     * API 업무 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException e) {
        log.debug("handleApiException code:{} message:{}", e.getCode(), e.getMessage());

        Map<String, Object> resBody = new HashMap<>();
        resBody.put("code", e.getCode());
        resBody.put("message", e.getMessage());
        return new ResponseEntity<>(resBody, e.getHttpStatus());
    }

    /**
     * 검증 실패
     * @Valid 실패시
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("=");
            builder.append(fieldError.getRejectedValue());
            builder.append("]는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(".");
        }

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", "ValidationError");

        jsonObj.addProperty("message", builder.toString());
        jsonObj.add("body", new JsonObject());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonObj.toString(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class, MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, NoHandlerFoundException.class, TypeMismatchException.class,
            HttpMessageNotReadableException.class, MissingServletRequestPartException.class })
    public ResponseEntity<String> handleException400(HttpServletRequest request, Exception e) {

        JsonObject jsonObj = new JsonObject();

        if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException hmnre = (HttpMessageNotReadableException) e;
            log.debug("hmnre.getRootCause().getClass(): {}", hmnre.getRootCause().getClass());
            if (hmnre.getRootCause() instanceof InvalidFormatException) {
                // invalid json format
                jsonObj.addProperty("code", "ValidationError");
                jsonObj.addProperty("message", hmnre.getLocalizedMessage());
            } else if (hmnre.getRootCause() instanceof JsonParseException) {
                // invalid json format
                jsonObj.addProperty("code", "ValidationError");
                jsonObj.addProperty("message", hmnre.getLocalizedMessage());
            } else {
                log.error("handleException400", e);
                // another format error
                jsonObj.addProperty("code", "BadRequest");
                jsonObj.addProperty("message", e.getLocalizedMessage());
            }
        } else if (e instanceof MissingServletRequestPartException) {
            MissingServletRequestPartException msrpe = (MissingServletRequestPartException) e;
            String missingPart = msrpe.getRequestPartName(); // 누락된 요청 부분 이름
            jsonObj.addProperty("code", "BadRequest");
            jsonObj.addProperty("message", "잘못된 요청입니다.('" + missingPart + "' 누락)");
        } else {
            log.debug("handleException400", e);
            String profile = System.getProperty("spring.profiles.active");
            log.debug("spring.profiles.active: {}", profile);
            String st = "잘못된 요청입니다.";
            if (profile == null) { // 개발에만 적용. 실행시 인자로 받는경우 적용
                st = ExceptionUtils.getStackTrace(e);
            }
            jsonObj.addProperty("code", "BadRequest");
            jsonObj.addProperty("message", st);
            Gson gson = new Gson();
            jsonObj.add("parameter", gson.toJsonTree(request.getParameterMap()).getAsJsonObject());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonObj.toString(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class, MissingPathVariableException.class, ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class, AsyncRequestTimeoutException.class, NullPointerException.class })
    public ResponseEntity<String> handleException500(HttpServletRequest request, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String strStack = sw.toString().replaceAll("\n", "");
        strStack = strStack.substring(0, strStack.length() > 300 ? 300 : strStack.length());

        log.error(String.format("handleException500 %s", strStack), e);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", "InternalServerError");

        String st = "내부 서버 오류";
        String profile = System.getProperty("spring.profiles.active");
        if (profile == null) { // 개발에만 적용
            st = ExceptionUtils.getStackTrace(e);
        }
        jsonObj.addProperty("message", st);
        Gson gson = new Gson();
        jsonObj.add("parameter", gson.toJsonTree(request.getParameterMap()).getAsJsonObject());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonObj.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
