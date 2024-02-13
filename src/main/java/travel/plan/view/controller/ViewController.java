package travel.plan.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.vo.SearchDetailVO;

@Slf4j
@Tag(name = "화면 이동", description = "화면 이동 용 Controller")
@Controller
public class ViewController {
    
    // 일단 임시로 넣어두고 명칭은 나중에 변경...
    @RequestMapping("/modalComment")
    public String modalComment() {
        return "modalComment";
    }

    @RequestMapping("/detail")
    public String detail(@SessionAttribute(name = "userId", required = false) String userId
    , @ModelAttribute("detail") SearchDetailVO vo
    ) {
        if(userId == null) {
            log.debug("MainController: userId는 null");
        } else {
            log.debug("MainController: " + userId);
            // 사용자 정보가 있을 경우 로그인/회원가입 텍스트 없애기
            // model.addAttribute("userId", userId);
        }
        // model.addAttribute("detail", vo);
        return "detail";
    }

    @RequestMapping("/myPage")
    public String myPage() {
        return "myPage";
    }
}
