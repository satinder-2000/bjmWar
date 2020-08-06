/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "homeMBean")
@SessionScoped
public class HomeMBean implements Serializable {
    
    static final Logger LOGGER=Logger.getLogger(HomeMBean.class.getName());
    
    public void redirectToSiteHome(){
        
        ExternalContext ctx=FacesContext.getCurrentInstance().getExternalContext();
        
        HttpServletRequest request= (HttpServletRequest)ctx.getRequest();
        HttpServletResponse response= (HttpServletResponse)ctx.getResponse();
        try {
            LOGGER.log(Level.INFO, "Redirecting to:{0}", "/");
            response.sendRedirect("/");
        } catch (IOException ex) {
            Logger.getLogger(HomeMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void redirectToHome(){
        String homePage=null;
        HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession(true);
        if (session.getAttribute(BJMConstants.USER)==null){
           homePage = request.getContextPath() + "user/UserHome?faces-redirect=true";
        }else{
           homePage = request.getContextPath();
        }            
        HttpServletResponse response= (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            LOGGER.log(Level.INFO, "Redirecting to:{0}", request.getContextPath());
            response.sendRedirect(homePage);
        } catch (IOException ex) {
            Logger.getLogger(HomeMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
