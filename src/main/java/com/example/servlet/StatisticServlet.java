package com.example.servlet;

import com.example.tool.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(value = {"/statistic"})
public class StatisticServlet extends HttpServlet {
    private final DB db = new DB("web", "user");

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
            //从数据库读取所有的订单信息
            String sql = "select * from phone";     //查询所有在售的手机信息
            db.select(sql);
            ResultSet results = db.results;
            ArrayList<ArrayList<String>> order_result = new ArrayList<>(db.getRows());   //保存所有要展示的销售报表的信息
            int price = 0;      //保存购物车总价格
            if (db.getRows() >= 1) {
                do {
                    int phone_id = results.getInt("phone_id");
                    int phone_price = results.getInt("price");
                    String name = results.getString("name");
                    String type = results.getString("type");
                    String stock = results.getString("stock");
                    db.select("select sum(num) as sum from order_list where phone_id=" + phone_id + " group by phone_id;");
                    ArrayList<String> order_item = new ArrayList<>(5);   //保存所有订单
                    order_item.add(name);       //添加购买的手机信息到order_result中
                    order_item.add(type);
                    order_item.add(stock);
                    if (db.getRows() < 1) {
                        order_item.add("0");
                    } else {
                        order_item.add(db.results.getString(1));
                        price = price + Integer.parseInt(db.results.getString("sum")) * phone_price; //计算卖出的物品总价格
                    }
                    order_result.add(order_item);
                } while (results.next());
            }
            req.setAttribute("order_result", order_result);
            req.setAttribute("price", price);
            req.getRequestDispatcher("./statistic.jsp").forward(req, resp);
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