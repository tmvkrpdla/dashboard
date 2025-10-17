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

    <link href="${pageContext.request.contextPath}/static/css/dashboard.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/static/js/dashboard/dashboard.js"></script>


    <title>사무실 대시보드</title>

</head>
<body>

<div id="wrapper">
    <div class="main-panel">
        <div class="content">


            <div class="line-first-container-1">
                <div class="div-background">

                    <div class="div-padding">

                        <div class="date-target-container">
                            <span id="dateTarget"></span>
                            <span class="div-title">종합 검침률 현황</span>

                        </div>

                        <div id="charts-container">
                            <div class="chart-item">
                                <canvas id="doughnut-chart5"></canvas>
                            </div>
                            <div class="chart-item">
                                <canvas id="doughnut-chart6"></canvas>
                            </div>
                            <div class="chart-item">
                                <canvas id="doughnut-chart7"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

            </div>


            <div class="line-second-container">


                <div class="flex-container" style="display: flex; gap: 30px;">

                    <div class="left-container flex-div">

                        <div class="div-background">

                            <div class="div-padding">

                                <div class="div-title-container">
                                    <span class="div-title-highlight">최근 14일</span>
                                    <span class="div-title">LP 검침률 추이</span>
                                </div>

                                <canvas id="chart-left"></canvas>

                            </div>
                        </div>
                    </div>

                    <div class="right-container flex-div">
                        <div class="div-background">

                            <div class="div-padding">

                                <div class="div-title-container">
                                    <span class="div-title-highlight">최근 14일</span>
                                    <span class="div-title">일간 검침률 추이</span>
                                </div>

                                <canvas id="chart-right"></canvas>

                            </div>
                        </div>
                    </div>

                </div>

            </div>


        </div>
    </div>
</div>
</body>
</html>
