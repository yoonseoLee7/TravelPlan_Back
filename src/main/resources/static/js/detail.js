$(window).on('load', function () {
    // TODO async await 바꿔야함!!!!!!!!!!!!!!
    getDetailInfo();
    changeProfile();
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

            if(response.title != "" && response.title != undefined) {
                $('#detail_title').text(response.title);
            }
            if(response.overview != "" && response.overview != undefined) {
                $('#detail_overview').text(response.overview);
            }
            if(response.addr1 != "" && response.addr1 != undefined) {
                $('#detail_addr').text(response.addr1 + response.addr2);
            }
            if(response.tel != "" && response.tel != undefined) {
                $('#detail_tel').text(response.tel + response.telname);
            }
            if(response.homepage != "" && response.homepage != undefined) {
                let homepage = response.homepage.split("\"")[1];
                changeHyperLink(homepage); // 받아온 홈페이지 주소로 하이퍼링크 만들기
                createIconHomepage(homepage);
            }
            
            let divBox = $('#div_image');
            divBox.empty();

            let defaultImage = "images/suggest_default.png";
            let img = '<img class="place_image_box2" style="width: 100%; height: 100%;" src="' + response.firstimage + '" onerror="this.src=\'' + defaultImage + '\'"/>';

            divBox.append(img);

            showTmap(response.mapy, response.mapx, response.contentId, response.title);

            updateContTypeId(response.contenttypeid);

            saveBookMark(response);
            // deleteBookMark(response);
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
        width: "100%", 
        height: "100%",
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

function showMyPage(result) {
    if(result != '' && result != null) {
        // 로그인 한 상태인 경우
        location.href = "/myPage";
    }
}

function changeProfile() {
    let userId = $('.imgThumb').attr('value');  //로그인됐는지확인하는거
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

// 받아온 홈페이지 주소로 하이퍼링크 만들기
function changeHyperLink(homepage) {
    let divBox = $('#detail_homepage');
    divBox.empty();
    let a = `<a href="` + homepage + `" target="_blank">` + homepage + `</a>`;
    divBox.append(a);
}

// 받아온 홈페이지 주소가 있을 경우 홈페이지 이동 아이콘 생성
function createIconHomepage(homepage) {
    let divBox = $('#icon_homepage_box');
    divBox.empty();
    let icon = `<a href="` + homepage + `" target="_blank">🔗</a>`;
    divBox.append(icon);
}

// 홈페이지 타이틀 클릭 시 메인화면으로 이동
function returnMain() {
    location.href = "/";
}

//---------------------------------댓글모달
function openModalComment(){
    var modal = $('.modal');
    if(modal.is(':visible') == false) {
        modal.show();
        modal.data('visible', 'true');
    }
}

function closeModalComment(result){
    var modal = $('.modal');
    if(result.target.classList.contains("modal") && modal.is(':visible') == true) {
        modal.hide();
    }
}
var changeContTypeId = "";
function updateContTypeId(id){
    changeContTypeId = id;
    modalLoadComments();
}

//contenttypeid get - result = contTypeId
function modalLoadComments(){
    // var contTypeId = "14";
    console.log(changeContTypeId);
    $.ajax({
        type: "GET",
        url: "/api/main/getCommentsModal",
        data:{contTypeId:changeContTypeId},
        success:function(response){
            console.log("모달 댓글 로딩",response);
            //display 함수 호출
            displayComments(response);
        },
        error:function(error){
            console.log("모달 댓글 로딩실패",error);
        }
    });
}

//댓글출력
function displayComments(results){
    var ul = $('#comment_list');
    ul.empty();   

    if (results.body.length === 0) {
        let defined = '<p>댓글이 없습니다.</p>';
        ul.append(defined);
        return;
    }
    console.log(results);
    
    results.body?.forEach(function(result){
        let json = JSON.stringify(result);
        var epochTime = result.REG_DTM;
        var date = new Date(epochTime);
        var formattedDate = date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0');
        var userNick = $('.imgThumb').attr("value");

        let li = `<li class="search_items comment_text" value='${json}' onclick='modalReplyClick(this,${result.CONT_TYPE_ID})'>
                    <img class="imgThumb" src="https://static.nid.naver.com/images/web/user/default.png?type=s160" value="${userNick}"/>
                    <div class="imgThumb_text">
                        <div style="font-size: 18px;">${result.RPLY_CTT}</div>
                        <div style="font-size: 14px;">${formattedDate}</div>
                    </div>
                    <input id="find" type="hidden" value="${result.RPLY_ID}">
                </li>`;
        ul.append(li);
    });
}

function displayReply(results) {
    var ul = $('#replies');
    ul.empty();

    if (results.body.length === 0) {
        let defined = '<p>댓글이 없습니다.</p>';
        ul.append(defined);
        return;
    }
    console.log(results);
    results.body?.forEach(function(result) {
        var epochTime = result.REG_DTM;
        var date = new Date(epochTime);
        var formattedDate = date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0');
        var userNick = $('.imgThumb').attr("value");
        
        let li = `<li class="search_items">
                <img class="imgThumb" src="https://static.nid.naver.com/images/web/user/default.png?type=s160" value="${userNick}">
                ${result.RPLY_CTT}  ${formattedDate}</li>`;
        ul.append(li);
        
    });
}

//댓글 클릭시
function modalReplyClick(result,contTypeId){
    var rplyId = $(result).find('input[type="hidden"]').val();
    var currentTime = new Date();
    var date = currentTime.toISOString();
    var replyLength = $(result).next('.replyForm').length;
    
    //대댓글창 유무확인
    if (replyLength === 0) {        
        //대댓글 loading
        $.ajax({
            type: "POST",
            url: "/api/main/getRepies",
            data: { contTypeId,
                    upprRplyId:rplyId        
            },
            success: function (response) {
                console.log("대댓글 modal로딩 성공",response);
                displayReply(response);
            },
            error: function (error) {
                console.error("대댓글 modal로딩 오류",error);
            }
        });

        var replyFormHTML = `<div class="replyForm"><ul id="replies"></ul>
        <input id="replyContent"/><button class="submitReply">전송</button><button class="cancelReply">취소</button></div>`;
        $('.replyForm').remove();
        $(result).after(replyFormHTML);

        // 전송
        $(result).next('.replyForm').find('.submitReply').click(function() {
            var userId = $('#comment_input_box').attr("value");
            var upprRplyId = rplyId;
            var replyContent = $(this).siblings('#replyContent').val(); 
            var delYn = "N";
            if (!userId || userId === 0) {
                alert("로그인 후 이용해주세요.");
                return;
            }
            if(replyContent === ""){
                alert("내용을 적어주세요.");
                return;
            }
            $.ajax({
                type: "POST",
                url: "/api/main/saveComment",
                data: { 
                    rplyCtt: replyContent,
                    delYn,
                    regDtm: date,
                    contTypeId,
                    upprRplyId
                 },
                success: function (response) {
                    console.log("대댓글 저장 성공",response);
                    modalLoadComments();
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

function commentEnter(event){
    if(event.key === "Enter"){modalCommentSave();}
}

//댓글 저장
function modalCommentSave() {
    var currentTime = new Date();
    var date = currentTime.toISOString();
    var commentContent = $('.comment_input').val();
    var delYn = "N";
    var userId = $('#comment_input_box').attr("value");
    
    if (!userId || userId === 0) {
        alert("로그인 후 이용해주세요.");
        return;
    }
    if(commentContent === ""){
        alert("내용을 적어주세요.");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/api/main/saveComment",
        data: { 
            rplyCtt: commentContent,
            regDtm: date,
            contTypeId:changeContTypeId,
            delYn
         },
        success: function (response) {
            console.log("모달댓글 저장 성공",response);
            modalLoadComments();
            $('.comment_input').val('');
        },
        error: function (error) {
            console.error("모달댓글 저장 오류",error);
        }
    });
}

//-------------------북마크영역
function saveBookMark(result){
    $('#bookMark').click(function() {
        var bookMarkDiv = document.getElementById("bookMark");
        var visible = bookMarkDiv.getAttribute("data-visible");
        let value = JSON.stringify(result);
        var currentTime = new Date();
        var date = currentTime.toISOString();
        var userId = $('#comment_input_box').attr("value");
        var delYN = "";
       
        if (!userId || userId === 0) {
            alert("로그인 후 이용해주세요.");
            return;
        }
        if(visible === "false"){
            delYN = "N"; 
            //red
            document.getElementById("org").style.filter = "invert(16%) sepia(89%) saturate(6054%) hue-rotate(358deg) brightness(97%) contrast(113%)";
        }
        if(visible === "true"){
            delYN = "Y";
            document.getElementById("org").style.filter = "invert(90%) sepia(13%) saturate(5708%) hue-rotate(357deg) brightness(99%) contrast(105%)";
        }
        $.ajax({
            type:"POST",
            url:"/api/main/saveBookMark",
            data:{
                contTypeId:value.contenttypeid,
                noorLat:value.mapx,
                noorLon:value.mapy,
                regDtm:date,
                regrId:userId,
                delYN,
                title:value.title
            },
            success:function(){
                if(visible === "false"){
                    bookMarkDiv.setAttribute("data-visible","true");
                    console.log("북마크 저장");
                }else{
                    bookMarkDiv.setAttribute("data-visible","false");
                    console.log("북마크 업뎃");
                }
            },
            error:function(error){
                console.log("북마크 저장 실패",error);
            }
        });
    });
}

// function deleteBookMark(result){
//     $('#bookMark').click(function() {
//         var bookMarkDiv = document.getElementById("bookMark");
//         var visible = bookMarkDiv.getAttribute("data-visible");
    
//         if(visible === "true"){
//         //삭제 
//         let value = JSON.stringify(result);
//         var currentTime = new Date();
//         var date = currentTime.toISOString();
//         var userId = $('#submitBtn').attr("value");
//         var delYN = "Y";
//         console.log(value);
    
//         $.ajax({
//             type:"POST",
//             url:"/api/main/saveBookMark",
//             data:{
//                 contTypeId:"14",
//                 noorLat:127.0976311526,
//                 noorLon:37.5123821225,
//                 regDtm:date,
//                 regrId:userId,
//                 delYN,
//                 title:"샤롯데씨어터"
//             },
//             success:function(){
//                 console.log("북마크 해제");
//                 bookMarkDiv.setAttribute("data-visible","false");
//                 document.getElementById("org").style.filter = "invert(90%) sepia(13%) saturate(5708%) hue-rotate(357deg) brightness(99%) contrast(105%)";
//             },
//             error:function(error){
//                 console.log("북마크 해제 실패",error);
//             }
    
//         });
//     }
//     });
// }