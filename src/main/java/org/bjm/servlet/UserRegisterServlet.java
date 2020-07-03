/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.servlet;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.bjm.mbean.UserRegisterMBean;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ConvertPngToJpg;
import org.bjm.util.ImageUtil;

/**
 *
 * @author root
 */
@WebServlet(name = "UserRegisterServlet", urlPatterns = {"/UserRegisterServlet"}, initParams = {
                @WebInitParam(name = "iconSize", value = "120"),
                @WebInitParam(name = "imgFormat", value = "jpeg")
        })
@MultipartConfig

public class UserRegisterServlet extends HttpServlet {
    
    
    private static final Logger LOGGER=Logger.getLogger(UserRegisterServlet.class.getName());
    
    List<String> errors=new ArrayList<>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user=new User();
        user.setFirstname(request.getParameter("firstname"));
        user.setLastname(request.getParameter("lastname"));
        user.setEmail(request.getParameter("email"));
        String dob=request.getParameter("dob");
        String gender=request.getParameter("gender");
        String stateCd=request.getParameter("stateCd");
        String phone=request.getParameter("phone");
        String mobile=request.getParameter("mobile");
        
        //DOB > 16
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate userDob= LocalDate.parse(dob, formatter);
        LocalDate dateToday=LocalDate.now();
        Period age=Period.between(userDob, dateToday);
        if(age.getYears()<16){
            errors.add("Min age must be 16");
        }else{
            user.setDob(userDob);
        }
        //Gender
        if (gender==null || gender.isEmpty()){
            errors.add("Gender not selected.");
        }
        
        
        //Phone and Mobile
        if (phone.isEmpty() && mobile.isEmpty()){
            errors.add("Phone or Mobile required");
        }
        
        if (!phone.isEmpty()) {
            if (!phone.startsWith("0")) {//prefix
                errors.add("Phone must begin with STD Code (0)");
            } else if (phone.length() != 11) {//Phone with STD Code (excluding the 0) must made 10 chars
                errors.add("Total 11 digits expected on Phone (incl. 0).");
            } else {
                user.setPhone(phone);
            }
        }
        
        
        
        if (!mobile.isEmpty()) {
            if (!mobile.startsWith("0")) {//prefix
                errors.add("Mobile must begin with STD Code (0)");
            } else if (mobile.length() != 11) {//Phone with STD Code (excluding the 0) must made 10 chars
                errors.add("Total 11 digits expected on Mobile (incl. 0).");
            } else {
                user.setMobile(mobile);
            }
        }
        
        //State Code
        if (stateCd.equals("--")){
           errors.add("Total 11 digits expected on Mobile (incl. 0)."); 
        }else{
            user.setStateCode(stateCd);
        }
        
        //Profile Image 
        saveProfileImage(user, request, response);
        
        //Check if we need to send Errors
        if (!errors.isEmpty()){
            response.setStatus(400);
            Gson gson=new Gson();
            String errorJson=gson.toJson(errors);
            PrintWriter out = response.getWriter();
            out.print(errorJson);
            out.flush();
        }else{
            HttpSession session=request.getSession(true);
            session.setAttribute(BJMConstants.USER, user);
            response.sendRedirect("UserRegisterConfirm.html");
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void saveProfileImage(User user,HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException  {
        
        Part profileFile=request.getPart("profilePic");
        BufferedImage logoBufferedImage=null;
        if (profileFile==null){//User did not Upload the Profile file. Make Avatar with the Initials
            try {
                char[] chars = new char[2];
                String sizeStr = getInitParameter("iconSize");
                String imgFormat = getInitParameter("imgFormat");
                int size = Integer.parseInt(sizeStr);
                chars[0] = user.getFirstname().charAt(0);
                chars[1] = user.getLastname().charAt(0);
                String text = new String(chars);
                logoBufferedImage = ImageUtil.drawIcon(size, text);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(logoBufferedImage, imgFormat, baos);
                baos.flush();
                byte[] jpgData = baos.toByteArray();
                baos.close();
                user.setProfileFile(text+"."+imgFormat);
                user.setImage(jpgData);
            } catch (IOException ex) {
                Logger.getLogger(UserRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            
            try {
                InputStream input = profileFile.getInputStream();
            
            int fileSize = (int) profileFile.getSize();
            if (fileSize > (1000 * 1024)) {
                errors.add("Profile Img exceeds 1MB");

            } else {//process with the processing of the image.
                //Step 1 Resize the image
                logoBufferedImage = ImageUtil.resizeImage(input, 150);
                String fullFileName = profileFile.getSubmittedFileName();
                String fileType=fullFileName.substring(fullFileName.indexOf('.'));
                byte[] jpgData=null;
                if (fileType.equals("png")){//convert to jpg first. Jelastic' OpenJDK doen not handle png images well and throw exception.
                    byte[] pngData=new byte[input.available()];
                    jpgData=ConvertPngToJpg.convertToJpg(pngData);
                }else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(logoBufferedImage, "jpg", baos);
                    baos.flush();
                    jpgData = baos.toByteArray();
                    baos.close();
                }
                user.setProfileFile(fullFileName);
                user.setImage(jpgData);
                
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

        }
    }

}
