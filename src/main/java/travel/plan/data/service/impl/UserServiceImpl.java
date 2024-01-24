package travel.plan.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import ch.qos.logback.core.util.COWArrayList;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    }
