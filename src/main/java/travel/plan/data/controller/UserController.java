package travel.plan.data.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.service.UserService;



@Slf4j
@Tag(name = "User", description = "User table 관련 API")
@RestController
@RequestMapping(value = "/api-docs")
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping("/checkId")
    public Map<String, Object> checkId(@RequestParam Map<String, Object> map) throws Exception {
        return userService.checkId(map);
    }

    @PostMapping("/sendUserInfo")
    public Map<String, Object> sendLoginInfo(@RequestParam Map<String, Object> map) throws Exception {
        return userService.userJoin(map);
    }

    @GetMapping("/loginCheck")
    public Map<String, Object> loginCheck(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
        return userService.loginCheck(map, request);
    }

    @GetMapping("/getUserInfo")
    public Map<String, Object> changeProfile(@RequestParam Map<String, Object> map) throws Exception {
        return userService.getUserInfo(map);
    }
}
