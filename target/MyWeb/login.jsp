<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录/注册页面</title>
    <link rel="stylesheet" type="text/css" href="./css/login.css">
</head>
<%
    String message = "";
    if (request.getAttribute("message") != null) {
        message = request.getAttribute("message").toString();
    }
%>
<body>
<div id="form" class="login">
    <div class="logo">用户登陆</div>
    <div class="login_form">
        <form id="Login" name="Login" method="get" onsubmit="" action="./login">
            <div class="login-item">
                <label for="username">用户名：</label>
                <input type="text" id="username" name="username" class="login_input">
            </div>
            <div class="login-item">
                <label for="password">密码：</label>
                <input type="password" id="password" name="passwd" class="login_input">
            </div>
            <div class="login-item">
                <label for="address">地址：</label>
                <input type="text" id="address" name="address" class="login_input" placeholder="注册才需填写">
            </div>
            <div class="login-item">
                <label for="email">邮箱：</label>
                <input type="email" id="email" name="email" class="login_input" placeholder="注册才需填写">
            </div>
            <div class="login-sub">
                <input type="submit" name="登录" value="登录"/>
                <input type="submit" name="注册" value="注册"/>
            </div>
            <br>
            <span id="msg" class="error"><%=message%></span>
        </form>
    </div>
</div>
</body>
</html>