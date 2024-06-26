package travel.plan.api.search.vo;

import lombok.Data;

@Data
public class SearchDetailVO {
    // 콘텐츠 타입 Id
    private String contenttypeid;

    //관광지 id(locationVO랑 비교해서 콘텐츠 타입id 가져오기)
    private int contentid;

    // 홈페이지 주소
    private String homepage;

    // 콘텐츠명
    private String title;    

    // 대표이미지(원본)
    private String firstimage;

    // 대표이미지(썸네일)
    private String firstimage2;

    // 주소
    private String addr1;

    // 상세주소
    private String addr2;

    // 개요
    private String overview;

    private Double mapx;

    private Double mapy;
}
