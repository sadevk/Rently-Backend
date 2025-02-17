/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sadev_vr38
 */
@MultipartConfig
@WebServlet(name = "UpdateProfile", urlPatterns = {"/UpdateProfile"})
public class UpdateProfile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        User updatedUser = gson.fromJson(req.getParameter("user"), User.class);
        Part imagePart = req.getPart("image");
        JsonObject responseJson = new JsonObject();
        String BASE_URL = req.getServletContext().getRealPath("");

        responseJson.addProperty("success", Boolean.FALSE);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {

            File profileImageDir = new File(BASE_URL + File.separator + "profile-images");

            if (!profileImageDir.exists()) {
                profileImageDir.mkdir();
            }

            if (imagePart != null && imagePart.getName().equals("image") && imagePart.getSubmittedFileName() != null) {
                String fileName = "profile_" + updatedUser.getId() + ".png";
                InputStream inputStream = imagePart.getInputStream();
                File imgFile = new File(profileImageDir, fileName);
                Files.copy(inputStream, imgFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            session.update(updatedUser);
            transaction.commit();

            responseJson.addProperty("success", Boolean.TRUE);
            responseJson.addProperty("message", "Updated Successfully!");

        } catch (Exception e) {

            transaction.rollback();
            responseJson.addProperty("error", e.getMessage());
            e.printStackTrace();

        } finally {
            session.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
