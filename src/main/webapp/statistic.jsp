<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>销售报表</title>
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
    <li><a href="./record">客户日志</a></li>
    <li><a href="./statistic">销售报表</a></li>
    <li><a href="./phoneManage">商品目录</a></li>
    <li><a href="#user"><%=username%>
    </a></li>
</ul>
<div id="statistic">
    <h1>销售情况统计:</h1>
    <table>
        <tr>
            <th>名称</th>
            <th>参数</th>
            <th>库存</th>
            <th>卖出</th>
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
                    out.print("\n</tr>");
                }
            }
        %>
    </table>
    <p>
        总销售额：￥<%=price%>
    </p>
    <span><%=message%></span>
</div>
</body>
</html>