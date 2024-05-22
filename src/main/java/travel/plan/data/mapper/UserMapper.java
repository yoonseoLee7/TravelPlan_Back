package travel.plan.data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import jakarta.servlet.http.HttpServletRequest;
import travel.plan.data.dto.KorContDTO;
import travel.plan.data.dto.RplyHstrDTO;
import travel.plan.data.dto.UserDTO;
import travel.plan.data.dto.UserVO;

@Mapper
public interface UserMapper {

    public Map<String, Object> findById(UserDTO testid, HttpServletRequest request) throws Exception;

    public UserDTO getUserById(int testId);

    //test 전체조회
    public List<UserDTO> selectAll();

    public int checkId(Map<String, Object> map);
    public int userJoin(Map<String, Object> map);
    public UserVO loginCheck(Map<String, Object> map); // 사용자 로그인
    public UserVO getUserInfo(Map<String, Object> map);

    public int commentCount(String userId); // 사용자의 총 댓글 개수 조회
    public int bookmarkCount(String userId); // 사용자의 북마크 장소 총 개수 조회
    public List<RplyHstrDTO> commentList(String userId); // 최신순 댓글 내역 조회
    public List<KorContDTO> bookmarkList(String userId); // 최신순 북마크 내역 조회

    public int loginLog(Map<String, Object> userInfo); // 로그인 로그 등록
}
