package com.example.servlet;

import com.example.tool.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(value = {"/shop"})
public class ShopServlet extends HttpServlet {
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
            //访问记录插入数据库
            String url = req.getRequestURL().toString() + "?" + req.getQueryString();  //得到url
            db.execute("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            System.out.println("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            //购物车中的删除操作
            String delete = req.getParameter("delete");
            if (delete != null) {
                String shop_car_id = req.getParameter("shop_car_id");
                db.execute("delete from shop_car where shop_car_id=" + shop_car_id + ";");
            }
            //从数据库读取并展示已经添加到数据库的订单信息
            String sql = "select * from shop_car where user_id=" + user_id + ";";     //查询用户要购买的所有phone_id
            db.select(sql);
            ResultSet results = db.results;
            ArrayList<ArrayList<String>> order_result = new ArrayList<>(db.getRows());   //保存所有订单
            int price = 0;      //保存购物车总价格
            if (db.getRows() >= 1) {
                do {
                    int phone_id = results.getInt("phone_id");
                    int shop_car_id = results.getInt("shop_car_id");
                    db.select("select * from phone where phone_id=" + phone_id + ";");
                    ArrayList<String> order_item = new ArrayList<>(5);   //保存所有订单
                    order_item.add(String.valueOf(shop_car_id));
                    order_item.add(db.results.getString("name"));       //添加购买的手机信息到order_result中
                    order_item.add(db.results.getString("type"));
                    order_item.add(db.results.getString("price"));
                    order_item.add(results.getString("num"));
                    price = price + Integer.parseInt(db.results.getString("price")) * results.getInt("num"); //购物车物品总价格
                    order_result.add(order_item);
                } while (results.next());
            }
            req.setAttribute("order_result", order_result);
            req.setAttribute("price", price);
            req.getRequestDispatcher("./shop.jsp").forward(req, resp);
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
