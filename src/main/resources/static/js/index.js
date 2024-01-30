function initTmap(){
    var map = new Tmapv2.Map("map_div",  
    {
        center: new Tmapv2.LatLng(37.566481622437934,126.98502302169841), // 지도 초기 좌표
        width: "inherit", 
        height: "inherit",
        zoom: 15
    });
    initSuggestPlace2();
}

// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
function initSuggestPlace() {
    var searchText = "잠실롯데월드";
    location.href='/suggest?searchText=' + searchText;
}

function initSuggestPlace2() {
    var searchText = "잠실롯데월드";
    fetch(`/api/main/suggest?searchText=${searchText}`)
    .then(rsp => {
        if(!rsp.ok) {
            console.error("오류 발생");
        };
        return rsp.json();
    })
    .then(data => {
        console.debug("응답 body >>> ", data);
    })
    .catch(err => {
        // 오류 처리
    })
}