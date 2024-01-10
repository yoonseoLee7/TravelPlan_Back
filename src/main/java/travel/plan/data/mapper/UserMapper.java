package travel.plan.data.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import travel.plan.data.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

//@Mapper
public interface UserMapper {

    // @Select("SELECT * FROM test WHERE id = #{testid}")
    // public UserMapper findTest(@Param("test") int testValue);


    // public void registerItem(HashMap<String, Object> paramMap);

    //public List<UserDTO> selectUser() throws Exception;
    //public void insertUser(UserDTO user) throws Exception;
    //public void updateUser(UserDTO user) throws Exception;    
    //public void deleteUser(int userId) throws Exception;
}
