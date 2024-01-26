package travel.plan.api.search.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchAreaVO {
     // 관심장소 Id
    @NotBlank
    private int id;

    // 장소명
    private String name;

    // 중심점 위도
    private double noorLat;

    // 중심점 경도
    private double noorLon;
}
