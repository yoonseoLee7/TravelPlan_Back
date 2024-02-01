package travel.plan.api.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import travel.plan.api.search.service.MainPageService;
import travel.plan.api.search.vo.SearchPuzzleVO;

@RestController
@RequestMapping(value = "/api/search")
public class MainPageController {

    @Autowired
    MainPageService mainPageService;

    //검색어 리스트
    @GetMapping("/searchAreaByName")
    public List<String> searchAreaByName(String searchText) throws Exception {
        return mainPageService.searchAreaByName(searchText);
    }

    //검색어에 따른 혼잡도 
    @GetMapping("/searchPuzzleById")
    public SearchPuzzleVO searchPuzzleById(String searchText) throws Exception {
        return mainPageService.searchPuzzleById(searchText);
    }

}
