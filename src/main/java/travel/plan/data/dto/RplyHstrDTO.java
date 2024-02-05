package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RplyHstrDTO {
    //댓글id,등록회원id
    @NotBlank
    private int rply_id,regr_id;
    
    //수정id,상위댓글id
    private int corr_id,uppr_rply_id;

    //관광지코드번호,댓글내용,삭제여부
    @NotBlank
    private String cont_type_id,rply_ctt,del_yn;

    //등록일시
    @NotBlank
    private LocalDateTime reg_dtm;
    
    //수정일시
    private LocalDateTime corc_dtm;
}
