package travel.plan.data.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/commentCount/{userId}") // 사용자의 총 댓글 개수 조회
  public Map<String, Object> commentCount(@PathVariable(value = "userId") String userId) throws Exception {
    return userService.commentCount(userId);
  }

  @GetMapping("/bookmarkCount/{userId}") // 사용자의 북마크 장소 총 개수 조회
  public Map<String, Object> bookmarkCount(@PathVariable(value = "userId") String userId) throws Exception {
    return userService.bookmarkCount(userId);
  }

  @GetMapping("/commentList/{userId}") // 최신순 댓글 내역 조회
  public Map<String, Object> commentList(@PathVariable(value = "userId") String userId, @RequestParam String count) throws Exception {
    return userService.commentList(userId, count);
  }
  
  @GetMapping("/bookmarkList/{userId}") // 최신순 북마크 내역 조회
  public Map<String, Object> bookmarkList(@PathVariable(value = "userId") String userId, @RequestParam String count) throws Exception {
    return userService.bookmarkList(userId, count);
  }
  
  @PostMapping("/loginLog")
  public Map<String, Object> loginLog(@RequestBody Map<String, Object> userInfo) {
    return userService.loginLog(userInfo);
  }
  
}
