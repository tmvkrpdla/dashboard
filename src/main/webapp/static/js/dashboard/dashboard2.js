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


$(function () {


    function getTotalUsageFifteenMinute() {

        const dtDttmHI = getCurrentQuarterTime();


        $.ajax({
            url: '../dashboard/api/getTotalUsageFifteenMinute',
            method: 'GET',
            data: {
                dtDttmHI: dtDttmHI
            },
            success: function (res) {
                console.log("-----> getTotalUsageFifteen time :", dtDttmHI);
                console.log("<-----", res);
                $('#totalUsageFifteenMinute').text(res)
            },
            error: function () {
                // alert("데이터 조회 실패");
            }
        });
    }
    getTotalUsageFifteenMinute();

    const ctx = document.getElementById('energyChart').getContext('2d');

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
    });


});
