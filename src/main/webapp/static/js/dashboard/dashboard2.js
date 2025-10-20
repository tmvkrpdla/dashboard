// ✅ 현재 URL에서 contextPath 자동 추출 (예: /dashboard)
function getContextPath() {
    const path = window.location.pathname; // 예: /dashboard/index, /, /dashboard/
    console.log("path : ", path);
    const firstLevel = path.split('/')[1]; // dashboard 또는 빈 값
    return firstLevel ? `/${firstLevel}` : ''; // /dashboard 또는 ''
}

// 오늘 날짜 YYYYMMDD 형식 만들기
function getTodayYMD() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
}


function getCurrentQuarterTime() {
    const now = new Date();

    const yyyyMMdd = now.getFullYear().toString().padStart(4, '0')
        + (now.getMonth() + 1).toString().padStart(2, '0')
        + now.getDate().toString().padStart(2, '0');

    const hour = now.getHours();
    const minute = now.getMinutes();

    // 현재 분을 15분 단위로 내림
    const quarter = Math.floor(minute / 15) * 15;

    return yyyyMMdd
        + hour.toString().padStart(2, '0')
        + quarter.toString().padStart(2, '0'); // 예: "202510171030"
}

/**
 * 전력 사용량으로 탄소(CO2) 배출량 계산 (반올림 정수)
 * @param {number} kWh - 전력 사용량 (킬로와트시)
 * @param {string} unit - 반환 단위 ("g" 또는 "kg"), 기본 "kg"
 * @returns {number} - 탄소 배출량
 */
function getCarbonEmission(kWh, unit = "kg") {
    const co2PerKWh = 0.4425; // kg CO2 per 1 kWh
    let emission = kWh * co2PerKWh;

    if (unit === "g") {
        emission *= 1000; // kg → g
    }

    return Math.round(emission); // 소수점 없이 반올림
}


function loadHourlyUsage() {
    const today = getTodayYMD();
    // const today = '20250724';

    $.ajax({
        url: contextPath + '/dashboard/api/getHourlyUsage',
        type: 'GET',
        data: {today: today},
        success: function (data) {
            console.log('hourly Usage Data:', data);
            // dtDttmH에서 시간만 추출 (마지막 두 자리)
            const labels = data.map(row => {
                const dtStr = row.dtDttmH.toString();
                return dtStr.slice(-2); // HH
            });

            const usageData = data.map(row => row.usage);

            // 차트 그리기
            const ctx = document.getElementById('energyChart').getContext('2d');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '에너지 사용량',
                        data: usageData,
                        borderColor: '#00aaff',
                        borderWidth: 3,
                        fill: false,
                        tension: 0.4,
                        pointRadius: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } },
                    scales: {
                        x: {
                            ticks: { color: '#fff', font: { size: 13 } },
                            grid: { color: '#444' }
                        },
                        y: {
                            ticks: {
                                color: '#fff',
                                font: { size: 13 },
                                callback: value => value.toLocaleString()
                            },
                            grid: { color: '#444' }
                        }
                    },
                    elements: {
                        line: { borderJoinStyle: 'round', borderCapStyle: 'round' }
                    }
                },
                plugins: [{
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
                            const { x, y } = point;
                            if (index === 0) ctx.moveTo(x, y);
                            else ctx.lineTo(x, y);
                        });

                        ctx.stroke();
                        ctx.restore();
                    }
                }]
            });
        },
        error: function () {
            alert('데이터 가져오기 실패');
        }
    });
}


$(function () {


    function getTotalUsageFifteenMinute() {

        const dtDttmHI = getCurrentQuarterTime();


        $.ajax({
            url: contextPath + '/dashboard/api/getTotalUsageFifteenMinute',  //
            method: 'GET',
            data: {
                dtDttmHI: dtDttmHI
            },
            success: function (res) {
                console.log("-----> getTotalUsageFifteen time :", dtDttmHI);
                console.log("<-----  (실시간 에너지 사용량) : ", res);
                const co2 = getCarbonEmission(res, 'kg');
                $('#totalUsageFifteenMinute').text(res)
                $('#carbonEmission').text(co2);
            },
            error: function () {
                // alert("데이터 조회 실패");
            }
        });
    }

    function getHourTotalUsage() {

        $.ajax({
            url: contextPath + '/dashboard/api/getTotalUsageForHour',  // Controller 매핑 경로
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                console.log("<----- 최근 1시간 총 사용량 :", response);
                const co2 = getCarbonEmission(response, 'kg');

                $('#hourTotalUsage').text(response);
                $('#carbonEmissionHour').text(co2);

                // 결과를 화면에 표시 (예시)
                // $('#totalUsageValue').text(response.toLocaleString() + ' kWh');
            },
            error: function (xhr, status, error) {
                console.error("❌ 전력 사용량 조회 실패:", error);
                alert("전력 사용량을 불러오는 중 오류가 발생했습니다.");
            }
        });
    }


    /*const ctx = document.getElementById('energyChart').getContext('2d');

    const hours = [
        '00', '01', '02', '03', '04', '05', '06', '07', '08', '09',
        '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'
    ];

    // ✅ 예시 데이터 (서버에서 AJAX로 가져올 수도 있음)
    const usageData = [
        65000, 67000, 64000, 60000, 70000, 80000, 100000, 120000, 130000, 128000,
        95000, 135000, 120000, 100000, 85000, 80000, 90000, 100000, 120000, 135000,
        145000, 165000, 140000, 90000
    ];

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: hours,
            datasets: [{
                label: '에너지 사용량',
                data: usageData,
                borderColor: '#00aaff',
                borderWidth: 3,
                fill: false,
                tension: 0.4,
                pointRadius: 0,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {display: false},
            },
            scales: {
                x: {
                    ticks: {
                        color: '#fff',
                        font: {size: 13}
                    },
                    grid: {color: '#444'}
                },
                y: {
                    ticks: {
                        color: '#fff',
                        font: {size: 13},
                        callback: value => value.toLocaleString()
                    },
                    grid: {color: '#444'}
                }
            },
            elements: {
                line: {
                    borderJoinStyle: 'round',
                    borderCapStyle: 'round'
                }
            }
        },
        plugins: [{
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
    });*/

    getTotalUsageFifteenMinute();
    getHourTotalUsage();
    loadHourlyUsage();


});
