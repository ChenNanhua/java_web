package com.example.servlet;

import com.example.tool.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = {"/phoneManage"})
public class PhoneManageServlet extends HttpServlet {
    private final DB db = new DB("web", "phone");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            if (session.getAttribute("user_id") == null) {
                resp.sendRedirect("./login?login=login");           //登录状态失效，返回到登录界面
                return;
            }
            int user_id = (int) session.getAttribute("user_id");
            db.select("select name from user where user_id=" + user_id + ";");
            //判断是否是root用户，非root用户无法访问
            if (!db.results.getString("name").equals("root")) {
                System.out.println("非root用户!");
                req.setAttribute("message", "权限不足，无法访问");
                req.getRequestDispatcher("./index").forward(req, resp);     //jsp页面显示
                return;
            }
            if (req.getParameter("delete") != null) {     //删除一件商品
                String phone_id = req.getParameter("phone_id");
                int num = db.execute("delete from phone where phone_id=" + phone_id + ";");
                //判断该手机是否有库存
                if (num == 0) {
                    req.setAttribute("message", "删除信息失败!");
                } else {
                    req.setAttribute("message", "删除信息成功!");
                }
            }
            if (req.getParameter("add") != null) {     //添加新的商品
                String name = req.getParameter("name");
                String type = req.getParameter("type");
                String stock = req.getParameter("stock");
                String price = req.getParameter("price");
                System.out.println("insert into phone (name,type,stock,price) values(\"" + name + "\",\"" + type + "\"," + stock + "," + price + ");");
                int num = db.execute("insert into phone (name,type,stock,price) values(\"" + name + "\",\"" + type + "\"," + stock + "," + price + ");");
                //判断该手机是否有库存
                if (num == 0) {
                    req.setAttribute("message", "添加信息失败!");
                } else {
                    req.setAttribute("message", "添加信息成功!");
                }
            }
            if (req.getParameter("update") != null) {     //更新商品目录
                String phone_id = req.getParameter("phone_id");
                String name = req.getParameter("name");
                String type = req.getParameter("type");
                String stock = req.getParameter("stock");
                String price = req.getParameter("price");
                int num = db.execute("update phone set name=\"" + name + "\"," +
                        "type=\"" + type + "\"," +
                        "stock=\"" + stock + "\"," +
                        "price=\"" + price + "\" where phone_id=" + phone_id);
                //判断该手机是否有库存
                if (num == 0) {
                    req.setAttribute("message", "更新信息失败!");
                } else {
                    req.setAttribute("message", "更新信息成功!");
                }
            }
            String sql = "select * from phone;";            //显示所有的商品信息
            db.select(sql);
            req.setAttribute("results", db.results);
            req.getRequestDispatcher("./phoneManage.jsp").forward(req, resp);     //jsp页面显示
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
}
