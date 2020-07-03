/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "logoutMBean")
@RequestScoped
public class LogoutMBean {
    
    static final Logger LOGGER=Logger.getLogger(LogoutMBean.class.getName());
    
    
    public String logOut(){
        LOGGER.info("Logout Called.");
        HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(BJMConstants.USER);
        if (user!=null){
            String email=user.getEmail();
            session.removeAttribute(BJMConstants.USER);
            LOGGER.log(Level.INFO, "LogoutMBean : BJM User {0} logged out successfully.", email);
        }
        
        return "/LogoutConfirm?faces-redirect=true";
    }
    
}
