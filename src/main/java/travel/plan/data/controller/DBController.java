package travel.plan.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.service.UserService;

@Slf4j
@Tag(name = "User", description = "User table 관련 API")
@RestController
@RequestMapping(value = "/api-docs")
public class DBController {
    
    @Autowired
    UserService userService;

    @Operation(summary = "로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@Valid @RequestBody UserDTO loginParam, HttpServletRequest request)
            throws Exception {
        return userService.findById(loginParam, request);
    }

}
