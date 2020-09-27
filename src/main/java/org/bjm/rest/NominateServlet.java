/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ElectoralBeanLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
public class NominateServlet extends HttpServlet {
    
    private static final Logger LOGGER=Logger.getLogger(NominateServlet.class.getName());
    
    @Inject
    private ElectoralBeanLocal ebl;

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
        LOGGER.info("mode is : "+request.getParameter("mode"));
        String mode=request.getParameter("mode");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        String stateCd=user.getStateCode();
        List<String> constituenciesL=null;
        if (mode.equals("vs")){
           constituenciesL=ebl.getVSConstituencies(stateCd); 
        }else if(mode.equals("ls")){
           constituenciesL=ebl.getLSConstituencies(stateCd); 
        }else throw new ServletException("mode parameter is invalid");
         
        StringBuffer sb=new StringBuffer("[");
        for (int i=0;i<constituenciesL.size();i++){
            if (i==(constituenciesL.size()-1)){//last value
               sb.append("\""+constituenciesL.get(i)+"\"");
            }else{
               sb.append("\""+constituenciesL.get(i)+"\","); 
            }
        }
        sb.append("];");
        String toReturn=sb.toString();
        LOGGER.log(Level.INFO, "Returning: {0}", toReturn);
        out.println(toReturn);
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

}
