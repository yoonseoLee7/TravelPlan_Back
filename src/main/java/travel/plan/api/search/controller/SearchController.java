package travel.plan.api.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.api.search.vo.SearchPuzzleVO;

@Slf4j
@Tag(name = "Search", description = "장소 검색 관련 API")
@RestController
@RequestMapping(value = "/api/search")
public class SearchController {
    
    @Autowired
    SearchService searchService;

    @Operation(summary = "장소통합검색", description = "입력한 검색어와 일치하는 장소의 정보를 조회")
    @RequestMapping(value = "/searchArea", method = RequestMethod.GET)
    public List<SearchAreaVO> searchArea(@RequestParam String searchText) throws Exception {
        return searchService.searchArea(searchText);
    }

    @Operation(summary = "장소혼잡도검색", description = "특정 장소의 가장 최근에 집계된 혼잡도 정보를 조회")
    @RequestMapping(value = "/searchPuzzle", method = RequestMethod.GET)
    public SearchPuzzleVO searchPuzzle(SearchPuzzleDTO searchPuzzle) throws Exception {
        return searchService.searchPuzzle(searchPuzzle);
    }

    @Operation(summary = "위치기반관광정보검색", description = "특정 장소의 주변 관광지 정보 목록을 조회")
    @RequestMapping(value = "/searchLocation", method = RequestMethod.GET)
    public List<SearchLocationVO> searchLocation(SearchLocationDTO searchLocation) throws Exception {
        return searchService.searchLocation(searchLocation);
    }

    @Operation(summary = "공통정보검색", description = "특정 장소에 대한 상세정보를 조회")
    @RequestMapping(value = "/searchDetail", method = RequestMethod.GET)
    public SearchDetailVO searchDetail(SearchDetailDTO searchDetailDTO) throws Exception {
        return searchService.searchDetail(searchDetailDTO);
    }
}
