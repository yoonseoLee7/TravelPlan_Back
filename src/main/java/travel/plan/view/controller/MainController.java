package travel.plan.view.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
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

    // 첫 메인화면 진입 시 잠실롯데월드 추천방문지 호출용
    @GetMapping("/suggestInit")
    @ResponseBody
    public List<SearchDetailVO> suggest() throws Exception{
        //37.5110739, noorLon=127.09815059
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(127.09815059);
        searchLocationDTO.setMapY(37.5110739);
        searchLocationDTO.setRadius(500);

        return locationToDetail(searchLocationDTO);
    }

    // 검색 리스트 아이템 선택 시 추천방문지 호출용
    @GetMapping("/suggest")
    @ResponseBody
    public List<SearchDetailVO> suggest(@RequestBody SearchAreaVO vo) throws Exception{
        System.out.println(vo);
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(vo.getNoorLon());
        searchLocationDTO.setMapY(vo.getNoorLat());
        searchLocationDTO.setRadius(500);

        List<SearchDetailVO> detailList = locationToDetail(searchLocationDTO);

        getDistance(vo, detailList);

        return detailList;
    }

    private List<SearchDetailVO> locationToDetail(SearchLocationDTO searchLocationDTO) throws Exception {
        List<SearchLocationVO> locationList = searchService.searchLocation(searchLocationDTO);
    
        List<SearchDetailVO> detailList = new ArrayList<SearchDetailVO>();
        SearchDetailDTO detailDTO = new SearchDetailDTO();
        for(int i = 0; i < locationList.size(); i++) {
            detailDTO.setContentId(locationList.get(i).getContentid());
            detailDTO.setMobileApp("DEMO");
            detailDTO.setMobileOS("WIN");
            detailList.add(searchService.searchDetail(detailDTO));
        }

        return detailList; 
    }

    // 두 좌표 사이의 거리 값 측정
    public void getDistance(SearchAreaVO vo, List<SearchDetailVO> detailList) {
        List<Double> distance = new ArrayList<>();
        for(var detail: detailList) {
            var result = getDistanceOne(vo.getNoorLat(), vo.getNoorLon(), detail.getMapy(), detail.getMapx());
            distance.add(result);
        }
        // System.out.println(distance);
    }

    public double getDistanceOne(double latFirst, double lngFirst, double latSecond, double lngSecond) {
        double radius = 6371;
        double dLat = Math.toRadians(latSecond - latFirst);
        double dLng = Math.toRadians(lngSecond = lngFirst);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latFirst)) * Math.cos(Math.toRadians(latSecond))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c * 1000;
        System.out.println("dddddd:" + d);
        return d;
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

    @RequestMapping("/modalComment")
    public String modalComment() {
        return "modalComment";
    }

    @RequestMapping("/detail")
    public String detail() {
        return "detail";
    }
}
