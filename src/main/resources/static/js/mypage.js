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
      $('.commentList').empty();
      response.body.forEach(comment => {
        var row = `<tr class="comment_tr">
          <td style="width: 30%;">
            ${setLengthTitle(comment.TITLE)}
            <div style="width: 1%;"></div>
          </td>
          <td style="width: 55%;">
            ${setLengthText(comment.RPLY_CTT)}
            <div style="width: 1%;"></div>
          </td>
          <td style="width: 13%;">${comment.REG_DTM}</td>
        </tr>`
        $('.commentList').append(row);
      });
    },
    error: function(error){console.error('Error:',error);}
  });
}

function getBookmarks(count) { // 최신순 북마크 내역 조회
  $.ajax({
    type: "GET",
    url: `/api-docs/bookmarkList/${userInfo.userId}`,
    data: {count: count},
    success: function(response) {
      console.log(response.body);
    },
    error: function(error){console.error('Error:',error);}
  });
}

function setLengthTitle(title) {
  let maxLength = 10;

  if(title == undefined) {
    return '';
  }

  if(title.length > maxLength) {
    return title.slice(0, maxLength) + "..";
  }
  return title;
}

function setLengthText(text) {
  let maxLength = 25;

  if(text == undefined) {
    return '';
  }

  if(text.length > maxLength) {
    return text.slice(0, maxLength) + "..";
  }
  return text;
}