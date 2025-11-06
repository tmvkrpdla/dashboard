$(document).ready(function () {


    const pages = [$('#page1'), $('#page2'), $('#page4')];
    let current = 0;

    // 초기 위치 세팅
    pages.forEach((p, index) => {
        p.css({left: index === 0 ? '0' : '100%'});
    });


    setInterval(function () {
        const next = (current + 1) % pages.length;


        // 현재 페이지는 화면 밖으로 나가므로, 사용자가 덜 신경 쓰도록 1500ms로 설정했
        pages[current].animate({left: '-100%'}, 3700);

        // 다음 페이지가 화면에 등장하는 것을 사용자가 인지해야 하므로, 2000ms로 설정
        pages[next].css('left', '100%').animate({left: '0'}, 3700, function () {
            // 애니메이션 끝나면 해당 페이지 데이터 갱신
            refreshPage(next);
        });

        current = next;
    }, 7500);

    // 페이지별 데이터 갱신 함수
    function refreshPage(index) {
        switch (index) {
            case 0:
                // 페이지 1의 AJAX 함수
                getTopCardData();
                getTwoWeeksData();

                break;

            case 1:
                getTotalUsageFifteenMinute();
                getHourTotalUsage();
                loadHourlyUsage();
                break;
            case 2:
                loadUsageByRange('today');
                loadUsageByRange('week');
                // loadUsageByRange('month');
                // loadUsageByRange('year');
                loadYearlyUsageByDay();
                break;
        }
    }

});