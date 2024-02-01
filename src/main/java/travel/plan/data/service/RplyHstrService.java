package travel.plan.data.service;

import java.util.List;

import travel.plan.data.dto.RplyHstrDTO;

public interface RplyHstrService {
    
    public void saveComment(RplyHstrDTO rplyHstrDTO);

    public List<RplyHstrDTO> getComments(String contTypeId);

}
