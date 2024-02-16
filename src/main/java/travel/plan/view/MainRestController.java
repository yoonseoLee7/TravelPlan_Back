package travel.plan.view;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.data.dto.RplyHstrDTO;
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

    //poiId로 검색 관광지 댓글 로딩
    @GetMapping("/getComments")
    public Map<String,Object> getCommentsForPoi(@RequestBody String poiId) throws Exception{
        
        return rplyHstrService.getCommentsForPoi(poiId);
    }

    //댓글테이블 정보저장
    @PostMapping("/saveComment")
    @ResponseBody // dto객체 json으로 변환하기위해
    public Map<String, Object> saveComment(@SessionAttribute(name = "userId", required = false) Integer userId, @RequestBody RplyHstrDTO rplyHstrDTO) throws Exception{
        Map<String,Object> rply = new HashMap<>();
        try {
         rplyHstrDTO.setRegrId(userId);
         rply = rplyHstrService.saveComment(rplyHstrDTO);
        } catch (Exception e) {
            // 현재 로그인 되어 있는 상태가 아님
            log.error("defined userId", e);
            }        
        return rply;
    }

    //검색어 관련 리스트
    @GetMapping("/searchList")
    @ResponseBody
    public Map<String,Object> searchList(@RequestParam String searchText) throws Exception {
        return searchService.searchList(searchText);
    }
}
