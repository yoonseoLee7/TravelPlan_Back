package travel.plan.data.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.mapper.UserMapper;
import travel.plan.data.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    @Autowired
    private final UserMapper userMapper;

    public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        if (nick.getTestNick().equals("이춘득") && nick.getTestPwd().equals("1111")) {
            result.put("RSLT_CD", "Success");
            result.put("RSLT_MSG", "Login Success");
            log.debug("Login Success ====== {}", nick);
            return result;
        }

        result.put("RSLT_CD", "Fail");
        result.put("RSLT_MSG", "Authentication Fail");
        log.error("Login Fail ====== {}", nick);

        return result;
    }

    @Override
    public UserDTO getUserById(int testId) throws Exception{
        return userMapper.getUserById(testId);
    }

    }
