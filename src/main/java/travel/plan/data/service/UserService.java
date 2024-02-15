package travel.plan.data.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    public Map<String, Object> checkId(Map<String, Object> map) throws Exception; // 아이디 중복 체크
    public Map<String, Object> userJoin(Map<String, Object> map) throws Exception; // 사용자 회원가입
    public Map<String, Object> loginCheck(Map<String, Object> map, HttpServletRequest request) throws Exception; // 사용자 로그인
    public Map<String, Object> getUserInfo(Map<String, Object> map) throws Exception;
}
