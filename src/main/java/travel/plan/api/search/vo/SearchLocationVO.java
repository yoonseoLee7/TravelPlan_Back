package travel.plan.api.search.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchLocationVO {
    // 콘텐츠 Id
    @NotBlank
    private int contentid;

    // 콘텐츠 제목
    @NotBlank
    private String title;

    private Double mapx;

    private Double mapy;
}
