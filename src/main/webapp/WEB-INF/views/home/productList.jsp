<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
              crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css"
              integrity="sha512-xh6O/CkQoPOWDdYTDqeRdPCVd1SpvCA9XXcUnZS2FmJNp1coAFzvtCN9BmamE+4aHK8yyUHUSCcJHgXloTyT2A=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="../css/main.css" rel="stylesheet" type="text/css"/>
        <style>
            /*            .product .container {
                            margin-top: 50px;
                        }
            
                        .product__item {
                            width: 100%;
                            transition: 0.5s;
                        }
            
                        .product__item a {
                            color: black;
                            font-size: 18px;
                        }
            
                        .product__item img {
                            transition: 0.5s;
                        }
            
                        .product__item img:hover {
                            transform: translateY(-5px);
                            transition: 0.5s;
                        }
            
                        .product__item a:hover {
                            color: blue;
                            text-decoration: none;
                        }
            
                        .product__item img {
                            height: 230px;
                        }
            
                        .product__item span {
                            font-size: 20px;
                            color: red;
                        }*/

            .next {
                margin-top: 50px;
            }
        </style>
    </head>
    <body>
        <div class="sort">
            <div class="container d-flex">
                <div class="dropdown">
                    <button type="button" class="btn btn-primary dropdown-toggle" style="width: auto; margin-bottom: 10px;" data-toggle="dropdown">
                        S???p x???p theo
                    </button>
                    <div class="dropdown-menu panel-collapse">
                        <button class="dropdown-item" onclick="sortAjax(1, this)">Gi?? t??ng d???n</button>
                        <button class="dropdown-item" onclick="sortAjax(2, this)">Gi?? gi???m d???n</button>
                        <button class="dropdown-item" onclick="sortAjax(3, this)">T??? a t???i z</button>
                        <button class="dropdown-item" onclick="sortAjax(4, this)">T??? z t???i a</button>
                        <button class="dropdown-item" onclick="sortAjax(5, this)">B??n ch???y</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <%--<c:if test="${!empty productList}">--%>
            <div class="br-form" style="padding-bottom: 5px;">
                <div class="product__content row" style="margin: 0 -5px;">
                    <c:forEach items="${productList}" var="i">
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
            <%--</c:if>--%>
            <nav class="next" aria-label="Page navigation example">
                <ul class="pagination d-flex justify-content-center">
                    <c:forEach begin="1" end="${pageNum}" var="i">
                        <li class="page-item ${i == 1 ? " active" : "" }">
                            <button class="page-link" onclick="pagingAjax(this, event)">${i}</button>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
                integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
                integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>
        <script>
                                const parrentElement = document.querySelector(".product__content.row");
                                //            dom t???i th???ng container ch???a c??c productlist
                                const pageElements = document.querySelector(".pagination.d-flex.justify-content-center").querySelectorAll(".page-item");
                                //            DOM t???i c??c button chuy???n trang
                                let getPageNumber = () => {
                                    let count = 0;
                                    for (var i = 0; i < pageElements.length; i++) {
                                        if (pageElements[i].classList.contains("active"))
                                            break;
                                        else
                                            count++;
                                    }
                                    return count + 1;
                                }
                                //            v?? d??? khi ng?????i d??ng click v??o trang 2
                                //            th?? c??i n??t th??? 2 n?? s??? c?? class active
                                //            m??nh s??? d??ng for loop ????? ?????m v?? d???ng l???i khi th???y element c?? class active 
                                //            v?? tr??? v??? count + 1. T???i sao l???i l?? count + 1, v?? khi debug m???i ph??t hi???n ra 

                                //            pagination
                                const getRequestParam = (key) => {
                                    const queryString = window.location.search;
                                    const urlParams = new URLSearchParams(queryString);
                                    // url params = c??c query string c???a url 
                                    //                vd: home.do?a=1&b=2&c=3
                                    //                  th?? sau d???u ? n?? l?? query string v???i c??c parameter l?? a, b, c
                                    return urlParams.get(key);
                                    // return gi?? tr??? c???a th???ng parameter t??n l?? cateId
                                }
                                const formatVND = (currency) => {
                                    // chuy???n int sang vnd 
                                    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(currency);
                                }
                                const removeActiveClass = () => {
                                    // khi ng?????i d??ng b???m v??o c??c n??t trang ( vd trang th??? 1, trang th??? 2)
                                    // th?? ?????u ti??n l?? ph???i x??a active class ??i 
                                    document.querySelectorAll(".page-item.active").forEach(i => {
                                        i.classList.remove("active")
                                    })
                                }
                                const renderData = (productData) => {
                                    // render data th?? b??? data v?? html
                                    parrentElement.innerHTML = ""
                                    // x??a container 
                                    let mainImgUrl = ""
                                    // khai b??o c??i main img 
                                    // productData n?? l?? 1 m???ng object json
                                    // v???i 1 ph???n t??? = 1 product
                                    // => 1 product n?? s??? c?? 1 m???ng image
                                    productData.forEach(i => {
                                        i.imgList.forEach(img => {
                                            if (img.isMainImg)
                                                mainImgUrl = img.url;
                                        });
                                        // t??m th???ng main image trong c??i image list c???a product
                                        parrentElement.innerHTML +=
//                                        '<div class="product__item col-lg-3 col-md-4 col-sm-6">' +
//                                                '<a href="productDetail.do?id=' + i.productId + '">' +
//                                                '<img class="img-fluid" src="' + mainImgUrl + '" alt="">' +
//                                                '</a>' +
//                                                '<a href="productDetail.do?id=' + i.productId + '">' + i.name + '</a><br>' +
//                                                '<span>' + formatVND(i.price) + '</span>' +
//                                                '</div>'
                                                '<div class=" col-lg-2 col-md-3 col-sm-4 col-4" ' +
                                                'style = "padding: 5px; position: static;" > ' +
                                                '<div class="product__item">' +
                                                '<a href="productDetail.do?productId=' + i.productId + '">' +
                                                '<img class="img-fluid" src="' + mainImgUrl + '" alt="">' +
                                                '<p class="tooltip-text hinden-text">' + i.name +
                                                '<span>' + i.name + '</span>' +
                                                '</p>' +
                                                '<div style="display: flex; justify-content: flex-end; padding-top: 5px;">'
                                                + formatVND(i.price) +
                                                '</div>' +
                                                '</a>' +
                                                '</div>' +
                                                '</div>'
                                        // ????? data ra html
                                    })
                                }
                                const sortAjax = (id, el) => {
                                    document.querySelector(".btn.btn-primary.dropdown-toggle").innerHTML = el.innerHTML;
                                    // thay ?????i n???i dung b??n trong button sort 
                                    localStorage.setItem("sortId", id);
                                    $.ajax("/ProjectGroup6/GetProductAjax", {
                                        data: {
                                            func: 'getSortedProductList',
                                            option: id,
                                            cateId: getRequestParam("cateId"),
                                            pageNum: getPageNumber(),
                                            name: getRequestParam("name")
                                        },
                                        success: function (data) {
                                            //                        console.log(data)
                                            renderData(data)
                                        }
                                    })
                                }
                                const pagingAjax = (el, event) => {
                                    //                event.preventDefault();
                                    removeActiveClass()
                                    // x??a active class t???t c??? c??c n??t paging
                                    el.parentElement.classList.add("active")
                                    // th??m class active v??o n??t pagin m?? m??nh ???? b???m
                                    console.log(getPageNumber())
                                    console.log("sort id: " + localStorage.getItem("sortId"))
                                    $.ajax("/ProjectGroup6/GetProductAjax", {
                                        data: {
                                            cateId: getRequestParam("cateId"),
                                            func: 'getSortedProductList',
                                            pageNum: getPageNumber(),
                                            name: getRequestParam("name"),
                                            option: localStorage.getItem("sortId")
                                        },
                                        success: function (data) {
                                            renderData(data)
                                        }
                                    })
                                }
                                const sortBtn = document.querySelector(".btn.btn-primary.dropdown-toggle");
                                switch (getRequestParam("top")) {
                                    case "soldCount":
                                        sortBtn.innerHTML = "B??n ch???y"
                                        break;
                                    case "newest":
                                        sortBtn.innerHTML = "M???i nh???t"
                                        break;
                                }
        </script>

    </body>

</html>