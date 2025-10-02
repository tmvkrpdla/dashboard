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

    <link href="${pageContext.request.contextPath}/main/webapp/static/css/setting.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/main/webapp/static/js/dashboard/dashboard.js"></script>

    <title>사무실 대시보드</title>

</head>
<body>

<div id="wrapper">
    <div class="main-panel">
        <div class="content">

            <div class="line-first-container">


                <div class="date-target-container">
                    <span>어제 날짜 YYYY-MM-DD (요일) </span>
                    <span>종합 검침률 현황</span>

                </div>


                <div id="charts-container">
                    <%--LP 검침률 차트--%>
                    <div style="width: 31%;" class="chart5">
                        <canvas id="doughnut-chart5"
                                style="height: 180px; width: 100%; display: unset;"></canvas>
                    </div>

                    <%--일간 검침률 차트--%>
                    <div style="width: 31%;" class="chart6">
                        <canvas id="doughnut-chart6"
                                style="height: 180px; width: 100%; display: unset;"></canvas>
                    </div>

                    <%--정기 검침률 차트--%>
                    <div style="width: 31%;" class="chart7">
                        <canvas id="doughnut-chart7"
                                style="height: 180px; width: 100%; display: unset;"></canvas>
                    </div>
                </div>
            </div>


            <div class="line-second-container">

                <div class="flex-container" style="display: flex;">

                    <div class="left-container">

                        <div class="div-title-container">
                            <span>최근 14일</span>
                            <span>LP 검침률 추이</span>
                        </div>

                    </div>


                    <div class="right-container">
                        <div class="div-title-container">
                            <span>최근 14일</span>
                            <span>일간 검침률 추이</span>
                        </div>
                    </div>
                </div>

            </div>


        </div>
    </div>
</div>
</body>
</html>
