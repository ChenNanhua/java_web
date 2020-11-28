package com.example.servlet;

import com.example.tool.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.net.URLDecoder;

@WebServlet(value = {"/record"})
public class Record extends HttpServlet {
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
            //从数据库读取日志并输出给jsp
            String sql = "select * from record order by date;";     //查询所有phone_id
            db.select(sql);
            ResultSet results = db.results;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //格式化日期
            ArrayList<ArrayList<String>> order_result = new ArrayList<>();
            if (db.getRows() >= 1) {
                do {
                    String record_user_id = results.getString("user_id");
                    db.select("select * from user where user_id = " + record_user_id + ";");
                    ArrayList<String> order_item = new ArrayList<>(5);   //保存订单的子项
                    order_item.add(db.results.getString("user_id"));
                    order_item.add(db.results.getString("name"));
                    order_item.add(URLDecoder.decode(results.getString("url"), "UTF-8"));
                    order_item.add(fmt.format(results.getTimestamp("date")));
                    order_result.add(order_item);
                } while (results.next());
                req.setAttribute("order_result", order_result);
                req.getRequestDispatcher("./record.jsp").forward(req, resp);
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
}

