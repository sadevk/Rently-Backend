/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Admin;
import entity.Product;
import entity.User;
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
@WebServlet(name = "AdminLoadProducts", urlPatterns = {"/AdminLoadProducts"})
public class AdminLoadProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        
        JsonObject responseJson = new JsonObject();
        
        HttpSession httpSession = req.getSession();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        if (httpSession.getAttribute("id") != null && httpSession.getAttribute("email") != null) {
            
            Criteria adminCriteria = session.createCriteria(Admin.class);
            adminCriteria.add(Restrictions.and(
                    Restrictions.eq("id", httpSession.getAttribute("id")),
                    Restrictions.eq("email", httpSession.getAttribute("email").toString())
            ));
            
            if (!adminCriteria.list().isEmpty()) {
                
                responseJson.addProperty("authenticated", Boolean.TRUE);
                
                responseJson.addProperty("admin", httpSession.getAttribute("name").toString());
                
                Criteria criteria = session.createCriteria(Product.class);
                
                if (!criteria.list().isEmpty()) {
                    
                    List<Product> products = criteria.list();
                    
                    for (Product product : products) {
                        
                        product.getOwner().setPassword(null);
                        product.getOwner().setStatus(null);
                        
                    }
                    
                    responseJson.add("products", gson.toJsonTree(products));
                    
                }else{
                    
                    responseJson.addProperty("message", "No Products!");
                    
                }
                
            }else{
                
                responseJson.addProperty("authenticated", Boolean.FALSE);
                responseJson.addProperty("message", "Please Login to Continue!");
                
            }
            
        }else{
            
            responseJson.addProperty("message", "Please Login to Continue!");
            responseJson.addProperty("authenticated", Boolean.FALSE);
            
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
        
    }
    
}
