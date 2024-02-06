package travel.plan.view;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.data.service.RplyHstrService;
import travel.plan.data.service.UserService;


@Slf4j
@Tag(name = "index페이지 api", description = "Index페이지 Rest api용 Controller")
@RestController
@RequestMapping(value = "/api/main")
public class MainRestController {
    
    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @Autowired
    RplyHstrService rplyHstrService;

    // 지도에 검색지 표시
    @GetMapping("/congestion")
    public Map<String, Object> congestion(@ModelAttribute SearchAreaVO vo) throws Exception {
        return searchService.congestion(vo);
    }

    // 검색 리스트 아이템 선택 시 추천방문지 호출용
    @GetMapping("/suggest")
    public Map<String, Object> suggest(@ModelAttribute SearchAreaVO vo) throws Exception{
        return searchService.suggest(vo);
    }

    @PostMapping("/checkId")
    public Map<String, Object> checkId(@RequestParam Map<String, Object> map) throws Exception {
        return userService.checkId(map);
    }

    @PostMapping("/sendUserInfo")
    public Map<String, Object> sendLoginInfo(@RequestParam Map<String, Object> map) throws Exception {
        return userService.userJoin(map);
    }
    
    //댓글창에 최신 5개 정렬
    @GetMapping("/getCommets")
    public Map<String,Object> getComments(@PathVariable String contTypeId) throws Exception{
        return rplyHstrService.getComments(contTypeId);
    }

    //댓글테이블 정보저장
    @PostMapping("/saveComment")
    public Map<String, Object> saveComment(@RequestBody Map<String,Object> map) throws Exception{
        return rplyHstrService.saveComment(map);
    }

    //검색어 관련 리스트
    @GetMapping("/searchList")
    @ResponseBody
    public Map<String,Object> searchList(@RequestParam String searchText) throws Exception {
        

        return searchService.searchList(searchText);
    }


}
