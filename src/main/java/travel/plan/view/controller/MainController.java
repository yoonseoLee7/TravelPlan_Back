package travel.plan.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String mainView() {
        return "index";
    }

    

    
    
    
}
