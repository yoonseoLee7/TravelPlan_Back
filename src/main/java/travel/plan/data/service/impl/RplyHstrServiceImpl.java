package travel.plan.data.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiStatus;
import travel.plan.api.search.service.SearchService;
import travel.plan.data.dto.RplyHstrDTO;
import travel.plan.data.mapper.RplyHstrMapper;
import travel.plan.data.service.RplyHstrService;

@Slf4j
@Service
public class RplyHstrServiceImpl implements RplyHstrService{
    @Autowired
    RplyHstrMapper rplyHstrMapper;

    @Autowired
    SearchService searchService;

    //댓글테이블에 저장
    @Override
    public Map<String,Object> saveComment(RplyHstrDTO rplyHstrDTO){
        RplyHstrDTO save = rplyHstrMapper.saveComment(rplyHstrDTO);
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,save);  
    }

    //메인페이지 댓글 로딩
    @Override
    public Map<String, Object> getCommentsForPoi(String poiId){
        RplyHstrDTO rplyHstrDTO = new RplyHstrDTO();
        rplyHstrDTO.setPoiId(poiId);

        //가져오고
        List<Map<String,Object>> getrply = rplyHstrMapper.getCommentsForPoi(rplyHstrDTO.getPoiId());
        
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,getrply);
    }

    //상세페이지 모달댓글
    @Override
    public Map<String, Object> getCommentsContType(String contTypeId) throws Exception {
        
        RplyHstrDTO rplyHstrDTO = new RplyHstrDTO();
        rplyHstrDTO.setContTypeId(contTypeId);

        List<Map<String,Object>> getrply = rplyHstrMapper.getCommentsContType(rplyHstrDTO.getContTypeId());
        
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,getrply);
    }

    // //locatioAPI에서 값가져와서 vo에 저장
    // @Override
    // public List<SearchLocationVO> locationByContId(SearchLocationDTO searchLocationDTO) throws Exception {

    //     List<SearchLocationVO> searchLocation = new ArrayList<SearchLocationVO>();
    //     try {
    //         searchLocation = searchService.searchLocation(searchLocationDTO);
    //     } catch (Exception e) {
    //         log.error("tmap api request error", e);
    //         throw new ApiException(ApiStatus.AP_FAIL, "장소통합검색 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
    //     }

    //     return searchLocation;

    // }
}
