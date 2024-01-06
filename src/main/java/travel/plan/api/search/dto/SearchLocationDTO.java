package travel.plan.api.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 위치기반 관광정보 API Request 시 사용
@Data
public class SearchLocationDTO {
    
    // OS 종류(IOS, AND, WIN)
    @NotBlank
    private String MobileOS;

    // 서비스명(어플명)
    @NotBlank
    private String MobileApp;

    // GPS 경도
    @NotBlank
    private double mapX;

    // GPS 위도
    @NotBlank
    private double mapY;

    // 거리반경(단위: M)
    @NotBlank
    private int radius;
}
