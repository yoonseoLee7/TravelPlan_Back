package travel.plan.data.service;

import java.util.Map;

import travel.plan.data.dto.RplyHstrDTO;

public interface RplyHstrService {
    

    public Map<String,Object> saveComment(RplyHstrDTO rplyHstrDTO);

    public Map<String, Object> getCommentsForPoi(String poiId);

}
