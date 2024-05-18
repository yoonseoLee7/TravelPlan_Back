$(window).on('load', function () {
  commentCount();
});

function logout() {
  if (confirm("로그아웃 하시겠습니까?")) {
    location.href = "/logout";
    sessionStorage.clear();
  }
}

function commentCount() { // 사용자의 총 댓글 개수 조회
  var userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  $.ajax({
    type: "GET",
    url: `/api-docs/commentCount/${userInfo.userId}`,
    success: function(response) {
      $('.count_num.comment span').text(response.body);
    },
    error: function(error){console.error('Error:',error);}
  });
}