package travel.plan.api.search.vo;

import lombok.Data;

@Data
public class SearchPuzzleVO {
    // 혼잡도 유형
    private int type;

    // 단위 면적 당 평균 혼잡도
    private double congestion;

    // 장소의 혼잡도 레벨
    private int congestionLevel;
}
