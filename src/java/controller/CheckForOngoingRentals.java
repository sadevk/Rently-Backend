/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Rentals;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
@WebServlet(name = "CheckForOngoingRentals", urlPatterns = {"/CheckForOngoingRentals"})
public class CheckForOngoingRentals extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(req.getReader(), User.class);

        JsonObject responseJson = new JsonObject();

        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria = session.createCriteria(Rentals.class);
        criteria.add(Restrictions.eq("user", requestUser));
        criteria.add(Restrictions.gt("end_date", new Date()));
        
        if(!criteria.list().isEmpty()){
            responseJson.addProperty("available", Boolean.TRUE);
        }else{
            responseJson.addProperty("available", Boolean.FALSE);
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
        
    }
    
}
