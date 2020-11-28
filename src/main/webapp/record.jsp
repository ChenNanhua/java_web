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
    if (request.getAttribute("message") != null) {
        message = request.getAttribute("message").toString();
    }
    if (session.getAttribute("username") == null) {     //用户未登录成功
        response.sendRedirect("./login.jsp");           //重新登录
    } else {
        username = session.getAttribute("username").toString();
    }
%>
<body>
<ul class="top">
    <li><a href="./login?注销=注销">注销</a></li>
    <li><a href="./record">客户日志</a></li>
    <li><a href="./statistic">销售报表</a></li>
    <li><a href="./phoneManage">商品目录</a></li>
    <li><a href="#user"><%=username%>
    </a></li>
</ul>
<div id="record">
    <h1>用户访问日志 :</h1>
    <table>
        <tr>
            <th>user_id</th>
            <th>名字</th>
            <th>url</th>
            <th>日期</th>
        </tr>
        <%
            if (request.getAttribute("order_result") != null) {
                @SuppressWarnings("unchecked")
                ArrayList<ArrayList<String>> order_result = (ArrayList<ArrayList<String>>) request.getAttribute("order_result");
                for (ArrayList<String> order_item : order_result) {
                    out.print("<tr>\n");
                    for (int i = 0; i < order_item.size(); i++) {
                        if (i != 2) {
                            out.print("<td>" + order_item.get(i) + "</td>");
                        }else {
                            out.print("<td width=\"700px\">" + order_item.get(i) + "</td>");
                        }
                    }
                    //out.print("<td><a href=\"./shop?delete=delete&shop_car_id=" + order_item.get(0) + "\">删除</a></td>");
                    out.print("\n</tr>");
                }
            }
        %>
    </table>
    <span><%=message%></span>
</div>
</body>
</html>