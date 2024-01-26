package travel.plan.data.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KorContDTO {
    //회원id,위도값,경도값,등록회원id
    @NotBlank
    private int userId,noorLat,noorLon,regrId;

    //관광지코드번호,관심장소id,삭제여부
    @NotBlank
    private String contTypeId,poiId,delYN;

    //등록일시
    @NotBlank
    private LocalDateTime regDtm;

    //수정일시
    private LocalDateTime corcDtm;
    
    //수정회원id,혼잡도유형,혼잡도레벨,
    //관광지id,정렬순서 지정
    private int corrId,cngtType,cngtLevel,
                contId,displayOrder;
    
    //콘텐츠명,홈페이지주소,대표이미지원본,대표이미지썸네일,
    //주소,상세주소,개요
    private String title,hmpg,firstImg,scdImg,
                    addr,addrDtil,overView;
    
    //평균혼잡도
    private float cngt;

}
