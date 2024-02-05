package travel.plan.data.service;

import java.util.List;
import java.util.Map;

import travel.plan.data.dto.UserDTO;

public interface UserService {

   // public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testId) throws Exception;

    public List<UserDTO> selectAll();

    public Map<String, Object> checkId(Map<String, Object> map); // 아이디 중복 체크
    public Map<String, Object> userJoin(Map<String, Object> map); // 사용자 회원가입
}
