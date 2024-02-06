package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RplyHstrDTO {
    //댓글id,등록회원id
    @NotBlank
    private int rplyId,regrId;
    
    //수정id,상위댓글id
    private int corrId,upprRplyId;

    //관광지코드번호,댓글내용,삭제여부
    @NotBlank
    private String contTypeId,rplyCtt,delYn;

    //등록일시
    @NotBlank
    private LocalDateTime regDtm;
    
    //수정일시
    private LocalDateTime corcDtm;
}
