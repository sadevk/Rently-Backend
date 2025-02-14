/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Rental_Status;
import entity.Product;
import entity.Rentals;
import entity.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "PlaceOrder", urlPatterns = {"/PlaceOrder"})
public class PlaceOrder extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();
        User reqUser = gson.fromJson(requestJson.get("user"), User.class);
        Date startDate = new Date();
        Date endDate = new Date();
        
        try{
             
            startDate = dateFormat.parse(requestJson.get("start_date").getAsString());
             endDate = dateFormat.parse(requestJson.get("end_date").getAsString());
             
        }catch(ParseException e){
            e.printStackTrace();
        }

        responseJson.addProperty("success", Boolean.FALSE);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Rentals rentals = new Rentals();
        Product product = (Product) session.load(Product.class, requestJson.get("product").getAsInt());
        Rental_Status rental_Status = (Rental_Status) session.load(Rental_Status.class, 2);

        rentals.setUser(reqUser);
        rentals.setProduct(product);
        rentals.setStart_date(startDate);
        rentals.setEnd_date(endDate);
        rentals.setRental_status(rental_Status);
        rentals.setTotal(requestJson.get("total").getAsDouble());

        try {

            session.save(rentals);
            transaction.commit();

            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Order Placed Successfully!");

        } catch (Exception e) {

            transaction.rollback();
            responseJson.addProperty("error", e.getMessage());
            e.printStackTrace();

        } finally {
            session.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
