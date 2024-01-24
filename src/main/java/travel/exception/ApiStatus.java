package travel.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * API 응답상태
 */
@Slf4j
public enum ApiStatus {

        // ------------------------------------------------------------------------
        // 통신 응답부
        // ------------------------------------------------------------------------
        // 성공
        HTTP_OK("Success", "Ok", HttpStatus.OK), // 200
        HTTP_NOT_MODIFIED("Fail", "NotModified", HttpStatus.NOT_MODIFIED), // 304

        // 요청오류
        HTTP_BAD_REQUEST("Fail", "BadRequest", HttpStatus.BAD_REQUEST), // 400 요청 오류
        HTTP_UNAUTHORIZED("Fail", "Unauthorized", HttpStatus.UNAUTHORIZED), // 401 사용자 인증실패: 비밀번호 오류
        HTTP_DENIED("Fail", "Forbidden", HttpStatus.FORBIDDEN), // 403 사용자 인가실패: 권한없음
        HTTP_NOTFOUND("Fail", "NotFound", HttpStatus.NOT_FOUND), // 404
        HTTP_GONE("Fail", "Gone", HttpStatus.GONE), // 410 리소스 사용 만료
        HTTP_SEE_OTHER("Redirect", "SeeOther", HttpStatus.SEE_OTHER), // 303 리다이렉션

        // 서버오류
        HTTP_INTERNAL_SERVER_ERROR("Fail", "InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR), // 500

        // ------------------------------------------------------------------------
        // 업무 응답부
        // ------------------------------------------------------------------------
        // 업무 공통응답 성공
        AP_SUCCESS("Success", "Ok", HttpStatus.OK),
        // 업무 공통응답 실패
        AP_FAIL("Fail", "ApplicationFail", HttpStatus.OK);

        /**
         * 응답코드. 문자열. Success, Fail
         */
        private String code;

        /**
         * 응답메시지. 문자열. 응답설명
         */
        private String message;

        /**
         * 응답의 HTTP Status 객체. HttpStatus.
         */
        private HttpStatus status;

        /**
         * 응답상태 설정
         * 
         * @param code
         * @param message
         * @param status
         */
        ApiStatus(String code, String message, HttpStatus status) {
                this.code = code;
                this.message = message;
                this.status = status;
        };

        public String getCode() {
                return code;
        }

        public String getMessage() {
                return message;
        }

        public HttpStatus getHttpStatus() {
                return status;
        }

        public static HashMap<String, Object> getHashMap(ApiStatus apiStatus) {
                log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(),
                                apiStatus.getMessage());
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("code", apiStatus.getCode());
                resultMap.put("message", apiStatus.getMessage());
                return resultMap;
        }

        public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, String message) {
                log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(), message);
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("code", apiStatus.getCode());
                resultMap.put("message", message);
                return resultMap;
        }

        public static HashMap<String, Object> getHashMap(ApiStatus apiStatus, String message,
                        HashMap<String, Object> resultMap) {
                log.debug("ApiResult > getHashMap code {} message {} resultMap {}", apiStatus.getCode(),
                                message, resultMap);
                resultMap.put("code", apiStatus.getCode());
                resultMap.put("message", message);
                return resultMap;
        }

        public static HashMap<String, Object> getHashMap(HashMap<String, String> resultKey,
                        ApiStatus apiStatus) {
                log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(),
                                apiStatus.getMessage());
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put((resultKey.containsKey("code")) ? resultKey.get("code") : "code",
                                apiStatus.getCode());
                resultMap.put((resultKey.containsKey("message")) ? resultKey.get("message") : "message",
                                apiStatus.getMessage());
                return resultMap;
        }

        public static HashMap<String, Object> getHashMap(HashMap<String, String> resultKey,
                        ApiStatus apiStatus, String message) {
                log.debug("ApiResult > getHashMap code {} message {}", apiStatus.getCode(), message);
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put((resultKey.containsKey("code")) ? resultKey.get("code") : "code",
                                apiStatus.getCode());
                resultMap.put((resultKey.containsKey("message")) ? resultKey.get("message") : "message",
                                message);
                return resultMap;
        }

        public static HashMap<String, Object> getHashMap(HashMap<String, String> resultKey,
                        ApiStatus apiStatus, String message, HashMap<String, Object> resultMap) {
                log.debug("ApiResult > getHashMap code {} message {} resultMap {}", apiStatus.getCode(),
                                message, resultMap);
                resultMap.put((resultKey.containsKey("code")) ? resultKey.get("code") : "code",
                                apiStatus.getCode());
                resultMap.put((resultKey.containsKey("message")) ? resultKey.get("message") : "message",
                                message);
                return resultMap;
        }
}
