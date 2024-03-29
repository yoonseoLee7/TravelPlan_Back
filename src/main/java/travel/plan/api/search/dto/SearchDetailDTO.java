package travel.plan.api.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 공통정보 API Request 시 사용
@Data
public class SearchDetailDTO {
    // OS 종류(IOS, AND, WIN)
    @NotBlank
    private String MobileOS;

    // 서비스명(어플명)
    @NotBlank
    private String MobileApp;

    // 콘텐츠 Id
    @NotBlank
    private int contentId;
}
