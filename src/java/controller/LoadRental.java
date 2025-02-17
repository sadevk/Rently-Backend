/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import entity.Rentals;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "LoadRental", urlPatterns = {"/LoadRental"})
public class LoadRental extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        responseJson.addProperty("found", Boolean.FALSE);

        Criteria criteria = session.createCriteria(Rentals.class);
        criteria.add(Restrictions.eq("id", requestJson.get("id").getAsInt()));
        
        Rentals rental = (Rentals)criteria.uniqueResult();
        
        rental.getProduct().getOwner().setPassword(null);
        rental.getProduct().getOwner().setStatus(null);
        rental.getUser().setPassword(null);
        rental.getUser().setStatus(null);
        
        responseJson.addProperty("found", Boolean.TRUE);
        responseJson.add("rental", gson.toJsonTree(rental));
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
