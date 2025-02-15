/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Product;
import entity.Rentals;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sadev_vr38
 */
@WebServlet(name = "LoadHome", urlPatterns = {"/LoadHome"})
public class LoadHome extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        User requestUser = gson.fromJson(req.getReader(), User.class);

        JsonObject responseJson = new JsonObject();

        Session session = HibernateUtil.getSessionFactory().openSession();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Criteria criteriaOngoing = session.createCriteria(Rentals.class);
        criteriaOngoing.add(Restrictions.eq("user", requestUser));
        criteriaOngoing.add(Restrictions.gt("end_date", new Date()));

        if (!criteriaOngoing.list().isEmpty()) {
            responseJson.addProperty("ongoing", Boolean.TRUE);

            Rentals rental = (Rentals) criteriaOngoing.list().get(0);

            JsonObject ongoingRental = new JsonObject();
            ongoingRental.addProperty("id", rental.getId());
            ongoingRental.addProperty("productId", rental.getProduct().getId());
            ongoingRental.addProperty("productName", rental.getProduct().getName());
            ongoingRental.addProperty("duration", dateFormat.format(rental.getStart_date()) + " - " + dateFormat.format(rental.getEnd_date()));
            ongoingRental.addProperty("price", rental.getTotal() + "0 LKR");

            responseJson.add("ongoingRental", ongoingRental);

        } else {
            responseJson.addProperty("ongoing", Boolean.FALSE);
        }

        Criteria criteriaPopular = session.createCriteria(Rentals.class);
        criteriaPopular.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("product"))
                .add(Projections.rowCount(), "productCount")
        );
        criteriaPopular.addOrder(Order.desc("productCount"));

        List<Object[]> rentalList = criteriaPopular.list();

        JsonArray popularProducts = new JsonArray();
        
        for (Object[] rentals : rentalList) {
            Product product = (Product) rentals[0];
            product.getOwner().setStatus(null);
            product.getOwner().setPassword(null);
            popularProducts.add(gson.toJsonTree(product));
        }

        responseJson.add("popularProducts", popularProducts);
        
        Criteria myRentalsCriteria = session.createCriteria(Rentals.class);
        myRentalsCriteria.add(Restrictions.eq("user", requestUser));
        
        if(!myRentalsCriteria.list().isEmpty()){
            
            List<Rentals> myRentalsList =  myRentalsCriteria.list();
            
            JsonArray myRentals = new JsonArray();
            
            for (Rentals rentals : myRentalsList) {
                JsonObject myRental = new JsonObject();
                myRental.addProperty("productId", rentals.getProduct().getId());
                myRental.addProperty("productName", rentals.getProduct().getName());
                myRental.addProperty("duration", dateFormat.format(rentals.getStart_date()) + " - " + dateFormat.format(rentals.getEnd_date()));
                myRental.addProperty("total", rentals.getTotal() + "0 LKR");
                
                myRentals.add(myRental);
            }
            
            responseJson.add("myRentals", myRentals);
            
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
    }

}
