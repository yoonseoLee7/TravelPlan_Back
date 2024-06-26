package travel.plan.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Tag(name = "화면 이동", description = "화면 이동 용 Controller")
@Controller
public class ViewController {
    
    @RequestMapping("/detail")
    public String detail(@SessionAttribute(name = "userNick", required = false) String userNick,
        @SessionAttribute(name = "userId", required = false) String userId, Model model, String contentId) {
        if(userNick == null) {
            log.debug("MainController: userNick은 null");
        } else {
            log.debug("MainController: " + userNick);
            model.addAttribute("userNick", userNick);
            model.addAttribute("userId", userId);
        }
        model.addAttribute("contentId", contentId);
        log.info("ViewController-contentId: " + contentId);
        return "detail";
    }

    @RequestMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);//세션없으면 새로 생성 안함
        if(session != null){
            // session.removeAttribute("userId");
            session.invalidate();
        }
        return "redirect:/";
    }
    
}
