package travel.plan.data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import travel.plan.data.dto.RplyHstrDTO;

@Mapper
public interface RplyHstrMapper {
    public RplyHstrDTO saveComment(RplyHstrDTO rplyHstrDTO);

    public List<RplyHstrDTO> getComments(Map<String, Object> map);
}
