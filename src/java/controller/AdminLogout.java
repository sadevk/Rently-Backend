/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Admin;
import entity.Payments;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(name = "AdminLogout", urlPatterns = {"/AdminLogout"})
public class AdminLogout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        
        JsonObject responseJson = new JsonObject();
        
        responseJson.addProperty("success", Boolean.FALSE);
        
        HttpSession httpSession = req.getSession();
        
        httpSession.removeAttribute("id");
        httpSession.removeAttribute("email");
        httpSession.removeAttribute("name");
        
        httpSession.invalidate();
        
        responseJson.addProperty("success", Boolean.TRUE);
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
        
        
    }
    
}
