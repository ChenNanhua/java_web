<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理界面</title>
    <script type="text/javascript" src="./js/phoneManage.js"></script>
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
    <li><a href="./record">客户日志</a></li>
    <li><a href="./statistic">销售报表</a></li>
    <li><a href="./phoneManage">商品目录</a></li>
    <li><a href="#user"><%=username%>
    </a></li>
</ul>
<h1>商品目录:</h1>
<hr>
<div id="manage">
    <h2>所有商品:</h2>
    <table>
        <tr>
            <th>名称</th>
            <th>参数</th>
            <th>价格</th>
            <th>库存</th>
            <th>操作</th>
            <th>更新</th>
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
                            String stock = results.getString("stock");
                            out.print("<tr class=\"tr\">\n" +
                                    "      <td id=" + phone_id + ">" + "<input type=\"text\" class=\"li_phone\"value=\"" + name + "\"/></td>\n" +
                                    "      <td><input type=\"text\" class=\"li_phone\"value=\"" + type + "\"/></td>\n" +
                                    "      <td><input type=\"text\" class=\"li_phone\"value=\"" + price + "\"/></td>\n" +
                                    "      <td><input type=\"text\" class=\"li_phone\"value=\"" + stock + "\"/></td>\n" +
                                    //显示删除该商品的按钮
                                    "      <td><a class=\"delete\" onmouseover=\"this.style.background='#ff0000'\" onmouseout=\"this.style.background=''\">删除</a></td>\n" +
                                    "      <td><a class=\"delete\" onmouseover=\"this.style.background='#45B549'\" onmouseout=\"this.style.background=''\">更新</a></td>\n"
                            );
                        } while (results.next());
                        out.print("<tr class=\"tr\">\n" +
                                "      <td><input type=\"text\" class=\"li_phone\"/></td>\n" +
                                "      <td><input type=\"text\" class=\"li_phone\"/></td>\n" +
                                "      <td><input type=\"text\" class=\"li_phone\"/></td>\n" +
                                "      <td><input type=\"text\" class=\"li_phone\"/></td>\n" +
                                //显示删除该商品的按钮
                                "      <td><a class=\"add\" onmouseover=\"this.style.background='#ff0000'\" onmouseout=\"this.style.background=''\">添加</a></td>\n");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        %>
    </table>
    <span><font color="red"><%=message%></font></span>
</div>
</body>
</html>