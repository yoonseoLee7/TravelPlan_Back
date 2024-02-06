package travel.plan.data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.data.dto.UserDTO;

@Mapper
public interface UserMapper {

    public Map<String, Object> findById(UserDTO testid, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testId);

    //test 전체조회
    public List<UserDTO> selectAll();

    public int checkId(Map<String, Object> map);
    public int userJoin(Map<String, Object> map);
}
