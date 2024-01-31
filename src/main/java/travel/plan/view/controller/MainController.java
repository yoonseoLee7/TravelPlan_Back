package travel.plan.view.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.service.MainPageService;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;

@Slf4j
@Controller
public class MainController {

    @Autowired
    SearchService searchService;

    @RequestMapping("/")
    public String mainView() {
        return "index";
    }

    @RequestMapping(value = "/suggest", method=RequestMethod.GET)
    public ModelAndView suggest(@RequestParam String searchText, ModelAndView model) throws Exception{
        //37.5110739, noorLon=127.09815059
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(127.09815059);
        searchLocationDTO.setMapY(37.5110739);
        searchLocationDTO.setRadius(500);

        List<SearchLocationVO> locationList = searchService.searchLocation(searchLocationDTO);
    
        List<SearchDetailVO> detailList = new ArrayList<SearchDetailVO>();
        SearchDetailDTO detailDTO = new SearchDetailDTO();
        for(int i = 0; i < locationList.size(); i++) {
            detailDTO.setContentId(locationList.get(i).getContentid());
            detailDTO.setMobileApp("DEMO");
            detailDTO.setMobileOS("WIN");
            detailList.add(searchService.searchDetail(detailDTO));
        }

        for(int i = 0; i < detailList.size(); i++) {
            System.out.println(detailList.get(i).getFirstimage());
        }
        model.addObject("suggest", detailList);
        model.setViewName("index");
        return model; 
    }

    //검색 후 리스트
    @GetMapping("/searchList")
    @ResponseBody
    public List<String> searchList(@RequestParam String searchText) throws Exception{
        
        List<SearchAreaVO> searchArea = new ArrayList<SearchAreaVO>();
        
        try {
            searchArea = searchService.searchArea(searchText);
        } catch (Exception e) {
            log.error("api request error", e);
            throw new ApiException(ApiStatus.AP_FAIL, "장소통합검색 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        List<String> names = new ArrayList<>();
            for (SearchAreaVO searchAreaVO : searchArea) {
                names.add(searchAreaVO.getName());
            }
            
        return names;
    }

 
    // @RequestMapping(value = "/searchList", method=RequestMethod.GET)
    // public ModelAndView searchList(@RequestParam String searchText, ModelAndView model) throws Exception{
    //     List<SearchAreaVO> searchArea = new ArrayList<SearchAreaVO>();
    //     searchArea = searchService.searchArea(searchText);
                
    //     List<String> names = new ArrayList<>();
        
    //     for (SearchAreaVO searchAreaVO : searchArea) {
    //             names.add(searchAreaVO.getName());
    //         }
    //     model.addObject("searchList", names);
    //     model.setViewName("index");
    //     return model;
    // }


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
