package travel.plan.data.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiStatus;
import travel.plan.data.mapper.UserMapper;
import travel.plan.data.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;

    // 사용자 회원가입 시 아이디 중복체크
    @Override
    public Map<String, Object> checkId(Map<String, Object> map) {
        int result = userMapper.checkId(map);
        if(result == 0) {
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
        } else {
            // 중복 아이디가 있음
            return ApiResult.getHashMap(ApiStatus.AP_FAIL, "*이미 사용중인 닉네임 입니다");
        }
    }

    // 중복체크 통과 후 회원가입 정보 저장
    @Override
    public Map<String, Object> userJoin(Map<String, Object> map) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        map.put("reg", timestamp);

        int result = userMapper.userJoin(map);
        if(result == 1) {
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
        } else {
            return ApiResult.getHashMap(ApiStatus.AP_FAIL, "회원가입에 실패했습니다");
        }
    }

    // 로그인 시도한 정보와 동일한 사용자 정보가 있는지 확인
    @Override
    public Map<String, Object> loginCheck(Map<String, Object> map) {
        int result = userMapper.loginCheck(map);
        if(result >= 1) {
            // 해당 계정이 존재함, 로그인 성공
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
        } else {
            // 해당 계정이 없음, 로그인 실패
            return ApiResult.getHashMap(ApiStatus.AP_FAIL, "*닉네임 또는 비밀번호가 올바르지 않습니다");
        }
    }
}
