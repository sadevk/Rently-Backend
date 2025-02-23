/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
import entity.Product_status;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

/**
 *
 * @author sadev_vr38
 */
@MultipartConfig
@WebServlet(name = "AddListing", urlPatterns = {"/AddListing"})
public class AddListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getParameter("json"), JsonObject.class);
        Part imagePart = req.getPart("image");
        String BASE_URL = req.getServletContext().getRealPath("");

        JsonObject responseJson = new JsonObject();

        User owner = gson.fromJson(requestJson.get("user"), User.class);
        Category category = gson.fromJson(requestJson.get("category"), Category.class);

        responseJson.addProperty("success", Boolean.FALSE);

        if (!owner.getLongitude().isEmpty() || !owner.getLongitude().isEmpty()) {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            Product_status active = (Product_status) session.load(Product_status.class, 1);

            Product product = new Product();
            product.setName(requestJson.get("name").getAsString());
            product.setDescription(requestJson.get("description").getAsString());
            product.setRent_per_day(requestJson.get("rent").getAsDouble());
            product.setOwner(owner);
            product.setStatus(active);
            product.setCategory(category);

            try {

                session.save(product);
                transaction.commit();

                File productImageDir = new File(BASE_URL + File.separator + "product-images");

                if (!productImageDir.exists()) {
                    productImageDir.mkdir();
                }

                Criteria criteria = session.createCriteria(Product.class);
                criteria.addOrder(Order.desc("id"));

                Product updatedProduct = (Product) criteria.list().get(0);

                if (imagePart != null && imagePart.getName().equals("image") && imagePart.getSubmittedFileName() != null) {
                    String fileName = "product_" + updatedProduct.getId() + ".png";
                    InputStream inputStream = imagePart.getInputStream();
                    File imgFile = new File(productImageDir, fileName);
                    Files.copy(inputStream, imgFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                responseJson.addProperty("success", Boolean.TRUE);
                responseJson.addProperty("message", "Listing Added Successfully!");

            } catch (Exception e) {

                transaction.rollback();
                responseJson.addProperty("error", e.getMessage());
                e.printStackTrace();

            } finally {
                session.close();
            }
        } else {
            responseJson.addProperty("message", "Please Update Your Pickup Location!");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
    }

}
