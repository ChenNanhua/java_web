package com.example.servlet;

import com.example.tool.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@WebServlet(value = {"/order"})
public class OderServlet extends HttpServlet {
    private final DB db = new DB("web", "user");
    private int user_id;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            if (session.getAttribute("user_id") == null) {
                resp.sendRedirect("./login?login=login");           //登录状态失效，返回到登录界面
                return;
            }
            user_id = (int) session.getAttribute("user_id");
            //访问记录插入数据库
            String url = req.getRequestURL().toString() + "?" + req.getQueryString();  //得到url
            db.execute("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            System.out.println("insert into record (user_id,url) values (" + user_id + ",\"" + url + "\");");
            //提交付款订单
            String order = req.getParameter("order");
            if (order != null) {    //订单提交后的处理，发送确认邮件
                String sql = "select * from shop_car where user_id=" + user_id + ";";     //查询所有phone_id
                db.select(sql);
                ResultSet results = db.results;
                if (db.getRows() >= 1) {    //如果购物车有数据->把购物车数据添加到订单列表中
                    do {    //把shop_car的订单数据插入进order中，然后删除shop_car中的数据
                        sql = "insert into order_list(user_id,phone_id,num) values (" +
                                user_id + "," + results.getInt("phone_id") + "," + results.getInt("num") + ");";
                        db.execute(sql);
                    } while (results.next());
                    //发送订单信息到客户邮箱
                    String info = get_info();
                    db.select("select email from user where user_id=" + user_id + ";");
                    String email = db.results.getString("email");
                    SendEmail sendEmail = new SendEmail(email, "本次购物清单：", info);
                    sendEmail.start();
                    //删除购物车中的数据
                    sql = "delete from shop_car where user_id=" + user_id + ";";
                    db.execute(sql);
                }
            }
            {   //获取历史订单信息
                ArrayList<ArrayList<String>> order_result = new ArrayList<>(db.getRows());   //保存所有订单
                int price = set_info(order_result);//保存购物车总价格
                req.setAttribute("order_result", order_result);
                req.setAttribute("price", price);
                req.getRequestDispatcher("./order.jsp").forward(req, resp);
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

    protected String get_info() {   // from shop_car
        String info = "";
        int price = 0;
        try {
            //从数据库读取并展示已经添加到数据库的订单信息
            String sql = "select * from shop_car where user_id=" + user_id + ";";     //查询所有phone_id
            db.select(sql);
            ResultSet results = db.results;
            if (db.getRows() >= 1) {
                do {
                    int phone_id = results.getInt("phone_id");
                    db.select("select * from phone where phone_id=" + phone_id + ";");
                    info += db.results.getString("name") + "&nbsp;&nbsp;&nbsp;&nbsp;" + db.results.getString("type") + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                            db.results.getString("price") + "&nbsp;&nbsp;&nbsp;&nbsp;" + results.getString("num") + "台<br>";
                    price = price + Integer.parseInt(db.results.getString("price")) * results.getInt("num"); //购物车物品总价格
                } while (results.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        info += "总价格：" + price + "<br>" + "退订请联系客服";
        System.out.println(info);
        return info;
    }

    protected int set_info(ArrayList<ArrayList<String>> order_result) { // from order_list
        int price = 0;
        try {
            //从数据库读取并展示已经添加到数据库的订单信息
            String sql = "select * from order_list where user_id=" + user_id + ";";     //查询所有phone_id
            db.select(sql);
            ResultSet results = db.results;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //格式化日期
            if (db.getRows() >= 1) {
                do {
                    int phone_id = results.getInt("phone_id");
                    int order_id = results.getInt("order_id");
                    db.select("select * from phone where phone_id=" + phone_id + ";");
                    ArrayList<String> order_item = new ArrayList<>(5);   //保存所有订单
                    order_item.add(String.valueOf(order_id));
                    order_item.add(db.results.getString("name"));       //添加购买的手机信息到order_result中
                    order_item.add(db.results.getString("type"));
                    order_item.add(db.results.getString("price"));
                    order_item.add(results.getString("num"));
                    order_item.add(fmt.format(results.getTimestamp("date")));
                    price = price + Integer.parseInt(db.results.getString("price")) * results.getInt("num"); //购物车物品总价格
                    order_result.add(order_item);
                } while (results.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
}

