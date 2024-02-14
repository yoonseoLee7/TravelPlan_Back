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
    loadComments(result,function(){
        //댓글 저장
        submitComment(result);
    });
    
}

//--------------------------------------댓글영역
//초기화면 기본값 정보
// var defaultSpot = {
//     name: "롯데월드 잠실점",
//     poiId: "187961"
// };
$(document).ready(function(){
    //댓글쓰고 그냥 엔터쳤을때
    $('#commentContent').keypress(function(event) {
        if (event.which === 13) {
            submitComment();
        }
    });
})

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
            let li = `<li class="search_items" value='01001' type="button" id="replyButton">aaaaaaa</li>`;
            //    let li = `<li class="search_items" value='${comment.rplyId}'>${comment.rplyCtt}</li>`;
               $("#comment_list").append(li);
               $('#replyButton').click(function() {
                
                var upprRplyId = $(this).data('01001');
                var replyFormHTML = '<div class="replyForm" value="false" onclick="replyClick()"><input id="replyContent"/><button class="submitReply" data-upprRplyId="' + upprRplyId + '">전송</button id="hideBtn"><button>취소</button></div>';
               
                $(this).after(replyFormHTML);
            });
            
        }
    });
}
function displayinit(results){
    var resultDiv = $('#comment_list_box');
    resultDiv.empty();

    if(results.length === 0){
        resultDiv.html('댓글이 없습니다.');
        return;
    }
    console.log(results);
    var ul = $('#comment_list');

    results.body?.forEach(function (result){
        let li = `<li class="search_items" value='${result.rplyId}' type="button" id="replyButton">${result.rplyCtt}</li>`;
        ul.append(li);
        $('#replyButton').click(function() {
                
            var upprRplyId = $(this).data('${result.rplyId}');
            var replyFormHTML = '<div class="replyForm"><input id="replyContent"/><button class="submitReply" data-upprRplyId="' + upprRplyId + '">전송</button><button id="returnReply" value="false" onclick="replyClick()">취소</button></div>';
           
            $(this).after(replyFormHTML);
        });
    });

}
//댓글 클릭하면 대댓글창 사라지기
function replyClick(){
    let click = $('#returnReply').attr('value');
    console.log(click);
    
    if(click === "false"){
        //click = true;
        // $('.replyForm').hide();
        $('#returnReply').attr('value', "true");
        $('#returnReply').hide();
    }else{
        $('#returnReply').attr('value', "false");
        $('#returnReply').show();
    }
}
//======검색 후 댓글

// 해당장소 댓글내역 가져오기
function loadComments(result) {
    // var poiId = result.contents.poiId.split("=")[0];
    var poiId = result.id;

    $('#commentList').text(result.name);
    $.ajax({
        type: "GET",
        url: "/api/main/getComments",
        data: { poiId },
        success: function (response) {
            console.log("댓글 로딩 성공",response);
            displayComment(response);
        },
        error: function (error) {
            console.error("댓글 로딩 오류",error);
        }
    });
}
// 댓글
function displayComments(comments) {

    var commentHTML = '';
    if (comments && comments.length > 0) {
        comments.forEach(function(comment) {
            commentHTML += '<li class="search_items">';
            commentHTML += '<p>' + comment.rplyCtt + '</p>';
            commentHTML += '<button class="replyButton" data-upprRplyId="' + comment.upprRplyId + '">ㄴ</button>';
            commentHTML += '</li>';

    // 대댓글
    if(comment.replies && comment.replies.length > 0) {
        comment.replies.forEach(function(reply) {
            commentHTML += '<li class="search_items">';
            commentHTML += '<p>' + reply.rplyCtt + '</p>';
            commentHTML += '</li>';
        });
    }
    });
    } else {
        commentHTML = '<p>댓글이 없습니다.</p>';
    }
    $('#comment_list').html(commentHTML);
    
    // 대댓글 입력창
    $('.replyButton').click(function() {
        var upprRplyId = $(this).data('upprRplyId');
        var replyFormHTML = '<div class="replyForm"><input id="replyContent"/><button class="submitReply" data-upprRplyId="' + upprRplyId + '">Submit</button></div>';
        $(this).after(replyFormHTML);
    });

    // 대댓글 submit
    $(document).on('click', '.submitReply', function() {
        var upprRplyId = $(this).data('upprRplyId');
        var replyContent = $('#replyContent').val();
        
        //서버에 저장
        saveReply(upprRplyId, replyContent);
    });
}

//선택한 관광지에 댓글 작성 & 저장
function submitComment(result){
    var currentTime = new Date();
    var formattedTime = currentTime.toISOString().slice(0,19).replace('T',' ');
    var commentContent = $('#commentContent').val();
    var poiid = result.poiId;

    // if (commentContent === '') {
    //     alert('댓글 내용을 입력하세요.');
    //     return;
    // }
    
    //로그인 후 이용하세요

    $.ajax({
        type: "POST",
        url: "/api/main/saveComment",
        data: {
            rplyCtt: commentContent,
            regDtm: formattedTime,
            poiId: poiid,
            regrId: 1
        },
        success: function (response) {
            console.log("댓글저장성공",response);
            loadComments(response.body);
        },
        error: function (error) {
            console.error("댓글 저장에 실패했습니다.",error);
        }
    });
}





//poiid 가져오기
// function displayComments(result){
//     let value =JSON.parse($(result).attr('value'));
//     let lat = value.noorLat;
//     let lng = value.noorLon;
//     var name = value.name;
//     var poiid = value.id;

//     $('#commentList').text(name);

//     $.ajax({
//         url:'/api/main/savecomment',
//         type:'GET',
//         data:{
//             noorLat:lat,
//             noorLon:lng
//         },
//         success: function(response){
//             console.log("conttypeid 보내기성공",response);
//             loadComments(response);
//             submitComment(response);
//         },
//         error: function(error){
//             console.error('conttypeid 추출 error',error);
//         }
//     });
// }

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