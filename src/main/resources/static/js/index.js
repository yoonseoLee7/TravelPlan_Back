function initTmap(){
    var map = new Tmapv2.Map("map_div",  
    {
        center: new Tmapv2.LatLng(37.5110739, 127.09815059), // 지도 초기 좌표(잠실 롯데월드)
        width: "inherit", 
        height: "inherit",
        zoom: 15
    });
}

function initSuggestPlace() {
    $.ajax({
        url: '/suggestInit',
        type: 'GET',
        success: function(response){
            // 해당 데이터를 추천방문지에 뿌려줌
            showSuggestPlace(response);
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function showSuggestPlace(results) {
    var resultDiv = $('#c_inner_suggestion');
    resultDiv.empty();

    if(results.length === 0){
        return;
    }

    results?.forEach(function(result) {
        let img = '<img class="place_image_box" src="' + result.firstimage + '"/>';
        resultDiv.append(img);
    });
}



// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
function initSuggestPlace3() {
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
    
    var ul = $('<ul></ul>');
    results?.forEach(function(result) { // cf. 옵셔널체이닝
        var li = $('<li></li>').text(result);
        ul.append(li);
    });
    resultDiv.append(ul);
    
}

window.onload = function() {
    // 1. 지도에 잠실롯데월드 위치정보 띄우기
    // 2. 잠실롯데월드 주변 추천방문지 리스트 가져오기
    // 3. 추천 방문지 리스트 뿌려주기
    // 4. 보여주는 추천 방문지 리스트를 8개로 제한하기
    initTmap();
    initSuggestPlace();
}