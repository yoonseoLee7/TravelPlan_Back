package travel.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import travel.exception.ApiStatus;

/**
 * Controller 공통 응답처리
 */
@Slf4j
public class ApiResult {

    private static HashMap<String, Object> convert(Object target) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Register the custom module
        objectMapper.registerModule(new JavaTimeModule());

        return (HashMap<String, Object>) objectMapper.convertValue(target, new TypeReference<Map<String, Object>>() {
        });
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus) {
        log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(),
                apiStatus.getMessage());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", apiStatus.getMessage());
        return convert(resultMap);
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, String message) {
        log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(), message);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", message);
        return convert(resultMap);
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, int body) {
        log.debug("ApiResult > getHashMap code {} bodyMap {}", apiStatus.getCode(), body);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", apiStatus.getMessage());
        resultMap.put("body", body);
        return convert(resultMap);
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, List<?> bodyMap) {
        log.debug("ApiResult > getHashMap code {} bodyMap {}", apiStatus.getCode(), bodyMap);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", apiStatus.getMessage());
        resultMap.put("body", bodyMap);
        return convert(resultMap);
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus,
            Map<String, Object> bodyMap) {
        log.debug("ApiResult > getHashMap code {} bodyMap {}", apiStatus.getCode(), bodyMap);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", apiStatus.getMessage());
        resultMap.put("body", bodyMap);
        return convert(resultMap);
    }

    public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, String message,
            HashMap<String, Object> bodyMap) {
        log.debug("ApiResult > getHashMap code {} message {} bodyMap {}", apiStatus.getCode(),
                message, bodyMap);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", apiStatus.getCode());
        resultMap.put("message", message);
        resultMap.put("body", bodyMap);
        return convert(resultMap);
    }

}
