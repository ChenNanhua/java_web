package com.example.servlet;

import com.example.tool.DB;
import com.example.tool.MD5;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(value = "/login", loadOnStartup = 1)
public class UserManagementServlet extends HttpServlet {
    private DB db = new DB("web", "user");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        resp.setContentType("text/html;charset=utf-8");
        String login = req.getParameter("登录");          //判断点击的是登录还是注册
        String logout = req.getParameter("注销");
        try {
            if (logout != null) {
                session.removeAttribute("username");
                session.removeAttribute("user_id");
                fail(req, resp, "已注销");
                return;
            }
            //1.获取用户请求发送的数据
            String username = req.getParameter("username");
            String passwd = req.getParameter("passwd");
            if (username == null || passwd == null || username.equals("") || passwd.equals("")) {   //账号或密码为空则非法
                fail(req, resp, "请登录...");
                return;
            }
            passwd = MD5.encrypt(passwd);                   //md5确认密码
            if (login != null) {                                //登录逻辑
                String sql = "select * from user where name='" + username + "' and passwd = '" + passwd + "';";
                db.select(sql);
                if (db.getRows() == 1) {                        //判断用户是否存在
                    int user_id = db.results.getInt("user_id");
                    if (username.equals("root")){               //管理员的特殊逻辑
                        session.setAttribute("username", username);
                        session.setAttribute("user_id", user_id);
                        resp.sendRedirect("./phoneManage");   //返回给用户主界面
                        return;
                    }
                    success(resp, session, username, user_id);
                    System.out.println("登录成功的用户：" + user_id + "\t" + username + "\t" + passwd);
                } else {
                    fail(req, resp, "账密错误，请重新登录");
                }
            } else {                                            //新用户注册逻辑
                System.out.println("准备注册的用户：" + username + "\t" + passwd);
                String address = req.getParameter("address");
                String email = req.getParameter("email");
                if (address.equals("") || email.equals("")) {
                    fail(req, resp, "请填写address email信息！");
                } else {
                    String sql = "select * from user where name='" + username + "';";
                    db.select(sql);
                    if (db.getRows() == 1) {                        //判断账号是否已经注册过
                        fail(req, resp, "用户名已被注册过。");
                    } else {
                        sql = "insert into user (name,passwd,address,email) values ('" + username + "','" + passwd +
                                "','" + address + "','" + email + "');";
                        System.out.println(sql);
                        if (db.execute(sql) == 1) {
                            fail(req, resp, "注册成功！请登录...");
                        } else {
                            fail(req, resp, "注册失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //对request请求对象设置统一的编码
        req.setCharacterEncoding("utf-8");
        doGet(req, resp);
    }

    private void success(HttpServletResponse resp, HttpSession session, String username, int user_id) throws Exception {
        session.setAttribute("username", username);
        session.setAttribute("user_id", user_id);
        resp.sendRedirect("./index");   //返回给用户主界面
    }

    private void fail(HttpServletRequest req, HttpServletResponse resp, String message) throws Exception {
        req.setAttribute("message", message);
        req.getRequestDispatcher("./login.jsp").forward(req, resp);     //重新登录
    }
}
