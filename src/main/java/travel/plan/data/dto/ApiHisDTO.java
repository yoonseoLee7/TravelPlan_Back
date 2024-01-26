package travel.plan.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiHisDTO {
    //요청시간
    @NotBlank
    private LocalDateTime rqstDtm;
    
    //응답시간
    private LocalDateTime rspsDtm;

    //응답코드
    private String rspsCd;

    //요청json
    @NotBlank
    private String rqstJson;

    //응답메세지,응답json
    private String rspsMsg,srpsJson;

    //apiurl,sk정보통합검색api,sk혼잡도api
    //한국관광공통정보api,한국관광위치기반api
    @NotBlank
    private String apiURL,searchAreaApi,searchPuzzleApi,
                searchDetailApi,searchLocationApi;

}
