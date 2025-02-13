/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
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

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "LoadCategories", urlPatterns = {"/LoadCategories"})
public class LoadCategories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", Boolean.FALSE);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria =  session.createCriteria(Category.class);
        
        if(!criteria.list().isEmpty()){
            List<Category> categories = criteria.list();
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.add("categories", gson.toJsonTree(categories));
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
    }
    
}
