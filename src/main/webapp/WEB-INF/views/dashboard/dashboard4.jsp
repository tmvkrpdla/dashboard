<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <jsp:include page="../common/common.jsp"/>
    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Chart.js Data Labels 플러그인 -->
    <!-- Chart.js Data Labels 2.2.0 → 4.x 호환 -->
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0/dist/chartjs-plugin-datalabels.min.js"></script>

    <link href="${pageContext.request.contextPath}/static/css/dashboard4.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/static/js/dashboard/dashboard4.js"></script>

    <title>사무실 대시보드</title>

    <script>
        let contextPath = "${pageContext.request.contextPath}";
    </script>

</head>
<body>

<div id="wrapper">
    <div class="main-panel">
        <div class="content">

            <div class="line-first-container-4">

                <div class="div-background">
                    <div class="div-padding">

                        <div class="div-title"><span class="div-title-highlight">기간 별</span> 총 에너지 사용량</div>

                        <div class="card">

                            <div class="card-set yellow">

                                <div class="usage-box">
                                    <div class="usage-label">사용량</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-value" id="todayUsage"></span>
                                        <span class="unit">MWh</span>
                                    </div>
                                </div>

                                <div class="usage-box">
                                    <div class="usage-label">사용요금</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-fee" id="todayUsageBill"></span>
                                        <span class="unit">백만원</span>
                                    </div>
                                </div>

                                <div class="compare-text">전주 대비 <span id="todayVsLastWeek"></span> · 전월 대비 <span
                                        id="todayVsLastMonth"></span>
                                </div>

                                <div class="period-label">오늘</div>

                            </div>


                            <div class="card-set blue">
                                <div class="usage-box">
                                    <div class="usage-label">사용량</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-value" id="weekUsage"></span><span class="unit">MWh</span>
                                    </div>
                                </div>
                                <div class="usage-box">
                                    <div class="usage-label">사용요금</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-fee" id="weekUsageBill"></span>
                                        <span class="unit">백만원</span>
                                    </div>
                                </div>
                                <div class="compare-text">전주 대비 <span id="weekVsLastWeek"></span> · 전월 대비 <span
                                        id="weekVsLastMonth"></span>
                                </div>
                                <div class="period-label">이번 주</div>
                            </div>


                            <div class="card-set red">
                                <div class="usage-box">
                                    <div class="usage-label">사용량</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-value" id="monthUsage"></span>
                                        <span class="unit">MWh</span></div>
                                </div>
                                <div class="usage-box">
                                    <div class="usage-label">사용요금</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-fee" id="monthUsageBill"></span>
                                        <span class="unit">백만원</span>
                                    </div>
                                </div>
                                <div class="compare-text">전월 대비 <span id="monthVsLastMonth"></span> · 전년 동기 대비 <span
                                        id="monthVsLastYear"></span>
                                </div>
                                <div class="period-label">이번 달</div>
                            </div>


                            <div class="card-set green">
                                <div class="usage-box">
                                    <div class="usage-label">사용량</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-value" id="yearUsage"></span>
                                        <span class="unit">MWh</span>
                                    </div>
                                </div>
                                <div class="usage-box">
                                    <div class="usage-label">사용요금</div>
                                    <div class="card-value-ea-container">
                                        <span class="usage-fee" id="yearUsageBill"></span>
                                        <span class="unit">백만원</span>
                                    </div>
                                </div>
                                <div class="compare-text">전년 동기 대비 <span id="yearVsLastYear"></span></div>

                                <div class="period-label">올 해</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="line-second-container">


                <div class="div-background">

                    <div class="div-padding">

                        <div class="date-target-container">
                                <span class="div-title"><span
                                        class="div-title-highlight">올 해</span>일간 총 에너지 사용량 (kWh)</span>
                        </div>

                        <div>
                            <canvas id="energyChart4" height="350"></canvas>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
