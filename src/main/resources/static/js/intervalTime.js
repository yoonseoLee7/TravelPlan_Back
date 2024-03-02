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

// 숫자가 한자리인 경우 앞에 0 붙여서 나타내기
function plusZero(time) {
    return (time < 10) ? "0" + time : time;
}