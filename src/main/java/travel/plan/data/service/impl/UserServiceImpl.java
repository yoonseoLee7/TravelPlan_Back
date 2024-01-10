package travel.plan.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    

    @Override
    public Map<String, Object> findById(UserDTO nick, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        if (nick.getUserNick().equals("춘득") && nick.getUserPwd().equals("1234")) {
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

    }
