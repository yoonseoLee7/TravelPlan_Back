// -----------------------------------------------------------------------------------------------------
// 지도 관련

// 화면이 처음 보여졌을 때 실행되어야 할 기능들
$(window).on('load', function () {
    initTmap();
    initSuggestPlace();
    changeProfile();
    initSpotComments();
});

var map, marker, rect;

function initTmap(){
    // 잠실롯데월드의 위도, 경도
    let firstLat = 37.5110739;
    let firstLng = 127.09815059;
    
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
        url: '/api/main/congestion',
        type: 'GET',
        data: {
            id: "187961",
            name: "롯데월드 잠실점",
            noorLat: 37.5110739,
            noorLon: 127.09815059
        },
        success: function(response){createRect(firstLat, firstLng, response.body);},
        error: function(error){console.error('Error:',error);}
    });
}

function showTmap(result) {
    let value =JSON.parse($(result).attr('value'));
    let lat = value.noorLat;
    let lng = value.noorLon;
    
    var WGS84GEO = new Tmapv2.LatLng(lat, lng);
    var lonlat = Tmapv2.Projection.convertWGS84GEOToEPSG3857(WGS84GEO);
    var epsg3857 = new Tmapv2.Point(lonlat.x, lonlat.y);
	var wgs84 = Tmapv2.Projection.convertEPSG3857ToWGS84GEO(epsg3857);
    map.setCenter(wgs84); // 지도의 위치 변경
    marker.setPosition(WGS84GEO); // 마커의 위치 변경
    rect.setMap(null); // 사각형 삭제

    $.ajax({
        url: '/api/main/congestion',
        type: 'GET',
        data: value,
        success: function(response){createRect(lat, lng, response.body);},
        error: function(error){console.error('Error:',error);}
    });
}

function createRect(lat, lng, level) {
    rect = new Tmapv2.Rectangle({
        bounds: new Tmapv2.LatLngBounds(new Tmapv2.LatLng(Number(lat)+ 0.0014957,Number(lng)-0.0018867),
         new Tmapv2.LatLng(Number(lat)-0.0014957,Number(lng) +0.0018867)),// 사각형 영역 좌표
        strokeColor: "#000000",	//테두리 색상
        strokeWeight:2.5,
        strokeOpacity :1,
        fillColor: congestionLevelColor(level).color, // 사각형 내부 색상
        fillOpacity :0.5, 
        map: map
    });
}

//혼잡도별 색상, 혼잡도 표시 함수
function congestionLevelColor(congestionLevel){
    var color = ""
    
    switch(congestionLevel){
     case 1:
        color = '#9cf7bd';
        break;
     case 2:
        color ='#73b7ff';
        break;
     case 3:
        color ='#d9a8ed';
        break;
     case 4:
        color ='#ff96b4';
        break;
     }
    return {"color":color}
}

// -----------------------------------------------------------------------------------------------------
// 검색, 검색 리스트 관련

//EnterEvent
function handleKeyDown(event){
    if(event.key === "Enter"){searchList();}
}

