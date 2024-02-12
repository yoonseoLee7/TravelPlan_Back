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


    //mainpage 댓글테이블에 저장
    @Override
    public Map<String,Object> saveComment(RplyHstrDTO rplyHstrDTO){
        RplyHstrDTO save = rplyHstrMapper.saveComment(rplyHstrDTO);
        
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,save);  
    }

    //댓글 최신화 최대5개 정렬 가져오기
    @Override
    public Map<String,Object> getComments(RplyHstrDTO rplyHstrDTO){
        List<RplyHstrDTO> comments = rplyHstrMapper.getComments(rplyHstrDTO.getPoiId());

        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,comments);
    }
}
