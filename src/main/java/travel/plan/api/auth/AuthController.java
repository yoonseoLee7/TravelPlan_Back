package travel.plan.api.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.auth.dto.LoginParam;
import travel.plan.api.auth.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Tag(name = "Auth", description = "Authorization 관련 API")
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    /**
     * 로그인
     *
     * @param loginParam
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@Valid @RequestBody LoginParam loginParam, HttpServletRequest request)
            throws Exception {
        return authService.login(loginParam, request);
    }
}
