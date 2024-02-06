package travel.plan.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
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

    //locatioAPI에서 값가져와서 vo에 저장
    @Override
    public List<SearchLocationVO> locationByContId(SearchLocationDTO searchLocationDTO) throws Exception {

        List<SearchLocationVO> searchLocation = new ArrayList<SearchLocationVO>();
        try {
            searchLocation = searchService.searchLocation(searchLocationDTO);
        } catch (Exception e) {
            log.error("tmap api request error", e);
            throw new ApiException(ApiStatus.AP_FAIL, "장소통합검색 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        return searchLocation;

    }


    //conttypeid가져와서 댓글테이블에 저장
    @Override
    public void saveComment(RplyHstrDTO rplyHstrDTO){
        try {
            SearchLocationVO locationVO = new SearchLocationVO();
            SearchDetailVO searchDetailVO = searchService.searchDetail(new SearchDetailDTO());
            //contid = contid 일때 저장
            if(locationVO.getContentid() == searchDetailVO.getContentid()){

            rplyHstrDTO.setContTypeId(searchDetailVO.getContenttypeid());

            rplyHstrMapper.saveComment(rplyHstrDTO);
            }
        } catch (Exception e) {
            log.error("=============DBsave error==============",e);
        }

        
    }

    
    @Override
    public Map<String,Object> getComments(String contTypeId) throws Exception{
        
        List<RplyHstrDTO> comments = rplyHstrMapper.getComments(contTypeId);

        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,comments);
    }
}
