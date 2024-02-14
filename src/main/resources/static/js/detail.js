$(window).on('load', function () {
    getDetailInfo();
});

// 상세페이지 화면 전환 시 상세정보 조회 api 호출
function getDetailInfo() {
    let urlParam = new URL(location.href).searchParams;
    $.ajax({
        url: '/api/search/searchDetail',
        type: 'GET',
        data: {MobileOS: "WIN",
            MobileApp: "DEMO",
            contentId: urlParam.get('contentId')},
        success: function(response){
            console.log(response);
            // 가져온 상세정보를 알맞은 곳에 뿌려주기
            // {
            //     "contenttypeid": "14",
            //     "contentid": 130886,
            //     "homepage": "샤롯데씨어터 <a href=\"http://www.charlottetheater.co.kr/\" target=\"_blank\" title=\"새창 : 샤롯데씨어터 홈페이지로 이동\">http://www.charlottetheater.co.kr</a>",
            //     "title": "샤롯데씨어터",
            //     "firstimage": "http://tong.visitkorea.or.kr/cms/resource/57/1895057_image2_1.jpg",
            //     "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/57/1895057_image3_1.jpg",
            //     "addr1": "서울특별시 송파구 올림픽로 240 롯데월드",
            //     "addr2": "",
            //     "overview": "샤롯데씨어터는 국내 최초의 뮤지컬 전용 극장이다. 총 1,241석 규모로 롯데그룹이 한국의 뮤지컬 업계 발전을 도모하기 위해 건설비 총 450억 원을 투자하여 2004년 착공, 2006년 10월 28일 개관하였다. 세계 어느 극장과 비교해도 손색없는 샤롯데씨어터는 우아하고 고급스러운 중세 유럽 분위기의 품격 있는 내관과 외관을 자랑하고 있으며 뮤지컬 전용 극장으로서 갖춰야 할 모든 요소를 최첨단 시스템을 갖추고 있다. 오페라 공연의 경우 더 크고 생생하게 배우들의 표정과 생동감 있게 무대를 가까이서 즐길 수 있게 하기 위해서 오페라글라스도 대여해준다.",
            //     "mapx": 127.0976311526,
            //     "mapy": 37.5123821225
            // }
            $('#detail_title').text(response.title);
            $('#detail_overview').text(response.overview);
            $('#detail_addr').text(response.addr1 + response.addr2);
            $('#detail_tel').text(response.tel + response.telname);
            $('#detail_homepage').text(response.homepage);

            let divBox = $('#div_image');
            divBox.empty();

            let defaultImage = "images/suggest_default.png";
            let img = '<img class="place_image_box" src="' + response.firstimage + '" onerror="this.src=\'' + defaultImage + '\'"/>';

            divBox.append(img);

            showTmap(response.mapy, response.mapx, response.contentId, response.title);
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function showTmap(lat, lng, contentId, title) {
    let divBox = $('#map_div');
    divBox.empty();

    var map = new Tmapv2.Map("map_div",  
    {
        center: new Tmapv2.LatLng(lat, lng), // 지도 초기 좌표(잠실 롯데월드)
        width: "inherit", 
        height: "inherit",
        zoom: 15
    });

    marker = new Tmapv2.Marker({
		position: new Tmapv2.LatLng(lat, lng),
		map: map
	});

    $.ajax({
        url: '/api/main/congestion',
        type: 'GET',
        data: {
            id: contentId,
            name: title,
            noorLat: lat,
            noorLon: lng
        },
        success: function(response){createRect(lat, lng, response.body);},
        error: function(error){console.error('Error:',error);}
    });
}