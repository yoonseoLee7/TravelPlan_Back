package travel.plan.data.service;

import java.util.Map;

public interface UserService {
    public Map<String, Object> checkId(Map<String, Object> map); // 아이디 중복 체크
    public Map<String, Object> userJoin(Map<String, Object> map); // 사용자 회원가입
    public Map<String, Object> loginCheck(Map<String, Object> map); // 사용자 로그인
}
