package travel.plan.api.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.auth.dto.LoginParam;
import travel.plan.api.auth.service.AuthService;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * 로그인
     */
    @Override
    public Map<String, Object> login(LoginParam loginParam, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        if (loginParam.getUserId().equals("yeji") && loginParam.getPassword().equals("asdf1234")) {
            result.put("RSLT_CD", "Success");
            result.put("RSLT_MSG", "Login Success");
            log.debug("Login Success ====== {}", loginParam);
            return result;
        }

        result.put("RSLT_CD", "Fail");
        result.put("RSLT_MSG", "Authentication Fail");
        log.error("Login Fail ====== {}", loginParam);

        return result;
    }

}
