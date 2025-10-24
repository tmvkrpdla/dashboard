let energyChart4 = null;

/**
 * calcElectricBill - 전기 요금을 계산하는 함수 (주택용 3단계 누진제 기준)
 *
 * @param {number} kWh - 사용한 전력량(kWh)
 * @returns {number} - 계산된 전기 요금(원 단위, 부가세 포함)
 *
 * @example
 * const bill = calcElectricBill(350);
 * console.log(bill); // 약 53290원
 */
function calcElectricBill(kWh) {
    let bill = 0;
    let basic = 0;

    if (kWh <= 200) {
        basic = 910;
        bill = kWh * 93.3;
    } else if (kWh <= 400) {
        basic = 1600;
        bill = 200 * 93.3 + (kWh - 200) * 187.9;
    } else {
        basic = 2730;
        bill = 200 * 93.3 + 200 * 187.9 + (kWh - 400) * 280.6;
    }


    const subtotal = bill + basic;
    const tax = subtotal * 0.1; // 부가세 10%

    return subtotal + tax;
}


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
            // console.log(rangeType, response);

            const formatted = formatUsage(response.currentUsage);
            const totalUsageBill = calcElectricBill(response.currentUsage);

            const usageMap = {
                today: '#todayUsage',
                week: '#weekUsage',
                month: '#monthUsage',
                year: '#yearUsage'
            };

            const compareMap = {
                today: {week: 'todayVsLastWeek', month: 'todayVsLastMonth'},
                week: {week: 'weekVsLastWeek', month: 'weekVsLastMonth'},
                month: {week: 'monthVsLastMonth', month: 'monthVsLastYear'},
                year: {week: 'yearVsLastYear'}
            };

            // 사용량 텍스트 렌더링
            $(usageMap[rangeType]).text(formatted);

            // 전기요금 텍스트 렌더링 (있으면)
            const billElMap = {
                today: '#todayUsageBill',
                week: '#weekUsageBill',
                month: '#monthUsageBill',
                year: '#yearUsageBill'
            };

            if (billElMap[rangeType]) {
                // $(billElMap[rangeType]).text((totalUsageBill /1000000).toFixed(0).toLocaleString());
                $(billElMap[rangeType]).text((totalUsageBill / 1000000).toLocaleString(undefined, {maximumFractionDigits: 0}));
            }

            // 비교값 업데이트 공통 함수
            const updateCompare = (elId, percentValue) => {
                if (!elId || percentValue == null) return;
                const el = document.getElementById(elId);
                if (!el) return;
                // const percent = parseFloat(percentValue).toFixed(0);
                const percent = Number.parseFloat(percentValue).toFixed(0);
                const isUp = percent >= 0;
                el.className = isUp ? 'up' : 'down';
                el.textContent = `${Math.abs(percent)}% ${isUp ? '▲' : '▼'}`;
            };

            const compareIds = compareMap[rangeType];
            if (compareIds) {
                if (compareIds.week) updateCompare(compareIds.week, response.prevWeekPercent);
                if (compareIds.month) updateCompare(compareIds.month, response.prevMonthPercent);
            }
        },
        error: function () {
            alert('데이터 조회 실패');
        }
    });
}


function loadYearlyUsageByDay() {
    const ctx = document.getElementById('energyChart4').getContext('2d');

    $.ajax({
        url: contextPath + '/dashboard/api/getYearlyUsageByDay',
        type: 'GET',
        dataType: 'json', // JSON 형식으로 받음
        success: function (data) {
            // console.log("올해 일별 사용량:", data);

            // ✅ 서버에서 받은 데이터를 차트용 배열로 변환
            const labels = data.map(row => {
                // const y = row.dtDttm.toString().substring(0, 4);
                const m = row.dtDttm.toString().substring(4, 6);
                const d = row.dtDttm.toString().substring(6, 8);
                return `${m}-${d}`;      // x축 라벨 (월-일)
            });

            const usageData = data.map(row => row.usage); // y축 값

            if (energyChart4) {
                // ✅ 기존 차트가 있다면 데이터만 업데이트
                energyChart4.data.labels = labels;
                energyChart4.data.datasets[0].data = usageData;
                energyChart4.update();
                return;
            }

            // ✅ 최초 1번만 Chart 생성
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
                        mode: 'nearest',       // ㅋㅌ가장 가까운 포인트 기준
                        intersect: false
                    },
                    scales: {
                        x: {
                            ticks: {
                                color: '#fff',
                                font: {size: 13},
                                autoSkip: false,     // 자동 간격 유지 기능은 끕니다.
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


$(function () {


    loadUsageByRange('today');
    loadUsageByRange('week');
    loadUsageByRange('month');
    loadUsageByRange('year');
    //하단 올 해 일간 총 에너지 사용량
    loadYearlyUsageByDay();


});
