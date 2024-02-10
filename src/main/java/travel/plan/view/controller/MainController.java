package travel.plan.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.service.SearchService;
import travel.plan.data.service.RplyHstrService;
import travel.plan.data.service.UserService;

@Slf4j
@Controller
public class MainController {

    @Autowired
    SearchService searchService;
    @Autowired
    RplyHstrService rplyHstrService;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String mainView(@SessionAttribute(name = "userId", required = false) String userId) {
        if(userId == null) {
            log.debug("MainController: userId는 null");
        } else {
            log.debug("MainController: " + userId);
            // 사용자 정보가 있을 경우 로그인/회원가입 텍스트 없애기
            return "index/userId=" + userId;
        }
        return "index";
    }
}
