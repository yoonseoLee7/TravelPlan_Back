package travel.plan.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

    @RequestMapping("/")
    public String mainView(@SessionAttribute(name = "userNick", required = false) String userNick, Model model) {
        if(userNick == null) {
            log.debug("MainController: userId는 null");
        } else {
            log.debug("MainController: " + userNick);
            // 사용자 정보가 있을 경우 로그인/회원가입 텍스트 없애기
            model.addAttribute("userNick", userNick);
        }
        return "index";
    }
}
