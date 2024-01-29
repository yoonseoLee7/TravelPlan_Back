package travel.plan.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String mainView() {
        return "index";
    }

    // 일단 임시로 넣어두고 명칭은 나중에 변경...
    @RequestMapping("/modalLogin")
    public String modalLogin() {
        return "modalLogin";
    }

    @RequestMapping("/modalJoin")
    public String modalJoin() {
        return "modalJoin";
    }
}
