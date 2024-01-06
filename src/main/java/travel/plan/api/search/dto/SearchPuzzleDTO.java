package travel.plan.api.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 실시간 장소 혼잡도 API Request 시 사용
@Data
public class SearchPuzzleDTO {
    
    // 관심장소 Id
    @NotBlank
    private int poiId;

    // 중심점 위도
    private double noorLat;

    // 중심점 경도
    private double noorLon;
}
