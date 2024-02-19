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
            }
            
            let divBox = $('#div_image');
            divBox.empty();

            let defaultImage = "images/suggest_default.png";
            let img = '<img class="place_image_box" src="' + response.firstimage + '" onerror="this.src=\'' + defaultImage + '\'"/>';

            divBox.append(img);

            showTmap(response.mapy, response.mapx, response.contentId, response.title);
            //댓글 클릭한다면 로딩코멘트로 보내기
            $('#detail_comment').click(function() {
                modalLoadComments(response.contenttypeid);
            });
            //댓글입력칸 클릭하면 저장함수에 conttypeid보내기
            $('#submitBtn').click(function() {
                modalCommentSave(response.contenttypeid);
            });

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

// 받아온 홈페이지 주소로 하이퍼링크 만들기
function changeHyperLink(homepage) {
    let divBox = $('#detail_homepage');
    divBox.empty();
    let a = `<a href="` + homepage + `">` + homepage + `</a>`;
    divBox.append(a);
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
    if (!$(result.target).closest('.modal_main').length) {
        var modal = $('.modal');
        if (modal.data('visible') === 'true') {
            modal.hide();
            modal.data('visible', 'false');
        }
    }
}

//contenttypeid get
function modalLoadComments(result){
    var contTypeId = result;
    // var contTypeId = "14";
    console.log(contTypeId);
    $.ajax({
        type: "GET",
        url: "/api/main/getCommentsModal",
        data:{contTypeId},
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
    var ul = $('#commentUL');
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
        var liStyle = result.UPPR_RPLY_ID ? 'margin-left: 20px;' : '';
        
        let li = `<li class="li_search" value='${json}' onclick='modalReplyClick(this,${result.CONT_TYPE_ID})' style='${liStyle}'>${result.RPLY_CTT}  ${formattedDate}<input type="hidden" value="${result.RPLY_ID}"></li>`;
        ul.append(li);
    });
}

//댓글 클릭시
function modalReplyClick(result,contTypeId){
    var rplyId = $(result).find('input[type="hidden"]').val();

    //대댓글창 유무확인
    if ($(result).next('.replyForm').length === 0) {
        
        var replyFormHTML = '<div class="replyForm"><input id="replyContent"/><button class="submitReply" data-upprRplyId="' + rplyId + '">전송</button><button class="cancelReply">취소</button></div>';
        $(result).after(replyFormHTML);

        // 전송
        $(result).next('.replyForm').find('.submitReply').click(function() {

            var upprRplyId = rplyId;
            var replyContent = $(this).siblings('#replyContent').val(); 

            $.ajax({
                type: "POST",
                url: "/api/main/saveComment",
                data: { 
                    rplyCtt: replyContent,
                    regDtm: currentTime,
                    contTypeId,
                    regrId: $('#userId').val(),
                    upprRplyId
                 },
                success: function (response) {
                    console.log("대댓글 저장 성공",response);
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

function updateComments(result) {
    var contTypeId = result.body.contTypeId;

    $.ajax({
        type: "GET",
        url: "/api/main/getComments",
        data: { contTypeId },
        success: function (response) {
            console.log("댓글 업데이트 성공", response);
            displayComments(response); // update
        },
        error: function (error) {
            console.error("댓글 업데이트 실패", error);
        }
    });
}

//댓글 저장
function modalCommentSave(result){
    var currentTime = new Date();
    var date = currentTime.toISOString();
    var commentContent = $('.comment_input').val();
    //var value = JSON.parse($(result).attr('value'));
    var contTypeId = result;
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
            contTypeId,
            regrId: userId
         },
        success: function (response) {
            console.log("모달댓글 저장 성공",response);
            updateComments(response);
        },
        error: function (error) {
            console.error("모달댓글 저장 오류",error);
        }
    });

}

//-------------------북마크영역
function bookMarkSave(result){
    var change = result.currentTarget;
    change.classList.toggle("red");


}