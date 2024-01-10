package travel.plan.data.service;

// import java.util.List;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;

// import jakarta.servlet.http.HttpServletRequest;
// import travel.plan.api.auth.dto.LoginParam;
// import travel.plan.data.dto.UserDTO;
// import travel.plan.data.mapper.UserMapper;
// import java.util.HashMap;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.api.auth.dto.LoginParam;
import travel.plan.data.dto.UserDTO;

public interface UserService {
    
    //public void register(UserDTO userDto);

    //@ResponseBody
    //@RequestMapping
    //public String getList(int testId);
    
    public Map<String, Object> findById(UserDTO testid, HttpServletRequest request) throws Exception;

    //public HashMap<String, Object> findById(HashMap<String, Object> paramMap);

    
}
