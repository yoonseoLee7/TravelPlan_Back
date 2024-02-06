package travel.plan.data.service;

import java.util.List;
import java.util.Map;

import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.data.dto.RplyHstrDTO;

public interface RplyHstrService {
    
    public List<SearchLocationVO> locationByContId(SearchLocationDTO searchLocationDTO) throws Exception;

    public void saveComment(RplyHstrDTO rplyHstrDTO);

    public Map<String,Object> getComments(String contTypeId) throws Exception;

}
