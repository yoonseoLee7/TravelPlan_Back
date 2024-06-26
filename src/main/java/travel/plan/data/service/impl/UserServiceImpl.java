package travel.plan.data.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiStatus;
import travel.plan.data.dto.KorContDTO;
import travel.plan.data.dto.RplyHstrDTO;
import travel.plan.data.dto.UserVO;
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
  public Map<String, Object> loginCheck(Map<String, Object> map, HttpServletRequest request) throws Exception {
    UserVO dto = userMapper.loginCheck(map);
    if(dto != null) {
      // 해당 계정이 존재함, 로그인 성공
      HttpSession session = request.getSession(true); // 세션이 있으면 삭제 후 새로 생성
      session.setAttribute("userId", dto.getUserId());
      session.setAttribute("userNick", dto.getUserNick());

      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, dto);
    } else {
      // 해당 계정이 없음, 로그인 실패
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "*닉네임 또는 비밀번호가 올바르지 않습니다");
    }
  }

  // 사용자 닉네임을 통한 사용자 정보 조회
  @Override
  public Map<String, Object> getUserInfo(Map<String, Object> map) {
    UserVO dto = userMapper.getUserInfo(map);
    return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, dto);
  }

  @Override // 사용자의 총 댓글 개수 조회
  public Map<String, Object> commentCount(String userId) throws Exception {
    Integer count = userMapper.commentCount(userId);
    if(count >= 0) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, count);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "댓글 개수 조회에 실패했습니다.");
    }
  }

  @Override // 사용자의 북마크 장소 총 개수 조회
  public Map<String, Object> bookmarkCount(String userId) throws Exception {
    Integer count = userMapper.bookmarkCount(userId);
    if(count >= 0) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, count);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "댓글 개수 조회에 실패했습니다.");
    }
  }

  @Override // 최신순 댓글 내역 조회
  public Map<String, Object> commentList(String userId, String count) throws Exception {
    List<RplyHstrDTO> commentList = userMapper.commentList(userId);
    if(commentList.size() == Integer.parseInt(count)) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, commentList);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "댓글 내역 조회에 실패했습니다.");
    }
  }

  @Override // 최신순 북마크 내역 조회
  public Map<String, Object> bookmarkList(String userId, String count) throws Exception {
    List<KorContDTO> bookmarkList = userMapper.bookmarkList(userId);
    if(bookmarkList.size() == Integer.parseInt(count)) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, bookmarkList);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "북마크 내역 조회에 실패했습니다.");
    }
  }

  @Override // 로그인 로그 등록
  public Map<String, Object> loginLog(Map<String, Object> userInfo) {
    Integer result = userMapper.loginLog(userInfo);
    if(result == 1) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "로그인 로그 정보 등록에 실패했습니다.");
    }
  }

  @Override // 로그아웃 로그 등록
  public Map<String, Object> logoutLog(Map<String, Object> userInfo) {
    Integer result = userMapper.logoutLog(userInfo);
    if(result == 1) {
      return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, result);
    } else {
      return ApiResult.getHashMap(ApiStatus.AP_FAIL, "로그아웃 로그 정보 등록에 실패했습니다.");
    }
  }
}
