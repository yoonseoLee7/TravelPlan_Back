package travel.plan.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "화면 이동", description = "화면 이동 용 Controller")
@Controller
public class ViewController {
    
    // 일단 임시로 넣어두고 명칭은 나중에 변경...
    @RequestMapping("/modalLogin")
    public String modalLogin() {
        return "modalLogin";
    }

    @RequestMapping("/modalJoin")
    public String modalJoin() {
        return "modalJoin";
    }

    @RequestMapping("/modalComment")
    public String modalComment() {
        return "modalComment";
    }

    @RequestMapping("/detail")
    public String detail() {
        return "detail";
    }
}
