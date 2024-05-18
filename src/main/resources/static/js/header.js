window.onload = function() {
  changeHeader();
}

// 홈페이지 타이틀 클릭 시 메인화면으로 이동
function returnMain() {
  location.href = "/";
}

// 헤더 프로필 클릭 시 마이페이지 화면으로 이동
function showMyPage(result) {
  // 로그인 한 상태인 경우
  if(result != '' && result != null) {location.href = "/myPage";}  
}

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

// 화면 이동 시 로그인 체크 후 ui 요소 변경
function changeHeader() {
  if(window.location.pathname == "/") {
    $('#search_box').css('visibility', 'visible'); // 메인화면의 경우 검색창 활성화
  } else {
    $('#search_box').css('visibility', 'hidden');
  }

  if(sessionStorage.getItem("userId") != null) { // 로그인 상태인 경우
    $('#login_box').css('visibility', 'hidden');
  } else {
    $('#login_box').css('visibility', 'visible');
  }
}