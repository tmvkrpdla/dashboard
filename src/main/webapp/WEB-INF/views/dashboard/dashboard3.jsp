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

    <title>사무실 대시보드</title>

    <script>
        let contextPath = "${pageContext.request.contextPath}";
    </script>

</head>
<body>

<div id="wrapper">
    <div class="main-panel">
        <div class="content">

            <div class="button-container">
                <span id="prevPage">이전 페이지</span>
                <span id="nextPage">다음 페이지</span>
            </div>

        </div>
    </div>
</div>
</body>
</html>
