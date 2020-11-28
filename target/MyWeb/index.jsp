<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商城界面</title>
    <script type="text/javascript" src="./js/index.js"></script>
    <script type="text/javascript" src="./js/jquery.js"></script>
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
    }
    if (session.getAttribute("username") != null) {     //获取用户名称
        username = session.getAttribute("username").toString();
    }
%>
<body>
<ul class="top">
    <li><a href="./login?注销=注销">注销</a></li>
    <li><a href="./shop">全部订单</a></li>
    <li><a href="./shop">购物车</a></li>
    <li><a href="./index">商城</a></li>
    <li><a href="#user"><%=username%>
    </a></li>
</ul>
<h1>陈宇驰的商城主界面</h1>
<hr>
<div id="sale">
    <h4>商品展示:</h4>
    <table>
        <tr>
            <th>名称</th>
            <th>参数</th>
            <th>价格</th>
            <th>操作</th>
            <th>数量</th>
        </tr>
        <%
            ResultSet results = (ResultSet) request.getAttribute("results");
            if (results != null) {
                try {
                    results.last();
                    if (results.getRow() >= 1) {
                        results.first();
                        do {
                            int phone_id = results.getInt("phone_id");
                            String name = results.getString("name");
                            String type = results.getString("type");
                            String price = results.getString("price");
                            out.print("<tr class=\"tr\">\n" +
                                    "      <td id="+phone_id+">" + name + "</td>\n" +
                                    "      <td>" + type + "</td>\n" +
                                    "      <td>" + price + "</td>\n" +
                                    //显示库存及选择购买数量的按钮
                                    "<td>\n" +
                                    "            <ul class=\"btn-numbox\">\n" +
                                    "                <li>\n" +
                                    "                    <ul class=\"count\">\n" +
                                    "                        <li><span  class=\"num-jian\" width=\"10px\">-</span></li>\n" +
                                    "                        <li><input type=\"text\" class=\"input-num\" width=\"20px\"value=\"1\"/></li>\n" +
                                    "                        <li><span  class=\"num-jia\" width=\"10px\">+</span></li>\n" +
                                    "                    </ul>\n" +
                                    "                </li>\n" +
                                    "                <li><span class=\"kucun\">（库存:" + results.getString("stock") + "）</span></li>\n" +
                                    "                　　　\n" +
                                    "            </ul>\n" +
                                    "        </td>"+
                                    "      <td><a class=\"add\" onmouseover=\"this.style.background='#45B549'\" onmouseout=\"this.style.background=''\">加入购物车</a></td>\n");
                        } while (results.next());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        %>

    </table>
    <span><%=message%></span>
</div>
</body>
</html>