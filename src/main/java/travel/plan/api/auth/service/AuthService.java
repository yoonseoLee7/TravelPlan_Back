package travel.plan.api.auth.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.api.auth.dto.LoginParam;

public interface AuthService {

    /**
     * 로그인
     *
     * @param loginParam
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String, Object> login(LoginParam loginParam, HttpServletRequest request) throws Exception;
}
