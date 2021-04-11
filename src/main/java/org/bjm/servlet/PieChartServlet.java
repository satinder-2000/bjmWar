/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.model.Survey;
import org.bjm.model.SurveyVote;
import org.bjm.model.VoteType;
import org.bjm.util.BJMConstants;
import org.bjm.util.ImageUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author root
 */
@WebServlet(name = "PieChartServlet", urlPatterns = {"/PieChartServlet"})
public class PieChartServlet extends HttpServlet {
    
    int width = 500;
    int height = 350;
    
    
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;

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
        String surveyIdStr=request.getParameter("surveyId");
        int surveyId=Integer.parseInt(surveyIdStr);
        int agreeCt=0; 
        int disagreeCt=0;
        int undecidedCt=0;
        List<SurveyVote> surveyVotes=surveyBeanLocal.getSurveyVotes(surveyId);
        String diaS=request.getParameter("dia");
        int dia=Integer.parseInt(diaS);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        if (surveyVotes.isEmpty()){
            //FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle rb = ResourceBundle.getBundle("org.bjm.mbean.messages");// context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
            String text=rb.getString("noVoteYet");
            int diaE=(int)(dia*.5);
            BufferedImage bufferedImage=ImageUtil.drawEmptyPieChart(diaE, text, 12);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] pngData = baos.toByteArray();
            baos.close();
            os.write(pngData);
        }else{
            for (SurveyVote sv : surveyVotes) {
                if (sv.getVoteType() == VoteType.AGREE) {
                    agreeCt++;
                } else if (sv.getVoteType() == VoteType.DISAGREE) {
                    disagreeCt++;
                } else if (sv.getVoteType() == VoteType.UNDECIDED) {
                    undecidedCt++;
                }
            }
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue(VoteType.AGREE.toString(), new Double(agreeCt));
            dataset.setValue(VoteType.DISAGREE.toString(), new Double(disagreeCt));
            dataset.setValue(VoteType.UNDECIDED.toString(), new Double(undecidedCt));
            JFreeChart chart = ChartFactory.createPieChart("", dataset, true, false, false);
            chart.setBorderVisible(false);
            ChartUtils.writeChartAsPNG(os, chart, dia, dia);
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

}
