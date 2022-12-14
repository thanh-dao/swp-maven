<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Layout Demo</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../css/main.css" rel="stylesheet" type="text/css"/>
        <style>
            body {
                background-color: #F0F0F0;
            }
            .discount {
                position: relative;
                margin-top: 0px;
            }
            @media screen and (max-width: 1200px) {
                div.discount {
                    display: none;
                }
            }
            .discount .left, .discount .right {
                position: absolute;
                position: fixed;
                width: 190px;
                height: 300px;
                margin-bottom: 50px;
                margin-top: 100px;
                z-index: 1;
                margin-right: 5px;
                margin-left: 5px; 
            }

            .discount .right {
                right: 0;
            }

            .discount img {
                width: 300px;

            }

            .carousel-item img {
                width: 100%;
                height: 550px;
            }
            .top__seller .container {
                margin-top: 50px;
            }
            .intro{
                margin-top: 50px;              
            }
            .purpose{
                font-size: 14px;
                font-family: Arial, Helvetica, sans-serif;
            }
        </style>
    </head>
    <body>
        <!--        <div class="discount">
                    <img class="left img-fluid" src="<c:url value="${banner.url}"/>"  onclick="window.location.href = '#'">
                    <img class="right img-fluid" src="<c:url value="${banner.url}"/>" onclick="window.location.href = '#'">
                </div>-->
        <div class="container">
            <div class="br-form">
                <div id="demo" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ul class="carousel-indicators">
                        <li data-target="#demo" data-slide-to="0" class="active"></li>
                        <li data-target="#demo" data-slide-to="1"></li>
                        <li data-target="#demo" data-slide-to="2"></li>
                        <li data-target="#demo" data-slide-to="3"></li>
                        <li data-target="#demo" data-slide-to="4"></li>
                    </ul>
                    <!-- The slideshow -->
                    <div class="carousel-inner">
                        <div class="carousel-item  active">
                            <img class="img-fluid" src="<c:url value="/images/sach.jpg"/>" onclick="window.location.href = '<c:url value="/home/productList.do?cateId=8"/>'">
                        </div>
                        <div class="carousel-item">
                            <img class="img-fluid" src="<c:url value="/images/nuocHoa.jpg"/>" onclick="window.location.href = '<c:url value="/home/productList.do?cateId=5"/>'">
                        </div>
                        <div class="carousel-item">
                            <img class="img-fluid" src="<c:url value="/images/doGiaDung.jpg"/>" onclick="window.location.href = '<c:url value="/home/productList.do?cateId=3"/>'">
                        </div>
                        <div class="carousel-item">
                            <img class="img-fluid" src="<c:url value="/images/thucAn.jpg"/>" onclick="window.location.href = '<c:url value="/home/productList.do?cateId=1"/>'">
                        </div>
                        <div class="carousel-item">
                            <img class="img-fluid" src="<c:url value="/images/thuCung.jpg"/>"  onclick="window.location.href = '<c:url value="/home/productList.do?cateId=6"/>'">
                        </div>
                    </div>

                    <!-- Left and right controls -->
                    <a class="carousel-control-prev" href="#demo" data-slide="prev">
                        <span class="carousel-control-prev-icon"></span>
                    </a>
                    <a class="carousel-control-next" href="#demo" data-slide="next">
                        <span class="carousel-control-next-icon"></span>
                    </a>
                </div>
            </div>
            <c:if test="${!empty newProducts}">
                <div class="br-form" style="padding-bottom: 5px;">
                    <div class="d-flex justify-content-between">
                        <h3>S???N PH???M B??N CH???Y</h3>
                        <a href="<c:url value="/home/top.do?top=soldCount"/>">Xem th??m</a>
                    </div>
                    <div class="product__content row" style="margin: 0 -5px;">
                        <c:forEach items="${bestSellers}" var="i">
                            <div class=" col-lg-2 col-md-3 col-sm-4 col-4" 
                                 style="padding: 5px; position: static;">
                                <div class="product__item">
                                    <a href="<c:url value="/home/productDetail.do?productId=${i.productId}"/>">
                                        <img class="img-fluid" src="${i.getMainImage().url}" alt="">
                                        <p class="tooltip-text hinden-text">${i.name}
                                            <span>${i.name}</span>
                                        </p>
                                        <fmt:setLocale value="vi_VN"/>
                                        <div style="display: flex; justify-content: flex-end; padding-top: 5px;">
                                            <fmt:formatNumber value="${i.price}" type="currency"/>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            <c:if test="${!empty newProducts}">
                <div class="br-form" style="padding-bottom: 5px;">
                    <div style="display: flex; justify-content: space-between;">
                        <h3>S???N PH???M M???I NH???T</h3>
                        <a href="<c:url value="/home/top.do?top=newest"/>">Xem th??m</a>
                    </div>
                    <div class="product__content row" style="margin: 0 -5px;">
                        <c:forEach items="${newProducts}" var="i">
                            <div class=" col-lg-2 col-md-3 col-sm-4 col-4" 
                                 style="padding: 5px; position: static;">
                                <div class="product__item">
                                    <a href="<c:url value="/home/productDetail.do?productId=${i.productId}"/>">
                                        <img class="img-fluid" src="${i.getMainImage().url}" alt="">
                                        <p class="tooltip-text hinden-text">${i.name}
                                            <span>${i.name}</span>
                                        </p>
                                        <fmt:setLocale value="vi_VN"/>
                                        <div style="display: flex; justify-content: flex-end; padding-top: 5px;">
                                            <fmt:formatNumber value="${i.price}" type="currency"/>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
        <div class="purpose">
            <div class="container intro">
                <h4  style="font-family: Arial, Helvetica, sans-serif;">T???M NH??N, S??? M???NH V?? GI?? TR??? C???T L???I</h4>
                <h5>T???m nh??n</h5>
                <p>FEP ?????nh h?????ng tr??? th??nh n???n t???ng giao d???ch mua s???m tr???c tuy???n h??ng ?????u t???i ?????i H???c FPT, cung c???p tr???i nghi???m tuy???t v???i<br>tr??n website ????? th???a m??n m???i nhu c???u c???a kh??ch h??ng.</p>
                <h5>S??? m???nh</h5>
                <p>Mang l???i ni???m vui mua s???m, h??? tr??? vi???c mua b??n tr??? n??n d??? d??ng h??n.</p>
                <h5>Gi?? tr??? c???t l??i</h5>
                <div id="demo" class="collapse">
                    <q>T??N - T??M - TR?? - NH??N</q>
                    <p>Ch??ng t??i ?????t ch??? T??N l??n h??ng ?????u, l???y ch??? T??N ????? ?????t ra quy t???c chung cho s??? h???p t??c gi???a ch??ng t??i v?? kh??ch h??ng.<br>FEP lu??n ?????t ra nh???ng chu???n m???c cao ????i h???i ph???i c???n chu???n b??? ?????y ????? n??ng l???c
                        th???c thi, n??? l???c h???t m??nh ????? ?????m b???o ????ng<br>v?? cao h??n c??c cam k???t v???i kh??ch h??ng, ?????c bi???t l?? cam k???t v??? ch???t l?????ng s???n ph???m - d???ch v???.<br>
                        <br>Ch??ng t??i coi tr???ng kh??ch h??ng v?? lu??n l???y kh??ch h??ng l??m trung t??m, ?????t l???i ??ch v?? mong mu???n c???a kh??ch h??ng l??n h??ng ?????u.<br>N??? l???c mang ?????n cho kh??ch h??ng nh???ng tr???i nghi???m v??? s???n ph???m ??? d???ch v??? ho??n h???o nh???t.<br>S??? h??i l??ng c???a kh??ch h??ng l?? th?????c ??o th??nh c??ng c???a ch??ng t??i.
                        <br><br>FEP coi s??ng t???o l?? s???c s???ng, l?? ????n b???y ph??t tri???n, nh???m t???o ra gi?? tr??? kh??c bi???t v?? b???n s???c ri??ng trong m???i g??i s???n ph???m ??? d???ch v???.
                        <br><br>Ch??ng t??i x??y d???ng c??c m???i quan h??? v???i kh??ch h??ng b???ng s??? thi???n ch??, t??nh th??n ??i, tinh th???n nh??n v??n.<br>X??y d???ng m??i tr?????ng l??m vi???c chuy??n nghi???p, n??ng ?????ng, s??ng t???o.<br>T???o ??i???u ki???n thu nh???p cao v?? c?? h???i ph??t tri???n c??ng b???ng cho t???t c??? c??n b??? nh??n vi??n.
                    </p>
                    <p>Ch??n th??nh c???m ??n s??? ???ng h??? v?? ch??c c??c b???n c?? tr???i nghi???m mua b??n tuy???t v???i t???i FEP!</p>
                </div>
                <a onclick="showMore(this)" style="color: #FFA500;" data-toggle="collapse" data-target="#demo">Xem th??m <i class="fa-solid fa-angle-down"></i></a>
            </div>
        </div>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    </body>
    <script>
                    function showMore(el) {
                        el.innerHTML = el.innerHTML == 'Xem th??m <i class="fa-solid fa-angle-down"></i>' ? 'Thu g???n <i class="fa-solid fa-angle-up"></i>' : 'Xem th??m <i class="fa-solid fa-angle-down"></i>';
                    }
    </script>
</html>