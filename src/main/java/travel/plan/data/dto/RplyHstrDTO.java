package travel.plan.data.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
    private String rplyCtt,delYn;

    //등록일시
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDtm;
    
    //수정일시
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime corcDtm;

    //관광지코드번호,장소통합번호
    private String contTypeId,poiId;
}
