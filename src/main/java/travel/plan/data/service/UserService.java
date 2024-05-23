package travel.plan.data.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
  public Map<String, Object> checkId(Map<String, Object> map) throws Exception; // 아이디 중복 체크
  public Map<String, Object> userJoin(Map<String, Object> map) throws Exception; // 사용자 회원가입
  public Map<String, Object> loginCheck(Map<String, Object> map, HttpServletRequest request) throws Exception; // 사용자 로그인
  public Map<String, Object> getUserInfo(Map<String, Object> map) throws Exception;

  public Map<String, Object> commentCount(String userId) throws Exception; // 사용자의 총 댓글 개수 조회
  public Map<String, Object> bookmarkCount(String userId) throws Exception; // 사용자의 북마크 장소 총 개수 조회
  public Map<String, Object> commentList(String userId, String count) throws Exception; // 최신순 댓글 내역 조회
  public Map<String, Object> bookmarkList(String userId, String count) throws Exception; // 최신순 북마크 내역 조회

  public Map<String, Object> loginLog(Map<String, Object> userInfo); // 로그인 로그 등록
  public Map<String, Object> logoutLog(Map<String, Object> userInfo); // 로그아웃 로그 등록
}
