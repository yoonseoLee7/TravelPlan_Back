package travel.plan.api.search.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchAreaVO {
     // 관심장소 Id
    @NotBlank
    private String id;

    // 장소명
    // 장소 검색 리스트에 장소명을 보여주기 위해 필요
    private String name;

    // 중심점 위도
    private double noorLat;

    // 중심점 경도
    private double noorLon;
}
