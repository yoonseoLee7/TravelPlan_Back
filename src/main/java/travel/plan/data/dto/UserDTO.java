package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    private int testId;
    private String testNick,testPwd;

    //회원id auto_increment
    @NotBlank
    private int userId;
    
    //회원닉네임
    @NotBlank
    private String userNick;

    //회원비밀번호
    @NotBlank
    private String userPwd;

    //등록일시
    @NotBlank
    private LocalDateTime regDtm;

    //등록회원id
    @NotBlank
    private int regrId;

    //탈퇴일시
    private LocalDateTime trmnDtm;

    //탈퇴여부
    private String trmnYN;

    //수정일시
    private LocalDateTime corcDtm;

    //수정id
    private int corrId;

}
