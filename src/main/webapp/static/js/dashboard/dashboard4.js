function formatUsage(value) {
    if (value == null || isNaN(value)) return '-'; // 예외 처리
    return Math.round(value / 1000).toLocaleString(); // mWh 변환 + 숫자 포맷
}


function loadUsageByRange(rangeType) {
    $.ajax({
        url: contextPath + '/dashboard/api/getUsageByRange',
        type: 'GET',
        data: {rangeType: rangeType},
        success: function (response) {
            // console.log(rangeType + ' 사용량:', usage.toLocaleString() + ' kWh');
            console.log(rangeType, response);
            const formatted = formatUsage(response.currentUsage);

            switch (rangeType) {
                case 'today': {
                    $('#todayUsage').text(formatted);

                    const lastWeekEl = document.getElementById('todayVsLastWeek');
                    const lastMonthEl = document.getElementById('todayVsLastMonth');

                    // ✅ 전주 대비
                    if (response.prevWeekPercent != null) {
                        const percent = response.prevWeekPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastWeekEl.className = isUp ? 'up' : 'down';
                        lastWeekEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }

                    // ✅ 전월 대비
                    if (response.prevMonthPercent != null) {
                        const percent = response.prevMonthPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastMonthEl.className = isUp ? 'up' : 'down';
                        lastMonthEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }
                    break;
                }

                case 'week': {
                    $('#weekUsage').text(formatted);

                    const lastWeekEl = document.getElementById('weekVsLastWeek');
                    const lastMonthEl = document.getElementById('weekVsLastMonth');

                    // ✅ 전주 대비
                    if (response.prevWeekPercent != null) {
                        const percent = response.prevWeekPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastWeekEl.className = isUp ? 'up' : 'down';
                        lastWeekEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }

                    // ✅ 전월 대비
                    if (response.prevMonthPercent != null) {
                        const percent = response.prevMonthPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastMonthEl.className = isUp ? 'up' : 'down';
                        lastMonthEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }

                    break;
                }

                case 'month': {
                    $('#monthUsage').text(formatted);


                    const lastWeekEl = document.getElementById('monthVsLastMonth');
                    const lastMonthEl = document.getElementById('monthVsLastYear');

                    // ✅ 전월 대비
                    if (response.prevWeekPercent != null) {
                        const percent = response.prevWeekPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastWeekEl.className = isUp ? 'up' : 'down';
                        lastWeekEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }

                    // ✅ 전년 동기 대비
                    if (response.prevMonthPercent != null) {
                        const percent = response.prevMonthPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastMonthEl.className = isUp ? 'up' : 'down';
                        lastMonthEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }


                    break;
                }

                case 'year': {
                    $('#yearUsage').text(formatted);


                    const lastWeekEl = document.getElementById('yearVsLastYear');

                    // ✅ 전년 동기 대비

                    if (response.prevWeekPercent != null) {
                        const percent = response.prevWeekPercent.toFixed(0);
                        const isUp = percent >= 0;
                        lastWeekEl.className = isUp ? 'up' : 'down';
                        lastWeekEl.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
                    }


                    break;
                }
            }
        },
        error: function () {
            alert('데이터 조회 실패');
        }
    });
}


$(function () {

    const ctx = document.getElementById('energyChart4').getContext('2d');
    let energyChart4 = null; // 전역으로 선언하여 재사용 가능하게


    function loadYearlyUsageByDay() {
        $.ajax({
            url: contextPath + '/dashboard/api/getYearlyUsageByDay',
            type: 'GET',
            dataType: 'json', // JSON 형식으로 받음
            success: function (data) {
                console.log("올해 일별 사용량:", data);

                // ✅ 서버에서 받은 데이터를 차트용 배열로 변환
                const labels = data.map(row => {
                    // const y = row.dtDttm.toString().substring(0, 4);
                    const m = row.dtDttm.toString().substring(4, 6);
                    const d = row.dtDttm.toString().substring(6, 8);
                    return `${m}-${d}`;      // x축 라벨 (월-일)
                });

                const usageData = data.map(row => row.usage); // y축 값

                // ✅ 기존 차트가 있으면 삭제 후 다시 생성
                if (energyChart4) {
                    energyChart4.destroy();
                }

                // ✅ 차트 생성
                energyChart4 = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '하루 에너지 사용량 (kWh)',
                            data: usageData,
                            borderColor: '#00aaff',
                            borderWidth: 3,
                            pointRadius: 1, // 기본 점
                            tension: 0.4,
                            fill: false,
                            pointHoverRadius: 6,      // ✅ 마우스 올리면 점 표시
                            pointHoverBackgroundColor: '#fff',
                            pointHoverBorderColor: '#00aaff',
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {display: false},
                            tooltip: {
                                enabled: true,
                                backgroundColor: 'rgba(0, 0, 0, 0.7)',
                                titleColor: '#fff',
                                bodyColor: '#fff',
                                borderColor: '#00aaff',
                                borderWidth: 1,
                                displayColors: false
                            }
                        },
                        interaction: {
                            mode: 'nearest',       // ✅ 가장 가까운 포인트 기준
                            intersect: false
                        },
                        scales: {
                            x: {
                                ticks: {
                                    color: '#fff',
                                    font: {size: 13},
                                    autoSkip: false,     // 🚨 자동 간격 유지 기능은 끕니다.
                                    maxRotation: 0,      // 글자 기울어짐 방지
                                    callback: function (value, index, ticks) {
                                        const label = this.getLabelForValue(value);
                                        // ✅ 라벨이 'xx-01' 형식일 때만 라벨을 표시하고, 나머지는 빈 문자열을 반환합니다.
                                        if (label.endsWith('-01')) {
                                            return label;
                                        }
                                        return '';
                                    }
                                }
                            },
                            y: {
                                ticks: {
                                    color: '#fff',
                                    font: {size: 13},
                                    callback: value => value.toLocaleString()
                                },
                                grid: {color: '#444'}
                            }
                        }
                    },
                    plugins: [
                        {
                            id: 'glow',
                            afterDraw: chart => {
                                const ctx = chart.ctx;
                                const dataset = chart.data.datasets[0];
                                const meta = chart.getDatasetMeta(0);

                                ctx.save();
                                ctx.shadowColor = 'rgba(0, 170, 255, 0.8)';
                                ctx.shadowBlur = 15;
                                ctx.lineWidth = dataset.borderWidth;
                                ctx.strokeStyle = dataset.borderColor;
                                ctx.beginPath();

                                meta.data.forEach((point, index) => {
                                    const {x, y} = point;
                                    if (index === 0) ctx.moveTo(x, y);
                                    else ctx.lineTo(x, y);
                                });
                                ctx.stroke();
                                ctx.restore();
                            }
                        }]
                });
            },
            error: function (xhr, status, error) {
                console.error('데이터 조회 실패', status, error);
                alert('데이터 조회 실패');
            }
        });
    }


    loadUsageByRange('today');
    loadUsageByRange('week');
    loadUsageByRange('month');
    loadUsageByRange('year');
    /*TODO : 전주 대비, 전월 대비 해주기
    * today, week, month, year 호출 시 각각 전주, 전월을 추가로 호출해서 percent를 계산하자
    * ?? : 이게 나은 방법일까?
    * */


    //하단 올 해 일간 총 에너지 사용량
    loadYearlyUsageByDay();


});
