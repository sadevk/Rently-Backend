/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Admin;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "AdminLogin", urlPatterns = {"/AdminLogin"})
public class AdminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);

        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", Boolean.FALSE);

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria adminCriteria = session.createCriteria(Admin.class);
        adminCriteria.add(
                Restrictions.and(
                        Restrictions.eq("email", requestJson.get("email").getAsString()),
                        Restrictions.eq("password", requestJson.get("password").getAsString())
                )
        );
        
        if(!adminCriteria.list().isEmpty()){
            
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Login Successful");
            
            Admin admin = (Admin)adminCriteria.list().get(0);
            
            HttpSession httpSession =  req.getSession();
            httpSession.setAttribute("id", admin.getId());
            httpSession.setAttribute("email", admin.getEmail());
            httpSession.setAttribute("name", admin.getFname()+" "+admin.getLname());
            
        }else{
            
            responseJson.addProperty("message", "Invalid Credentials!");
            
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
