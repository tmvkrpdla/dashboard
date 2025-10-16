$(function () {

    // 오늘 날짜에서 하루 빼기
    const today = new Date();
    today.setDate(today.getDate() - 1);

    // 요일 이름 배열
    const weekdays = ['일', '월', '화', '수', '목', '금', '토'];

    // 포맷팅 (YYYY-MM-DD)
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const weekday = weekdays[today.getDay()];

    // 최종 문자열
    const formattedDate = `${year}-${month}-${day} (${weekday})`;

    $('#dateTarget').text(formattedDate);

    // 차트
    /*** 차트 데이터 ***/
    const chartData = [
        {id: 'doughnut-chart5', value: 95.2, color: '#FFD93D', label: 'LP 검침률'},
        {id: 'doughnut-chart6', value: 96.1, color: '#00BFFF', label: '일간 검침률'},
        {id: 'doughnut-chart7', value: 97.7, color: '#FF3D71', label: '정기 검침률'}
    ];

    /*** Chart.js 기본 설정 ***/
    Chart.defaults.color = '#fff';
    // Chart.defaults.font.family = 'Arial';
    Chart.defaults.plugins.legend.display = false;
    Chart.defaults.plugins.tooltip.enabled = false;

    /*** 각 도넛 차트 생성 ***/
    chartData.forEach(chartInfo => {
        const ctx = document.getElementById(chartInfo.id).getContext('2d');

        new Chart(ctx, {
            type: 'doughnut',
            data: {
                datasets: [{
                    data: [chartInfo.value, 100 - chartInfo.value],
                    backgroundColor: [chartInfo.color, '#2f2f2f'],
                    borderWidth: 0
                }]
            },
            options: {
                cutout: '75%', // 차트 안쪽 여백
                rotation: -90,
                circumference: 360,
                responsive: true,
                layout: {
                    padding: {
                        bottom: 50 // ✅ 하단 여백 확보 (라벨 겹침 방지)
                    }
                },
                plugins: {
                    legend: {display: false},
                    tooltip: {enabled: false}
                }
            },
            plugins: [{
                id: 'centerText',
                afterDraw(chart) {
                    const {width, height, ctx} = chart;

                    // ✅ 중앙 % 표시
                    ctx.save();
                    ctx.font = `600 75pt Arial`; // 고정 폰트 크기
                    ctx.fillStyle = '#fff';
                    ctx.textAlign = 'center';
                    ctx.textBaseline = 'middle';
                    ctx.fillText(chartInfo.value.toFixed(1), width / 2, height / 2 - 10);

                    ctx.font = `26.25pt Arial`;
                    // ctx.fillText('%', width / 2, height / 2 + 25);
                    ctx.fillText('%', width / 2, height / 2 + 60);
                    ctx.restore();

                    // ✅ 하단 라벨 표시 (도넛보다 아래)
                    ctx.save();
                    ctx.font = '21pt Arial';
                    ctx.fillStyle = chartInfo.color;
                    ctx.textAlign = 'center';
                    // ctx.textBaseline = 'top';
                    ctx.textBaseline = 'middle';
                    ctx.fillText(chartInfo.label, width / 2, height - 10); // 여백 확보
                    ctx.restore();
                }
            }]
        });
    });
    // 차트

    //최근 14일
    // 샘플 데이터
    const rawData = [
        {date: '10/3', value: 93.9},
        {date: '10/4', value: 78.0},
        {date: '10/5', value: 97.3},
        {date: '10/6', value: 98.2},
        {date: '10/7', value: 98.3},
        {date: '10/8', value: 91.6},
        {date: '10/9', value: 98.1},
        {date: '10/10', value: 98.3},
        {date: '10/11', value: 98.3},
        {date: '10/12', value: 98.9},
        {date: '10/13', value: 98.9},
        {date: '10/14', value: 97.1},
        {date: '10/15', value: 98.8},
        {date: '10/16', value: 99.2} // 오늘 데이터라고 가정
    ];

    // 오늘 날짜 기준으로 최근 14일
    const recent14Data = rawData.filter(d => {
        const [month, day] = d.date.split('/').map(Number);
        const dDate = new Date(today.getFullYear(), month - 1, day);
        return dDate < today; // 오늘 데이터 제외
    }).slice(-14); // 최근 14일

    const labels = recent14Data.map(d => d.date);
    const values = recent14Data.map(d => d.value);

    // y축 min/max 동적 계산
    const minValue = Math.floor(Math.min(...values) / 2) * 2; // 2 단위 반올림
    const maxValue = Math.ceil(Math.max(...values) / 2) * 2;

    const commonOptions = {
        scales: {
            y: {
                // min: 90,
                min: minValue, // 최소값 동적
                max: 100, // 최대값 고정
                ticks: { stepSize: 2 },
                grid: {
                    borderDash: [5, 5],  // 가로 점선
                    color: '#606060',       // 점선 색상
                    drawTicks: false
                }
            },
            x: {
                grid: {
                    drawTicks: false,
                    drawOnChartArea: false // x축 가로선 제거
                }
            }
        },
        plugins: {
            legend: {display: false},
            tooltip: {enabled: true},
            datalabels: { // 차트 상단 값 표시
                anchor: 'end',
                align: 'end',
                color: '#fff',
                font: {weight: '600', size: 12}
            }
        }
    };


    new Chart(document.getElementById('chart-left'), {
        type: 'bar',
        data: {labels, datasets: [{data: values, backgroundColor: '#FFD700'}]},
        options: commonOptions,
        plugins: [ChartDataLabels]
    });

    new Chart(document.getElementById('chart-right'), {
        type: 'bar',
        data: {labels, datasets: [{data: values, backgroundColor: '#00A1FF'}]},
        options: commonOptions,
        plugins: [ChartDataLabels]
    });
    //최근 14일

    $('#nextPage').on('click', function () {
        console.log("클릭");
        window.location.href = '../dashboard/dashboard2';
    })


    // 슬라이드
  /*  const pages = [$('#wrapper > .main-panel > .content > .page').first(), $('#page2')];
    let current = 0;
    const total = pages.length;

    // 초기 위치 세팅
    pages.forEach((p, index) => {
        p.css({ left: index === 0 ? '0' : '100%' });
    });

    setInterval(function() {
        const next = (current + 1) % total;

        // 현재 페이지 왼쪽으로 슬라이드
        pages[current].animate({ left: '-100%' }, 500);

        // 다음 페이지 화면으로 들어오기
        pages[next].css('left', '100%').animate({ left: '0' }, 500);

        current = next;
    }, 2000); // 3초마다 슬라이드*/

   /* const pages = [$('#wrapper > .main-panel > .content > .page').first(), $('#page2')];
    let current = 0;

    pages.forEach((p, index) => {
        p.css({ left: index === 0 ? '0' : '100%', position: 'absolute', top: 0, width: '100%', height: '100%' });
        p.css('z-index', index === 0 ? 2 : 1); // 현재 페이지 z-index 높게
    });

    setInterval(function() {
        const next = (current + 1) % pages.length;

        // 슬라이드 애니메이션
        pages[current].css('z-index', 1).animate({ left: '-100%' }, 500); // 뒤로 보내기
        pages[next].css('z-index', 2).css('left', '100%').animate({ left: '0' }, 500); // 앞으로 가져오기

        current = next;
    }, 3000);*/
    // 슬라이드
});
