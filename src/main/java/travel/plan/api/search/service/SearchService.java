package travel.plan.api.search.service;

import java.util.List;

import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.api.search.vo.SearchPuzzleVO;

public interface SearchService {

    // 장소 통합 검색
    public List<SearchAreaVO> searchArea(String searchText) throws Exception;

    // 실시간 장소 혼잡도
    public SearchPuzzleVO searchPuzzle(SearchPuzzleDTO searchPuzzleDTO) throws Exception;

    // 위치기반 관광정보
    public List<SearchLocationVO> searchLocation(SearchLocationDTO searchLocationDTO) throws Exception;

    // 공통정보
    public SearchDetailVO searchDetail(SearchDetailDTO searchDetailDTO) throws Exception;
}