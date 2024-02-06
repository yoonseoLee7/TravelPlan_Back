package travel.plan.api.search.service;

import java.util.List;
import java.util.Map;

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

    // 지도 위치 및 혼잡도 표시
    public Map<String, Object> congestion(SearchAreaVO vo) throws Exception;

    // 검색 리스트 아이템 선택 시 추천방문지 호출용
    public Map<String, Object> suggest(SearchAreaVO vo) throws Exception;
}