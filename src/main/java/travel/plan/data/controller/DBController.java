package travel.plan.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.mapper.UserMapper;
import travel.plan.data.service.UserService;
import travel.plan.data.service.impl.UserServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


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

    //디비연결 테스트
    @GetMapping("/test")
    public ResponseEntity<Map<String,Object>> getUserById(@PathVariable int testid) throws Exception{
        UserDTO user = userService.getUserById(testid);

        if(user != null){
            Map<String,Object> response = new HashMap<>();
            response.put("user",user);
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.notFound().build();
        }
        
    }
        

}
