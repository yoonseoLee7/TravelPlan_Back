package travel.plan.api.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 위치기반 관광정보 API Request 시 사용
@Data
public class SearchLocationDTO {
    
    // OS 종류(IOS, AND, WIN)
    @NotBlank
    String MobileOS;

    // 서비스명(어플명)
    @NotBlank
    String MobileApp;

    // GPS 경도
    @NotBlank
    double mapX;

    // GPS 위도
    @NotBlank
    double mapY;

    // 거리반경(단위: M)
    @NotBlank
    int radius;
}
