package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    //회원id auto_increment
    @NotBlank
    private int user_id;
    
    //회원닉네임
    @NotBlank
    private String user_nick;

    //회원비밀번호
    @NotBlank
    private String user_pwd;

    //등록일시
    @NotBlank
    private LocalDateTime reg_dtm;

    //등록회원id
    @NotBlank
    private int regr_id;

    //탈퇴일시
    private LocalDateTime trmn_dtm;

    //탈퇴여부
    private String trmn_yn;

    //수정일시
    private LocalDateTime corc_dtm;

    //수정id
    private int corr_id;

}
