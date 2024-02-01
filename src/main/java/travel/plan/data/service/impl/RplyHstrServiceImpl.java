package travel.plan.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchDetailVO;
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

    //conttypeid가져와서 댓글테이블에 저장
    @Override
    public void saveComment(RplyHstrDTO rplyHstrDTO){
        try {
            SearchDetailVO searchDetailVO = searchService.searchDetail(new SearchDetailDTO());

            rplyHstrDTO.setCont_type_id(searchDetailVO.getCont_type_id());

            rplyHstrMapper.saveComment(rplyHstrDTO);
        } catch (Exception e) {
            e.getMessage();
        }

        
    }

    @Override
    public List<RplyHstrDTO> getComments(String contTypeId){
        
        return rplyHstrMapper.getComments(contTypeId);
    }
}
