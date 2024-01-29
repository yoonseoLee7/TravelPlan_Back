package travel.plan.api.search.service;

import java.util.List;

import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.vo.SearchPuzzleVO;

public interface MainPageService {

    
    //main페이지 관광지명 검색창 리스트
    public List<String> searchAreaByName(String searchText) throws Exception;

    //main페이지 검색 결과에 따른 혼잡도 리스트
    public SearchPuzzleVO searchPuzzleById(String searchText) throws Exception;
    
}
