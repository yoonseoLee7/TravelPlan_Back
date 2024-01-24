package travel.plan.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.service.UserService;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@Tag(name = "User", description = "User table 관련 API")
@RestController
@RequestMapping(value = "/api-docs")
public class UserController {
    
    @Autowired
    UserService userService;
    

    // @Operation(summary = "로그인")
    // @RequestMapping(value = "/login", method = RequestMethod.POST)
    // public Map<String, Object> login(@Valid @RequestBody UserDTO loginParam, HttpServletRequest request)
    //         throws Exception {
    //     return userService.findById(loginParam, request);
    // }

    //디비연결 테스트
    // @ResponseBody
    // @RequestMapping(value = "/{testid}", method = RequestMethod.GET)
    // public ResponseEntity<UserDTO> selectOneByID(int testid) throws Exception{
    //     UserDTO userDTO = userService.getUserById(testid);
    //     if(userDTO != null){
    //         return new ResponseEntity<>(userDTO,HttpStatus.OK);
    //     }else{
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    //디비연결테스트 회원리스트 전체조회
    @RequestMapping(value = "/test2", method=RequestMethod.GET)
    public List<UserDTO> selectList() {
        List<UserDTO> list = userService.selectAll();
        log.info("list : {}", list);
        return list;
    }
    
}
