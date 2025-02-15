/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Product;
import entity.Rentals;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "LoadMyListings", urlPatterns = {"/LoadMyListings"})
public class LoadMyListings extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(req.getReader(), User.class);

        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", Boolean.FALSE);

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteriaProduct = session.createCriteria(Product.class);

        criteriaProduct.add(Restrictions.eq("owner", requestUser));
        
        JsonArray listingArray = new JsonArray();

        if (!criteriaProduct.list().isEmpty()) {

            List<Product> products = criteriaProduct.list();

            for (Product product : products) {

                User user = product.getOwner();
                user.setPassword(null);
                user.setStatus(null);
                
                JsonObject productJson = gson.fromJson(gson.toJsonTree(product),JsonObject.class);

                Criteria criteriaRental = session.createCriteria(Rentals.class);
                criteriaRental.add(Restrictions.eq("product", product));
                criteriaRental.add(Restrictions.gt("end_date",new Date()));
                
                if (criteriaRental.list().isEmpty()) {
                    productJson.addProperty("ongoing", Boolean.FALSE);
                }else{
                    productJson.addProperty("ongoing", Boolean.TRUE);
                }
                
                listingArray.add(productJson);
                

            }
            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.add("products", gson.toJsonTree(listingArray));

        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
    }

}
