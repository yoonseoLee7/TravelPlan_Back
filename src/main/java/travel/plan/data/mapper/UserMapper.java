package travel.plan.data.mapper;

import org.apache.ibatis.annotations.Mapper;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.data.dto.UserDTO;
import java.util.Map;

@Mapper
public interface UserMapper {

    public Map<String, Object> findById(UserDTO testid, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testId);

}
