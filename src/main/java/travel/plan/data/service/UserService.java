package travel.plan.data.service;

import java.util.List;
import java.util.Map;

import travel.plan.data.dto.UserDTO;

public interface UserService {

   // public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testId) throws Exception;

    public List<UserDTO> selectAll();

    public int userJoin(Map<String, Object> map);
}
