package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLginHisDTO {
    //로그인이력id,회원id
    @NotBlank
    private int user_lgin_his_id,user_id;

    //로그인일시,로그아웃일시
    @NotBlank
    private LocalDateTime login_dtm,lgout_dtm;

    //유저에이전트
    private String user_agent;
}
