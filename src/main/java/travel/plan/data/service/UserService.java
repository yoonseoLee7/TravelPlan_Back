package travel.plan.data.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.data.dto.UserDTO;

public interface UserService {

    public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testid) throws Exception;

}
