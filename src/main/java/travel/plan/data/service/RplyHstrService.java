package travel.plan.data.service;

import java.util.Map;

import travel.plan.data.dto.RplyHstrDTO;

public interface RplyHstrService {
    

    public Map<String,Object> saveComment(RplyHstrDTO rplyHstrDTO)throws Exception;
    public Map<String,Object> getReplyCount(int rplyId)throws Exception;

    public Map<String, Object> getCommentsForPoi(String poiId)throws Exception;
    public Map<String, Object> getCommentsContType(String contTypeId)throws Exception;
    public Map<String, Object> getReplyList(RplyHstrDTO rplyHstrDTO)throws Exception;

}
