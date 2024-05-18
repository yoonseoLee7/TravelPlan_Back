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