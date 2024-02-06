package travel.plan.view.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.data.dto.RplyHstrDTO;
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

    //검색어 관련 리스트
    @GetMapping("/searchList")
    @ResponseBody
    public List<SearchAreaVO> searchList(@RequestParam String searchText) throws Exception {
        List<SearchAreaVO> searchArea = new ArrayList<SearchAreaVO>();

        try {
            searchArea = searchService.searchArea(searchText);
        } catch (Exception e) {
            log.error("api request error", e);
            throw new ApiException(ApiStatus.AP_FAIL, "장소통합검색 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        return searchArea;
    }

    //댓글테이블 정보저장
    @PostMapping("/saveComment")
    public void saveComment(@RequestBody RplyHstrDTO rplyHstrDTO){
        rplyHstrService.saveComment(rplyHstrDTO);
    }
    
    //댓글창에 최신 5개 정렬
    @GetMapping("/getCommets")
    public List<RplyHstrDTO> getComments(@PathVariable String cont_type_id) {
        return rplyHstrService.getComments(cont_type_id);
    }
}
