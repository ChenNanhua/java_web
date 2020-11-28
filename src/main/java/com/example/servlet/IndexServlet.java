package com.example.servlet;

import com.example.tool.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = {"/index"})
public class IndexServlet extends HttpServlet {
    private DB db = new DB("web", "user");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            if (session.getAttribute("user_id") == null) {
                resp.sendRedirect("./login?login=login");           //登录状态失效，返回到登录界面
                return;
            }
            int user_id = (int) session.getAttribute("user_id");
            //访问记录插入数据库
            String url = req.getRequestURL().toString() + "?" + req.getQueryString();  //得到url
            db.execute("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            System.out.println("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            if (req.getParameter("shop") != null) {     //添加至购物车的操作
                String phone_id = req.getParameter("phone_id");
                int num = Integer.parseInt(req.getParameter("num"));
                db.select("select stock from phone where phone_id=" + phone_id + ";");
                int stock = db.results.getInt("stock");
                //判断该手机是否有库存
                if (stock < num) {
                    req.setAttribute("message", "库存不足，无法购买！");
                } else {
                    db.select("select * from shop_car where user_id=" + user_id + " and phone_id=" + phone_id + ";");
                    String sql = "";
                    if (db.getRows() == 0) {    //判断是否已有该手机的购物信息
                        sql = "insert into shop_car (user_id,phone_id,num) values (" + user_id + "," + phone_id + "," + num + ");";
                    } else {
                        sql = "update shop_car set num=num+" + num + ";";
                    }
                    if (db.execute(sql) == 1) {
                        String message = req.getParameter("name") + "&nbsp;&nbsp;" + req.getParameter("type") + "&nbsp;&nbsp;" + num + "台添加到购物车成功";
                        req.setAttribute("message", message);
                    } else {
                        req.setAttribute("message", "添加到购物车失败，请联系管理员！");
                    }
                }
            }
            String sql = "select * from phone;";            //显示所有的商品信息
            db.select(sql);
            req.setAttribute("results", db.results);
            req.getRequestDispatcher("./index.jsp").forward(req, resp);     //jsp页面显示
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
