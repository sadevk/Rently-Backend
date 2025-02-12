/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
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
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", Boolean.FALSE);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria =  session.createCriteria(User.class);
        
        criteria.add(Restrictions.eq("email", requestJson.get("email").getAsString()));
        criteria.add(Restrictions.eq("password", requestJson.get("password").getAsString()));
        
        if(!criteria.list().isEmpty()){
            
            User user = (User)criteria.list().get(0);
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Found!");
            responseJson.add("user", gson.toJsonTree(user));
            
        }else{
         
            responseJson.addProperty("message", "Not Found!");
            
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
        
    }
    
}
