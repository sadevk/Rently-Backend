/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.List;
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
@WebServlet(name = "LoadDiscover", urlPatterns = {"/LoadDiscover"})
public class LoadDiscover extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         Gson gson = new Gson();
        User requestUser = gson.fromJson(req.getReader(), User.class);
        
        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", Boolean.FALSE);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria =  session.createCriteria(Product.class);
        
        criteria.add(Restrictions.not(Restrictions.eq("owner", requestUser)));
        
        if(!criteria.list().isEmpty()){
            
            List<Product> products = criteria.list();
            
            for (Product product : products) {
                
                User user = product.getOwner();
                user.setPassword(null);
                user.setStatus(null);
                
                
            }
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.add("products", gson.toJsonTree(products));
            
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
        
    }

}
