/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import entity.User_Status;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", Boolean.FALSE);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            User_Status status = (User_Status) session.get(User_Status.class, 1);

            if (status == null) {
                throw new Exception("User status not found");
            }

            User user = new User();
            user.setFname(requestJson.get("fname").getAsString());
            user.setLname(requestJson.get("lname").getAsString());
            user.setMobile(requestJson.get("mobile").getAsString());
            user.setEmail(requestJson.get("email").getAsString());
            user.setPassword(requestJson.get("password").getAsString());
            user.setStatus(status);

            session.save(user);
            transaction.commit();

            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Sign Up Successfull");

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
