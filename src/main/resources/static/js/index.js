// -----------------------------------------------------------------------------------------------------
// 지도 관련

// 화면이 처음 보여졌을 때 실행되어야 할 기능들
window.onload = function() {
    // initTmap();
    //initSuggestPlace();
}

// 잠실롯데월드의 위도, 경도
let firstLat = 37.5110739;
let firstLng = 127.09815059;
var map, marker, rect;

function initTmap(){
    map = new Tmapv2.Map("map_div",  
    {
        center: new Tmapv2.LatLng(firstLat, firstLng), // 지도 초기 좌표(잠실 롯데월드)
        width: "inherit", 
        height: "inherit",
        zoom: 15
    });

    marker = new Tmapv2.Marker({
		position: new Tmapv2.LatLng(firstLat, firstLng),
		map: map
	});

    $.ajax({
        url: '/congestionInit',
        type: 'GET',
        success: function(response){
            rect = new Tmapv2.Rectangle({
                bounds: new Tmapv2.LatLngBounds(new Tmapv2.LatLng(Number(firstLat)+ 0.0014957,Number(firstLng)-0.0018867),
                 new Tmapv2.LatLng(Number(firstLat)-0.0014957,Number(firstLng) +0.0018867)),// 사각형 영역 좌표
                strokeColor: "#000000",	//테두리 색상
                strokeWeight:2.5,
                strokeOpacity :1,
                fillColor: congestionLevelColor(response.body).color, // 사각형 내부 색상
                fillOpacity :0.5, 
                map: map
            });
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

// TODO init 요소들과 합쳐서 하나의 function으로 만들 수 있는지 생각
function showTmap(result) {
    let value =JSON.parse($(result).attr('value'));
    let lat = value.noorLat;
    let lng = value.noorLon;
    // {"id":"5845839","name":"경복궁 한옥마을점 주차장","noorLat":37.39052415,"noorLon":126.63841805}
    
    var WGS84GEO = new Tmapv2.LatLng(lat, lng);
    var lonlat = Tmapv2.Projection.convertWGS84GEOToEPSG3857(WGS84GEO);
    var epsg3857 = new Tmapv2.Point(lonlat.x, lonlat.y);
	var wgs84 = Tmapv2.Projection.convertEPSG3857ToWGS84GEO(epsg3857);
    map.setCenter(wgs84); // 지도의 위치 변경
    marker.setPosition(WGS84GEO); // 마커의 위치 변경
    rect.setMap(null); // 사각형 삭제

    $.ajax({
        url: '/congestion',
        type: 'GET',
        data: value,
        success: function(response){
            rect = new Tmapv2.Rectangle({
                bounds: new Tmapv2.LatLngBounds(new Tmapv2.LatLng(Number(lat)+ 0.0014957,Number(lng)-0.0018867),
                 new Tmapv2.LatLng(Number(lat)-0.0014957,Number(lng) +0.0018867)),// 사각형 영역 좌표
                strokeColor: "#000000",	//테두리 색상
                strokeWeight:2.5,
                strokeOpacity :1,
                fillColor: congestionLevelColor(response.body).color, // 사각형 내부 색상
                fillOpacity :0.5, 
                map: map
            });
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

//혼잡도별 색상, 혼잡도 표시 함수
function congestionLevelColor(congestionLevel){
    var congest = ""
    var color = ""
    
    switch(congestionLevel){
     case 1:
         congest ="여유";
         color = '#9cf7bd';
         break;
     case 2:
         congest ="보통";
           color ='#73b7ff';
         break;
     case 3:
         congest ="혼잡";
           color ='#d9a8ed';
         break;
     case 4:
         congest ="매우 혼잡";
           color ='#ff96b4';
         break;
     }
    return {"color":color,"congest":congest}
}

// -----------------------------------------------------------------------------------------------------
// 검색, 검색 리스트 관련

//EnterEvent
function handleKeyDown(event){
    if(event.key === "Enter"){
        searchList();
    }
}

//검색어 리스트
function searchList(){
    var searchText = $('#searchText').val();

    // cf. js에서 {searchText:searchText} 와 같이 property명칭이 동일한 경우 {searchText} 로만 써도 됨. (단축속성)
    $.ajax({
        url: '/searchList',
        type: 'GET',
        data: { searchText },
        success: function(response){
            searchResults(response);
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function searchResults(results){
    var resultDiv = $('#result_list_box');
    resultDiv.empty();

    if(results.length === 0){
        resultDiv.html('검색 결과가 없습니다.');
        return;
    }
    
    var ul = $('<div></div>');
    results?.forEach(function (result) { // cf. 옵셔널체이닝
        let json = JSON.stringify(result);
        let li = `<div onclick="placeItem(this);" value='${json}'>${result.name}</div>`;
        ul.append(li);
    });

    resultDiv.append(ul);
}

// 검색 리스트 아이템 클릭 시 이벤트
function placeItem(result) {
    // 추천방문지
    suggestPlace(result);
    // 지도 혼잡도
    showTmap(result);
}

// -----------------------------------------------------------------------------------------------------
// 추천방문지 관련

// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
function initSuggestPlace() {
    $.ajax({
        url: '/suggestInit',
        type: 'GET',
        success: function(response){
            // 해당 데이터를 추천방문지에 뿌려줌
            showSuggestPlace(response.body);
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function suggestPlace(vo) {
    let value = $(vo).attr('value');
    // {"id":"5845839","name":"경복궁 한옥마을점 주차장","noorLat":37.39052415,"noorLon":126.63841805}

    $.ajax({
        url: '/suggest',
        type: 'GET',
        data: JSON.parse(value),
        success: function(response){
            // 해당 데이터를 추천방문지에 뿌려줌
            showSuggestPlace(response.body);
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function showSuggestPlace(results) {
    var resultDiv = $('#c_inner_suggestion');
    resultDiv.empty();

    if(results.length === 0) return;

    var resultCount = 0;
    for(let result of results) {
        let defaultImage = "images/sample.jpg"
        let img = '<img class="place_image_box" src="' + result.firstimage + '" onclick="showDetailPage()" onerror="this.src=\'' + defaultImage + '\'"/>';
        resultCount++;
        resultDiv.append(img);

        if(resultCount >= 8) break; // 최대 갯수 8개로 제한
    }
}

// 추천방문지 리스트에서 선택 후 상세페이지 이동
function showDetailPage() {
    // TODO 나중에 넘길때 필요한 데이터들 추가
    location.href = "/detail";
}

// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
// function initSuggestPlace2() {
//     var searchText = "잠실롯데월드";
//     fetch(`/api/main/suggest?searchText=${searchText}`)
//     .then(rsp => {
//         if(!rsp.ok) {
//             console.error("오류 발생");
//         };
//         return rsp.json();
//     })
//     .then(data => {
//         console.debug("응답 body >>> ", data);
//     })
//     .catch(err => {
//         // 오류 처리
//     })
// }

// -----------------------------------------------------------------------------------------------------
// 실시간 시간 표시

// 1초에 한번씩 실행
setInterval(function() {
    var date = new Date();

    var year = date.getFullYear();
    var month = plusZero(date.getMonth() + 1);
    var day = plusZero(date.getDate());
 
    var hours = plusZero(date.getHours());
    var minutes = plusZero(date.getMinutes());
    var seconds = plusZero(date.getSeconds());

    var resultString = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

    $('.p_title_current_time').text(resultString);
}, 1000);

function plusZero(time) {
    if (time < 10) {
        return time = "0" + time;
    } else {
        return time;
    }
}

// -----------------------------------------------------------------------------------------------------
// 로그인|회원가입 모달창

function showModal() {
    var modal = $('.modal');
    if(modal.is(':visible') == false) {
        modal.show();

        $('#tab_join').attr("class", "tab_unselected");
        $('#tab_login').attr("class", "tab_selected");

        $('#join').hide();
        $('#login').show();
    }
}

function closeModal(result) {
    var modal = $('.modal');
    if(result.target.classList.contains("modal") && modal.is(':visible') == true) {
        modal.hide();
    }
}

// 탭을 선택할 시 선택한 탭으로 모달이 보이도록 변경
function checkTabStatus(result) {
    var tab = $(result).attr("id");
    var classType = $(result).attr("class");

    // tab_login tab_join
    // tab_selected tab_unselected
    // 탭이 선택되어 있지 않은 경우
    if(tab == "tab_join" && classType == "tab_unselected") {
        $('#tab_join').attr("class", "tab_selected");
        $('#tab_login').attr("class", "tab_unselected");

        $('#join').show();
        $('#login').hide();
    } else if(tab == "tab_login" && classType == "tab_unselected") {
        $('#tab_join').attr("class", "tab_unselected");
        $('#tab_login').attr("class", "tab_selected");

        $('#join').hide();
        $('#login').show();
    }
}

function sendUserInfo() {
    $.ajax({
        url: '/api/main/sendUserInfo',
        type: 'POST',
        data: {username:$('#join_username').val(),
            password:$('#join_password').val()},
        success: function(response){
            if(response.code == "Fail") {
                console.log(response.message);
            }
            if(response.code == "Success") {
                console.log("회원가입 성공");
            }
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

// 아이디 중복 여부 체크
function checkId() {
    $.ajax({
        url: '/api/main/checkId',
        type: 'POST',
        data: {username:$('#join_username').val(),
            password:$('#join_password').val()},
        success: function(response){
            if(response.code == "Fail") {
                // TODO 중복 닉네임 메시지 띄우기
                console.log(response.message);
            }
            if(response.code == "Success") {
                sendUserInfo();
            }
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}