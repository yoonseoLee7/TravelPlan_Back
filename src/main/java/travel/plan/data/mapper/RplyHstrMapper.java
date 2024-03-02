package travel.plan.data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import travel.plan.data.dto.RplyHstrDTO;

@Mapper
public interface RplyHstrMapper {
    public RplyHstrDTO saveComment(RplyHstrDTO rplyHstrDTO);
    public int getReplyCount(int uppr);

    public List<Map<String,Object>> getCommentsForPoi(String poiId);
    public List<Map<String,Object>> getCommentsContType(String contTypeId);
    public List<Map<String,Object>> getReplyList(RplyHstrDTO rplyHstrDTO);
    
}
