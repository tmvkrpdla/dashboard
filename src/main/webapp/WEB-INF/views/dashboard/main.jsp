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
        let contextPath = "${pageContext.request.contextPath}";
    </script>

    <script>
        $(document).ready(function () {


            // const pages = [$('#page1'), $('#page2'), $('#page3'), $('#page4')];
            const pages = [$('#page1'), $('#page2'), $('#page4')];
            let current = 0;

            // 초기 위치 세팅
            pages.forEach((p, index) => {
                p.css({ left: index === 0 ? '0' : '100%' });
            });


            setInterval(function() {
                const next = (current + 1) % pages.length;

              /*  // 현재 페이지 왼쪽으로 슬라이드
                pages[current].animate({ left: '-100%' }, 500);

                // 다음 페이지 화면으로 들어오기
                pages[next].css('left', '100%').animate({ left: '0' }, 1000, function() {
                    // ✅ 애니메이션 끝나면 해당 페이지 데이터 갱신
                    refreshPage(next);
                });*/

                // ➡️ 현재 페이지 왼쪽으로 슬라이드 (속도: 500ms -> 2000ms 로 늦춤)
                // 현재 페이지는 화면 밖으로 나가므로, 사용자가 덜 신경 쓰도록 1500ms로 설정했습니다.
                pages[current].animate({ left: '-100%' }, 3700);

                // ➡️ 다음 페이지 화면으로 들어오기 (속도: 1000ms -> 2000ms 로 늦춤)
                // 다음 페이지가 화면에 등장하는 것을 사용자가 인지해야 하므로, 2000ms로 설정했습니다.
                pages[next].css('left', '100%').animate({ left: '0' }, 3700, function() {
                    // ✅ 애니메이션 끝나면 해당 페이지 데이터 갱신
                    refreshPage(next);
                });

                current = next;
            }, 7500);

            // 페이지별 데이터 갱신 함수
            function refreshPage(index) {
                switch(index) {
                    case 0:
                        // 페이지 1의 AJAX 함수
                        // loadPage1Data();
                        break;

                    case 1:
                        // loadPage2Data();
                        break;
                    case 2:
                        // loadPage4Data();
                        break;
                }
            }

        });
    </script>


</head>
<body>

    <div id="page-container">
        <!-- 첫 번째 페이지 -->
        <div class="page" id="page1" style="left:0; background:#333; color:white;">
            <%@ include file="dashboard.jsp" %>
            <%--                <iframe style="width: 100%; height: 100%;" src="${pageContext.request.contextPath}/dashboard/dashboard"></iframe>--%>
        </div>

        <!-- 두 번째 페이지 -->
        <div class="page" id="page2" style="left:100%; background:#555; color:white;">
            <%@ include file="dashboard2.jsp" %>
        </div>

        <!-- 세 번째 페이지 -->
<%--        <div class="page" id="page3" style="left:100%; background:#555; color:white;">--%>
<%--            <%@ include file="dashboard3.jsp" %>--%>
<%--        </div>--%>
        <!-- 네 번째 페이지 -->
        <div class="page" id="page4" style="left:100%; background:#555; color:white;">
            <%@ include file="dashboard4.jsp" %>
        </div>

    </div>
</body>
</html>
