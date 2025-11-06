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

    <jsp:include page="../common/common.jsp"/>

    <link href="${pageContext.request.contextPath}/static/css/dashboard.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/static/js/main.js?1.1"></script>


    <title>사무실 대시보드</title>

    <style>
        #page-container {
            position: relative;
            width: 100%;
            height: 100vh;
            overflow: hidden;
            background: #222;
        }

        .page {
            position: absolute;
            top: 0;
            width: 100%;
            height: 100%;
        }
    </style>

    <script>
        let contextPath = "${pageContext.request.contextPath}";
    </script>


</head>
<body>

<div id="page-container">
    <!-- 첫 번째 페이지 -->
    <div class="page" id="page1" style="left:0;">
        <%@ include file="dashboard.jsp" %>
        <%--                <iframe style="width: 100%; height: 100%;" src="${pageContext.request.contextPath}/dashboard/dashboard"></iframe>--%>
    </div>

    <!-- 두 번째 페이지 -->
    <div class="page" id="page2" style="left:100%;">
        <%@ include file="dashboard2.jsp" %>
    </div>

    <!-- 세 번째 페이지 -->
    <%--        <div class="page" id="page3" style="left:100%; background:#555; color:white;">--%>
    <%--            <%@ include file="dashboard3.jsp" %>--%>
    <%--        </div>--%>
    <!-- 네 번째 페이지 -->
    <div class="page" id="page4" style="left:100%;">
        <%@ include file="dashboard4.jsp" %>
    </div>

</div>
</body>
</html>
