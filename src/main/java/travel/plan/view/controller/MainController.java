package travel.plan.view.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.data.service.UserService;

@Slf4j
@Controller
public class MainController {

    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String mainView() {
        return "index";
    }

    // 첫 메인화면 진입 시 잠실롯데월드 추천방문지 호출용
    @GetMapping("/suggestInit")
    @ResponseBody
    public Map<String, Object> suggest() throws Exception{
        //37.5110739, 127.09815059
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(127.09815059);
        searchLocationDTO.setMapY(37.5110739);
        searchLocationDTO.setRadius(500);

        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, locationToDetail(searchLocationDTO));
    }

    // 검색 리스트 아이템 선택 시 추천방문지 호출용
    @GetMapping("/suggest")
    @ResponseBody
    public Map<String, Object> suggest(@RequestBody SearchAreaVO vo) throws Exception{
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(vo.getNoorLon());
        searchLocationDTO.setMapY(vo.getNoorLat());
        searchLocationDTO.setRadius(500);

        List<SearchDetailVO> detailList = locationToDetail(searchLocationDTO);
        List<SearchDetailVO> sortList = new ArrayList<>();
        
        int[] rank = getDistance(vo, detailList);
        for(int i = 0; i < detailList.size(); i++) {
            for(int j = 0; j < rank.length; j++) {
                if (i == rank[j] - 1) {
                    sortList.add(detailList.get(j));
                }
            }
        }
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, sortList);
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
    public int[] getDistance(SearchAreaVO vo, List<SearchDetailVO> detailList) {
        List<Double> distance = new ArrayList<>();
        for(var detail: detailList) {
            var result = getDistanceOne(vo.getNoorLat(), vo.getNoorLon(), detail.getMapy(), detail.getMapx());
            distance.add(result);
        }

        // 가져온 좌표들의 가까운 순위대로 표시
        int[] ranks = new int[distance.size()];
        for(int i = 0; i < distance.size(); i++) {
            int rank = distance.size();
            for(int j = 0; j < distance.size(); j++) {
                if(distance.get(i) < distance.get(j)) {
                    rank--;
                }
                ranks[i] = rank;
            }
        }
        return ranks;
    }

    public double getDistanceOne(double latFirst, double lngFirst, double latSecond, double lngSecond) {
        double radius = 6371;
        double dLat = Math.toRadians(latSecond - latFirst);
        double dLng = Math.toRadians(lngSecond = lngFirst);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latFirst)) * Math.cos(Math.toRadians(latSecond))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c * 1000;
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

    @GetMapping("/congestionInit")
    @ResponseBody
    public Map<String, Object> congestion() throws Exception {
        SearchPuzzleDTO dto = new SearchPuzzleDTO();
        dto.setPoiId("187961");
        dto.setNoorLat(37.5110739);
        dto.setNoorLon(127.09815059);

        var level = searchService.searchPuzzle(dto).getCongestionLevel();
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, level);
    }

    @GetMapping("/congestion")
    @ResponseBody
    public Map<String, Object> congestion(SearchAreaVO vo) throws Exception {
        SearchPuzzleDTO dto = new SearchPuzzleDTO();
        dto.setPoiId(vo.getId());
        dto.setNoorLat(vo.getNoorLat());
        dto.setNoorLon(vo.getNoorLon());

        var level = searchService.searchPuzzle(dto).getCongestionLevel();
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, level);
    }


    @RequestMapping(value = "/sendUserInfo", method = RequestMethod.POST)
    public String sendLoginInfo(@RequestParam Map<String, Object> map) throws Exception {
        userService.userJoin(map);
        return "/index";
    }
}
