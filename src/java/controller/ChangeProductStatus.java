/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Product;
import entity.Product_status;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "ChangeProductStatus", urlPatterns = {"/ChangeProductStatus"})
public class ChangeProductStatus extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();

        Session session = HibernateUtil.getSessionFactory().openSession();
        responseJson.addProperty("success", Boolean.FALSE);

        Product product = (Product) session.load(Product.class, requestJson.get("product_id").getAsInt());

        Product_status active = (Product_status) session.load(Product_status.class, 1);
        Product_status inactive = (Product_status) session.load(Product_status.class, 2);

        if (product.getStatus().getId() == active.getId()) {

            product.setStatus(inactive);
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Product Deactivated");

        } else if (product.getStatus().getId() == inactive.getId()) {

            product.setStatus(active);
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Product Activated");

        }

        Transaction transaction = session.beginTransaction();

        try {

            session.update(product);
            transaction.commit();

        } catch (Exception e) {

            transaction.rollback();

        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
