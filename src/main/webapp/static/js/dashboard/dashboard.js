const API_URL = "https://smartami.kr/api/v2";

$(function () {

    // 오늘 날짜에서 하루 빼기
    const today = new Date();
    today.setDate(today.getDate() - 1);

    const weekdays = ['일', '월', '화', '수', '목', '금', '토'];

    function formatDate() {
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        const weekday = weekdays[today.getDay()];
        return `${year}-${month}-${day} (${weekday})`;
    }

    // ✅ YYYYMMDD → MM/DD 형태 변환 함수
    function formatShortDate(yyyymmdd) {
        if (!yyyymmdd) return '';
        const str = yyyymmdd.toString();
        return `${str.slice(4, 6)}/${str.slice(6, 8)}`;
    }


    // values: 해당 차트 데이터 배열
    function getYAxisMax(values) {
        if (!values || values.length === 0) return 96; // 안전 처리
        const maxData = Math.max(...values);
        return maxData > 96 ? 100 : 96;
    }


    function getTopCardData() {
        const type = 'eachHo'

        let seqCode, typeEnum;
        if (type === 'eachHo') {
            seqCode = 12; // 개별 세대용
            typeEnum = 'APT';
        } else if (type === 'commonArea') {
            seqCode = 21; // 공용 공간용
            typeEnum = 'COMMONAPT';
        }

        $.ajax({
            type: "GET",
            url: API_URL + `/ami/metering/statistics/yesterday?busiType&activeNonMeteringSite=false&seqCodeAptDong=${seqCode}&type=${typeEnum}`,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                console.log("response : ", response);
                const lp = response[0].lpCorrectionInspectionRate || 0;
                const day = response[0].dayDetectReviHoRate || 0;
                const reg = response[0].regDetectReviHoRate || 0;


                // ✅ 차트 데이터 동적으로 구성
                const chartData = [
                    {id: 'doughnut-chart5', value: lp, color: '#FFD93D', label: 'LP 검침률'},
                    {id: 'doughnut-chart6', value: day, color: '#00BFFF', label: '일간 검침률'},
                    {id: 'doughnut-chart7', value: reg, color: '#FF3D71', label: '정기 검침률'}
                ];

                // ✅ 응답 데이터를 차트로 전달
                drawTotalMeteringChart(chartData);


            },
            error: function (error) {
                // Handle error, e.g., show an error message
                console.error("Error updating :", error);


            }
        });

    }

    // =====================
    // API 데이터 조회
    // =====================
    function getTwoWeeksData() {

        const today = new Date();
        const endDate = today.toISOString().slice(0, 10).replace(/-/g, ''); // YYYYMMDD

        const start = new Date();
        start.setDate(today.getDate() - 15);
        const startDate = start.toISOString().slice(0, 10).replace(/-/g, ''); // YYYYMMDD


        let url = `${API_URL}/ami/metering/statistics?startDate=${startDate}&endDate=${endDate}&busiType=&activeNonMeteringSite=false&page=1&pageSize=15&seqCodeAptDong=12&type=APT`;

        console.log("twoWeeks url : ", url);

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                console.log("response : ", response);


                // ✅ 날짜 오름차순 정렬
                const sorted = response.sort((a, b) => {
                    const dateA = a.date ? parseInt(a.date.replace(/-/g, '')) : 0;
                    const dateB = b.date ? parseInt(b.date.replace(/-/g, '')) : 0;
                    return dateA - dateB;
                });


                // ✅ LP / Day / 날짜 데이터 추출
                const lpData = sorted.map(d => d.lpCorrectionInspectionRate || 0);
                const dayData = sorted.map(d => d.dayDetectReviHoRate || 0);
                const dateLabels = sorted.map(d => formatShortDate(d.date));

                drawRecent14Chart(dateLabels, lpData, dayData);


            },
            error: function (error) {
                console.error("Error updating :", error);
                alert("통신 에러"); // 에러 메시지 표시
            }
        });
    }


    function drawTotalMeteringChart(chartData) {

        $('#dateTarget').text(formatDate());

        Chart.defaults.color = '#fff';
        Chart.defaults.plugins.legend.display = false;
        Chart.defaults.plugins.tooltip.enabled = false;

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
                    cutout: '75%',
                    rotation: -90,
                    circumference: 360,
                    responsive: true,
                    layout: {
                        padding: {bottom: 50}
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

                        ctx.save();
                        ctx.font = `600 75pt Arial`;
                        ctx.fillStyle = '#fff';
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';
                        ctx.fillText(chartInfo.value.toFixed(1), width / 2, height / 2 - 10);
                        ctx.font = `26pt Arial`;
                        ctx.fillText('%', width / 2, height / 2 + 60);
                        ctx.restore();

                        ctx.save();
                        ctx.font = '21pt Arial';
                        ctx.fillStyle = chartInfo.color;
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';
                        ctx.fillText(chartInfo.label, width / 2, height - 10);
                        ctx.restore();
                    }
                }]
            });
        });
    }

    // =====================
    // 바 차트 그리기
    // =====================
    function drawRecent14Chart(labels, lpValues, dayValues) {
        Chart.defaults.color = '#fff';
        Chart.defaults.plugins.legend.display = false;
        Chart.defaults.plugins.tooltip.enabled = false;

        // LP와 Day 검침률 데이터 각각의 y축 범위 계산
        // const minLP = Math.floor(Math.min(...lpValues) / 2) * 2;
        // const minDay = Math.floor(Math.min(...dayValues) / 2) * 2;
        const minLP = 90;
        const minDay = 90;


        const commonOptions = (minValue, values) => ({
            scales: {
                y: {
                    min: minValue,
                    // max: 100,
                    max: getYAxisMax(values),
                    ticks: {stepSize: 2},
                    grid: {
                        borderDash: [5, 5],
                        color: '#606060',
                        drawTicks: false
                    }
                },
                x: {
                    grid: {
                        drawTicks: false,
                        drawOnChartArea: false
                    }
                }
            },
            plugins: {
                legend: {display: false},
                tooltip: {enabled: true},
                datalabels: {
                    anchor: 'end',
                    align: 'end',
                    color: '#fff',
                    font: {weight: '600', size: 12}
                }
            }
        });

        // ✅ LP 검침률 차트
        new Chart(document.getElementById('chart-left'), {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'LP 검침률',
                    data: lpValues,
                    backgroundColor: '#FFD700'
                }]
            },
            // options: commonOptions(minLP),
            options: commonOptions(minLP, lpValues),
            plugins: [ChartDataLabels]
        });

        // ✅ 일일 검침률 차트
        new Chart(document.getElementById('chart-right'), {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: '일일 검침률',
                    data: dayValues,
                    backgroundColor: '#00A1FF'
                }]
            },
            // options: commonOptions(minDay),
            options: commonOptions(minDay, dayValues),
            plugins: [ChartDataLabels]
        });
    }


    /*  //최근 14일
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
                  ticks: {stepSize: 2},
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
      //최근 14일*/


    getTopCardData();
    getTwoWeeksData();


});