//검색어 리스트
function searchList(){
    var searchText = $('#searchText').val();

    // cf. js에서 {searchText:searchText} 와 같이 property명칭이 동일한 경우 {searchText} 로만 써도 됨. (단축속성)
    $.ajax({
        url: '/api/main/searchList',
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
    console.log(results);
    // var ul = $('<div></div>');
    var ul = $('<ul></ul>');
    results.body?.forEach(function (result) { // cf. 옵셔널체이닝
        let json = JSON.stringify(result);
        // let li = `<div class="search_items" onclick="placeItem(this);" value='${json}'>${result.name}</div>`;
        let li = `<li class="search_items" onclick="placeItem(this);" value='${json}'>
        <img src="images/location.png"/>${result.name}</li>`;
        
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
    //해당 장소 댓글 로딩
    loadComments(result)
    //댓글 저장
    submitComment(result);
    //관광지 이름
    tourName(result);
}

//--------------------------------------댓글영역

//롯데월드 댓글 로딩
function initSpotComments() {
    $('.p_comment').text("롯데월드 잠실점");
    var id="187961";

    $.ajax({
        url:'/api/main/getComments',
        type:'GET',
        data: { poiId : id },
        success: function(response) {
            console.log("초기정보 가져오기 성공", response);
            displayinit(response);
        },
        error: function(error) {
            console.error("초기화면 정보 가져오기 실패:", error);          
        }
    });
}
//댓글 출력
function displayinit(results) {
    var ul = $('#comment_list');

    if (results.length === 0) {
        let defined = '<p>댓글이 없습니다.</p>';
        ul.append(defined);
        //return;
    }
    console.log(results);
    ul.empty();

    results.body?.forEach(function(result) {
        let json = JSON.stringify(result);
        var epochTime = result.REG_DTM;
        var date = new Date(epochTime);
        var formattedDate = date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0');
        
        let li = `<li class="search_items" value='${json}' onclick='replyClick(this)' style="${result.UPPR_RPLY_ID ? 'margin-left: 20px;' : ''}">${result.RPLY_CTT}  ${formattedDate}<input type="hidden" value="${result.RPLY_ID}"></li>`;
        ul.append(li);
    });
}
//롯데월드 댓글 저장
function initSave(){
    var userId = $('#userId').val();

    var commentContent = $('#commentContent').val();
    
    // 댓글 내용 유효성검사
    if(commentContent.trim().length === 0){
        alert("내용을 입력해주세요.");
        return;
    }
    
    // 로그인 여부 확인
    if (!userId || userId === 0) {
        alert("로그인 후 이용해주세요.");
        return;
    }
    
    // 로그인 후
    var currentTime = new Date();
    var date = currentTime.toISOString();

    var poiId = "187961";
    
    $.ajax({
        type: "POST",
        url: "/api/main/saveComment",
        data: {
            rplyCtt: commentContent,
            regDtm: date,
            poiId,
            regrId: userId
        },
        success: function (response) {
            console.log("댓글저장성공",response);
            // 저장된 댓글 포함하여 댓글 목록 새로고침
            initSpotComments();
            $('#commentContent').val('');
        },
        error: function (error) {
            console.error("댓글 저장에 실패했습니다.",error);
        }
    }); 
}

//댓글 클릭시
    function replyClick(result){
    // 상위댓글 넘버
    var rplyId = $(result).find('input[type="hidden"]').val();
    console.log("상위댓글넘버:",rplyId);

    // 대댓글 창이 이미 열려있는지 확인
    if ($(result).next('.replyForm').length === 0) {
        
        var replyFormHTML = '<div class="replyForm"><input id="replyContent"/><button class="submitReply" data-upprRplyId="' + rplyId + '">전송</button><button class="cancelReply">취소</button></div>';
        $(result).after(replyFormHTML);

        // 전송
        $(result).next('.replyForm').find('.submitReply').click(function() {
            // 대댓글 전송 로직 구현
            var upprRplyId = rplyId; // 상위댓글
            var replyContent = $(this).siblings('#replyContent').val(); //댓글내용
            // ----------------------------------------------대댓 ajax 써서 전송시키기
            $.ajax({
                type: "POST",
                url: "/api/main/saveComment",
                data: { 
                    rplyCtt: replyContent,
                    regDtm: currentTime,
                    poiId,
                    regrId: $('#userId').val(),
                    upprRplyId
                 },
                success: function (response) {
                    console.log("대댓글 저장 성공",response);
                    //displayinit(response);
                    updateComments(response);
                },
                error: function (error) {
                    console.error("대댓글 저장 오류",error);
                }
            });
            
            $(this).parent('.replyForm').remove();
        });

        // 대댓글 취소
        $(result).next('.replyForm').find('.cancelReply').click(function() {
            // 대댓글 창 삭제  
            $(this).parent('.replyForm').remove();
        });
    }    
}
//======검색 후 댓글
//관광지이름 출력
function tourName(result){
    var value = JSON.parse($(result).attr('value'));
    var name = value.name;
    $('#commentList').text(name);

}
// 검색 후 해당장소 댓글 로딩
function loadComments(result) {
    // var poiId = result.contents.poiId.split("=")[0];
    var value = JSON.parse($(result).attr('value'));
    var poiId = value.id;
    console.log("poiId=",poiId);

    $.ajax({
        type: "GET",
        url: "/api/main/getComments",
        data: { poiId },
        success: function (response) {
            console.log("댓글 로딩 성공",response);
            displayinit(response);
        },
        error: function (error) {
            console.error("댓글 로딩 오류",error);
        }
    });
}

//댓글 작성 & 저장
function submitComment(result){
    var currentTime = new Date();
    var date = currentTime.toISOString();
    var commentContent = $('#commentContent').val();
    var value = JSON.parse($(result).attr('value'));
    var poiId = value.id;
    var userId = $('#userId').val();

    $('#submitBtn').click(function() {
        if ($('#userId').val() === "") {
            alert("로그인 후 이용해주세요.");
            return;
        }
        if(commentContent === ""){
            alert("내용을 적어주세요.");
            return;
        }
    }); 

    $.ajax({
        type: "POST",
        url: "/api/main/saveComment",
        data: {
            rplyCtt: commentContent,
            regDtm: date,
            poiId,
            regrId: userId
        },
        success: function (response) {
            console.log("댓글저장성공",response);
            updateComments(response);
            $('#commentContent').val('');
        },
        error: function (error) {
            console.error("댓글 저장에 실패했습니다.",error);
        }
    }); 
}
//저장 후 목록불러오기
function updateComments(result) {
    var poiId = result.body.poiId;

    $.ajax({
        type: "GET",
        url: "/api/main/getComments",
        data: { poiId },
        success: function (response) {
            console.log("댓글 업데이트 성공", response);
            displayinit(response); // 댓글 목록을 업데이트하는 함수 호출
        },
        error: function (error) {
            console.error("댓글 업데이트 실패", error);
        }
    });
}
    
// -----------------------------------------------------------------------------------------------------
// 추천방문지 관련

// 메인화면 진입 시 첫 장소 근처에 위치한 관광명소의 추천 리스트 제공
function initSuggestPlace() {
    // 잠실롯데월드의 위도, 경도
    let firstLat = 37.5110739;
    let firstLng = 127.09815059;

    $.ajax({
        url: '/api/main/suggest',
        type: 'GET',
        data: {
            id: "187961",
            name: "롯데월드 잠실점",
            noorLat: firstLat,
            noorLon: firstLng
        },
        success: function(response){
            // 해당 데이터를 추천방문지에 뿌려줌
            showSuggestPlace(response.body);

        },
        error: function(error){console.error('Error:',error);}
    });
}

function suggestPlace(vo) {
    let value = $(vo).attr('value');
    // {"id":"5845839","name":"경복궁 한옥마을점 주차장","noorLat":37.39052415,"noorLon":126.63841805}

    $.ajax({
        url: '/api/main/suggest',
        type: 'GET',
        data: {
            id: value.id,
            name: value.name,
            noorLat: value.noorLat,
            noorLon: value.noorLon
        },
        success: function(response){
            // 해당 데이터를 추천방문지에 뿌려줌
            showSuggestPlace(response.body);

        },
        error: function(error){console.error('Error:',error);}
    });
}

var listDetail;
function showSuggestPlace(results) {
    window.listDetail = results;
    var resultDiv = $('#c_inner_suggestion');
    resultDiv.empty();

    if(results.length === 0) return;

    var resultCount = 0;
    for(let result of results) {
        let defaultImage = "images/suggest_default.png"
        // let img = '<img class="place_image_box" src="' + result.firstimage + '" onclick="showDetailPage(\'' + JSON.parse(JSON.stringify(result)) + '\')" onerror="this.src=\'' + defaultImage + '\'"/>';
        let img = '<img class="place_image_box" src="' + result.firstimage + '" onclick="showDetailPage('+ resultCount +')" onerror="this.src=\'' + defaultImage + '\'"/>';
        
        resultCount++;
        resultDiv.append(img);

        if(resultCount >= 8) break; // 최대 갯수 8개로 제한
    }
}

// 추천방문지 리스트에서 선택 후 상세페이지 이동
function showDetailPage(count) {
    location.href = "/detail?contentId=" + window.listDetail[count].contentid;
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
        url: '/api-docs/sendUserInfo',
        type: 'POST',
        data: {username:$('#join_username').val(),
            password:$('#join_password').val()},
        success: function(response){
            if(response.code == "Fail") {
                console.log(response.message);
            }
            if(response.code == "Success") {
                console.log("회원가입 성공");
                $('.modal').hide();
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
        url: '/api-docs/checkId',
        type: 'POST',
        data: {username:$('#join_username').val(),
            password:$('#join_password').val()},
        success: function(response){
            if(response.code == "Fail") {
                $('.error_message_join').text(response.message);
                $('#join_username').css('border', "2px solid #ff0000");
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

function loginCheck() {
    $.ajax({
        url: '/api-docs/loginCheck',
        type: 'GET',
        data: {username:$('#login_username').val(),
            password:$('#login_password').val()},
        success: function(response){
            if(response.code == "Fail") {
                $('.error_message_login').text(response.message);
                $('#login_username').css('border', "2px solid #ff0000");
                $('#login_password').css('border', "2px solid #ff0000");
            }
            if(response.code == "Success") {
                $('.modal').hide();
                $('#login_box').hide();
                $('.imgThumb').attr('value', $('#login_username').val());
                changeProfile();
            }
        },
        error: function(error){
            console.error('Error:',error);
        }
    });
}

function showMyPage(result) {
    console.log(result);
    if(result != '' && result != null) {
        // 로그인 한 상태인 경우
        location.href = "/myPage";
    }
}

function changeProfile() {
    let userId = $('.imgThumb').attr('value');                           //로그인됐는지확인하는거
    if(userId != undefined && userId != null && userId != '') {
        $.ajax({
            url: '/api-docs/getUserInfo',
            type: 'GET',
            data: {username: userId},
            success: function(response){
                let result = response.body;
                let img = result.userImg;
                if(img != null && img != '' && img != undefined) {
                    $('.imgThumb').attr("src", img);
                }
            },
            error: function(error){
                console.error('Error:',error);
            }
        });
    }
}