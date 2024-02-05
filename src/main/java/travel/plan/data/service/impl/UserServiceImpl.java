package travel.plan.data.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiStatus;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.mapper.UserMapper;
import travel.plan.data.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;

    // public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception {
    //     Map<String, Object> result = new HashMap<String, Object>();

    //     if (nick.getTestNick().equals("이춘득") && nick.getTestPwd().equals("1111")) {
    //         result.put("RSLT_CD", "Success");
    //         result.put("RSLT_MSG", "Login Success");
    //         log.debug("Login Success ====== {}", nick);
    //         return result;
    //     }

    //     result.put("RSLT_CD", "Fail");
    //     result.put("RSLT_MSG", "Authentication Fail");
    //     log.error("Login Fail ====== {}", nick);

    //     return result;
    // }

    @Override
    public UserDTO getUserById(int testId) throws Exception {
        UserDTO userDTO = userMapper.getUserById(testId);
        return userDTO;
    }

    @Override
    public List<UserDTO> selectAll() {
        List<UserDTO> list = userMapper.selectAll();
        
        return list;
    }

    @Override
    public Map<String, Object> checkId(Map<String, Object> map) {
        int result = userMapper.checkId(map);
        if(result == 0) {
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
        } else {
            // 중복 아이디가 있음
            return ApiResult.getHashMap(ApiStatus.AP_FAIL, "이미 사용중인 닉네임 입니다");
        }
    }

    @Override
    public Map<String, Object> userJoin(Map<String, Object> map) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        map.put("reg", timestamp);

        int result = userMapper.userJoin(map);
        System.out.println("result::::::::::::::" + result);
        if(result == 1) {
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
        } else {
            return ApiResult.getHashMap(ApiStatus.AP_FAIL, "회원가입에 실패했습니다");
        }
    }
}
