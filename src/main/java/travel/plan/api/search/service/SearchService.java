package travel.plan.api.search.service;

import java.util.List;

import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;

public interface SearchService {

    // 장소 통합 검색
    public List<SearchPuzzleDTO> searchArea(String searchText) throws Exception;

    // 실시간 장소 혼잡도
    // TODO 리턴 타입 변경 필요
    public void searchPuzzle(SearchPuzzleDTO searchPuzzleDTO) throws Exception;

    // 위치기반 관광정보
    public List<SearchDetailDTO> searchLocation(SearchLocationDTO searchLocationDTO) throws Exception;

    // 공통정보
    public void searchDetail(SearchDetailDTO searchDetailDTO) throws Exception;
}