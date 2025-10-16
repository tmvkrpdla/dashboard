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
        $(document).ready(function() {
            const pages = [$('#page1'), $('#page2'), $('#page3'), $('#page4')];
            let current = 0;

            // 초기 위치 세팅
            pages.forEach((p, index) => {
                p.css({ left: index === 0 ? '0' : '100%' });
            });

            setInterval(function() {
                const next = (current + 1) % pages.length;

                // 현재 페이지 왼쪽으로 슬라이드
                pages[current].animate({ left: '-100%' }, 500);

                // 다음 페이지 화면으로 들어오기
                pages[next].css('left', '100%').animate({ left: '0' }, 500);

                current = next;
            }, 5000); // 3초마다 자동 슬라이드
        });
    </script>


</head>
<body>

<div id="wrapper">
    <div id="page-container">
        <!-- 첫 번째 페이지 -->
        <div class="page" id="page1" style="left:0; background:#333; color:white;">
            <h2>첫 번째 페이지</h2>
            <%@ include file="dashboard.jsp" %>
        </div>

        <!-- 두 번째 페이지 -->
        <div class="page" id="page2" style="left:100%; background:#555; color:white;">
            <%@ include file="dashboard2.jsp" %>
        </div>

        <!-- 세 번째 페이지 -->
        <div class="page" id="page3" style="left:100%; background:#555; color:white;">
            <%@ include file="dashboard3.jsp" %>
        </div>
        <!-- 네 번째 페이지 -->
        <div class="page" id="page4" style="left:100%; background:#555; color:white;">
            <%@ include file="dashboard4.jsp" %>
        </div>

    </div>
</div>
</body>
</html>
