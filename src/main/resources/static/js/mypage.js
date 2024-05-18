var userInfo = JSON.parse(sessionStorage.getItem("userInfo"));

$(window).on('load', function () {
  commentCount();
  bookmarkCount();
});

function logout() {
  if (confirm("로그아웃 하시겠습니까?")) {
    location.href = "/logout";
    sessionStorage.clear();
  }
}

function commentCount() { // 사용자의 총 댓글 개수 조회
  $.ajax({
    type: "GET",
    url: `/api-docs/commentCount/${userInfo.userId}`,
    success: function(response) {
      $('.count_num.comment span').text(response.body);
      getComments(response.body);
    },
    error: function(error){console.error('Error:',error);}
  });
}

function bookmarkCount() { // 사용자의 북마크 장소 총 개수 조회
  $.ajax({
    type: "GET",
    url: `/api-docs/bookmarkCount/${userInfo.userId}`,
    success: function(response) {
      $('.count_num.bookmark span').text(response.body);
    },
    error: function(error){console.error('Error:',error);}
  });
}

function getComments(count) { // 최신순 댓글 내역 조회
  $.ajax({
    type: "GET",
    url: `/api-docs/commentList/${userInfo.userId}`,
    data: {count: count},
    success: function(response) {
      console.log(response.body);
    },
    error: function(error){console.error('Error:',error);}
  });
}