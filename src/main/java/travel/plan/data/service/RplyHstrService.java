package travel.plan.data.service;

import java.util.Map;

public interface RplyHstrService {
    

    public Map<String,Object> saveComment(Map<String,Object> map) throws Exception;

    public Map<String,Object> getComments(String contTypeId) throws Exception;

}
