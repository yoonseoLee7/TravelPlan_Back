package travel.plan.api.search.service.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.util.concurrent.GlobalEventExecutor;
import travel.common.ApiResult;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.service.MainPageService;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.api.search.vo.SearchPuzzleVO;

@Slf4j
@Service
public class MainPageServiceImpl implements MainPageService {

    /*
     * TODO Value 값 null 뜨는거 확인 필요
     */
    @Value("${app.sk.api-key}")
    private String skKey;

    @Value("${app.visitkorea.api-key}")
    private String tourKey;

    @Autowired
    private SearchService searchService;

    //관광지명 명칭 검색 했을 때 결과 리스트
    @Override
    public List<String> searchAreaByName(String searchText) throws Exception {
        List<SearchAreaVO> searchArea = searchService.searchArea(searchText);

        List<String> names = new ArrayList<>();
        try {
            for(SearchAreaVO searchAreaVO : searchArea){
                names.add(searchAreaVO.getName());
            }
        } catch (ApiException e) {
            e.getMessage();
        }
        
      
        return names;

    }

    //검색 리스트에 따른 혼잡도 제공
    @Override
    public SearchPuzzleVO searchPuzzleById(String searchText) throws Exception {
        List<SearchAreaVO> searchArea = searchService.searchArea(searchText);

        SearchPuzzleDTO searchPuzzleDTO = new SearchPuzzleDTO();
        try {
            for(SearchAreaVO searchAreaVO : searchArea){
                searchPuzzleDTO.setPoiId(searchAreaVO.getId());
                searchPuzzleDTO.setNoorLat(searchAreaVO.getNoorLat());
                searchPuzzleDTO.setNoorLon(searchAreaVO.getNoorLon());
            }
        } catch (ApiException e) {
            e.getMessage();
        }
        SearchPuzzleVO searchPuzzleVO = searchService.searchPuzzle(searchPuzzleDTO);        

        return searchPuzzleVO;
        
    }

    

}
