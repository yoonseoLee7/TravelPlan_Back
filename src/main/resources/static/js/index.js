// 화면이 처음 보여졌을 때 실행되어야 할 기능들
window.onload = function() {
    initTmap();
    initSuggestPlace();
}

function initTmap(){
    var map = new Tmapv2.Map("map_div",  
    {
        center: new Tmapv2.LatLng(37.5110739, 127.09815059), // 지도 초기 좌표(잠실 롯데월드)
        width: "inherit", 
        height: "inherit",
        zoom: 15
    });
}

// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
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

function suggestPlace(vo) {
    let value = $(vo).attr('value');
    // {"id":"5845839","name":"경복궁 한옥마을점 주차장","noorLat":37.39052415,"noorLon":126.63841805}

    $.ajax({
        url: '/suggest',
        type: 'GET',
        data: JSON.parse(value),
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

    if(results.length === 0) return;

    var resultCount = 0;
    for(let result of results) {
        console.log(result);
        let defaultImage = "images/sample.jpg"
        let img = '<img class="place_image_box" src="' + result.firstimage + '" onclick="showDetailPage()" onerror="this.src=\'' + defaultImage + '\'"/>';
        resultCount++;
        resultDiv.append(img);

        if(resultCount >= 8) break; // 최대 갯수 8개로 제한
    }
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




// 추천방문지 리스트에서 선택 후 상세페이지 이동
function showDetailPage() {
    // TODO 나중에 넘길때 필요한 데이터들 추가
    location.href = "/detail";
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
    
    var ul = $('<div></div>');
    results?.forEach(function (result) { // cf. 옵셔널체이닝
        let json = JSON.stringify(result);
        let li = `<div onclick="suggestPlace(this);" value='${json}'>${result.name}</div>`;
        ul.append(li);
    });

    resultDiv.append(ul);
}