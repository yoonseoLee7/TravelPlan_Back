package travel.plan.view.controller;

import java.util.ArrayList;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;

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
