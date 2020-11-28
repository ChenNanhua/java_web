<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的购物车</title>
    <link rel="stylesheet" type="text/css" href="./css/index.css">
</head>
<%
    String username = "";
    String message = "";
    int price = 0;
    if (request.getAttribute("message") != null) {
        message = request.getAttribute("message").toString();
    }
    if (session.getAttribute("username") == null) {     //用户未登录成功
        response.sendRedirect("./login.jsp");           //重新登录
    } else {
        username = session.getAttribute("username").toString();
    }
    if (request.getAttribute("price") != null) {     //获取用户名称
        price = Integer.parseInt(request.getAttribute("price").toString());
    }
%>
<body>
<ul class="top">
    <li><a href="./login?注销=注销">注销</a></li>
    <li><a href="./order">全部订单</a></li>
    <li><a href="./shop">购物车</a></li>
    <li><a href="./index">商城</a></li>
    <li><a href="#user"><%=username%>
    </a></li>
</ul>
<div id="shop">
    <h1>历史订单:</h1>
    <table>
        <tr>
            <th>order_id</th>
            <th>名称</th>
            <th>参数</th>
            <th>价格</th>
            <th>数量</th>
            <th>日期</th>
        </tr>
        <%
            if (request.getAttribute("order_result") != null) {
                @SuppressWarnings("unchecked")
                ArrayList<ArrayList<String>> order_result = (ArrayList<ArrayList<String>>) request.getAttribute("order_result");
                for (ArrayList<String> order_item : order_result) {
                    out.print("<tr>\n");
                    for (int i = 0; i < order_item.size(); i++) {
                        out.print("<td>" + order_item.get(i) + "</td>");
                    }
                    //out.print("<td><a href=\"./shop?delete=delete&shop_car_id=" + order_item.get(0) + "\">删除</a></td>");
                    out.print("\n</tr>");
                }
            }
        %>
    </table>
    <p>
        总价格：￥<%=price%>
    </p>
    <span><%=message%></span>
</div>
</body>
</html>