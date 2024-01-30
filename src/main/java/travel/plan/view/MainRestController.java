package travel.plan.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Tag(name = "index페이지 api", description = "Index페이지 Rest api용 Controller")
@RestController
@RequestMapping(value = "/api/main")
public class MainRestController {
    
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/suggest", method=RequestMethod.GET)
    public Map<String, Object> requestMethodName(@RequestParam String searchText) throws Exception {

        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(127.09815059);
        searchLocationDTO.setMapY(37.5110739);
        searchLocationDTO.setRadius(500);

        List<SearchLocationVO> locationList = searchService.searchLocation(searchLocationDTO);
        List<SearchDetailVO> detailList = locationList.stream().map(e -> {
            SearchDetailDTO detailDTO = new SearchDetailDTO();
            detailDTO.setContentId(e.getContentid());
            detailDTO.setMobileApp("DEMO");
            detailDTO.setMobileOS("WIN");
            try {
                return searchService.searchDetail(detailDTO);
            } catch (Exception err) {
                log.error("api request error", err);
                throw new ApiException(ApiStatus.AP_FAIL, "API 호출 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
            }
        }).collect(Collectors.toList());

        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, detailList);
    }
    
}
