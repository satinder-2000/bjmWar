/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;


/**
 *
 * @author root
 */
@Named(value = "loginMBean")
@ViewScoped
public class LoginMBean implements Serializable {
    
     static final Logger LOGGER=Logger.getLogger(LoginMBean.class.getName());
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    
    private String email;
    
    private String password;
    
    String documentServer;
    
    ExternalContext extCtx;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @PostConstruct
    public void init(){
        extCtx=FacesContext.getCurrentInstance().getExternalContext();
        LOGGER.info("View created for Login");
    }
    
    public String logIn(){
        User user =null;
        String type= null;
        String nextPage=null;
        try {
            if(email.isEmpty()){
                throw new RuntimeException("No Email provided");
            }
                user= userBeanLocal.getUser(email, password);
            if (user.getExceptionMsg()!=null){
                throw new RuntimeException(user.getExceptionMsg());
            }
            
            if (user!=null){
                LOGGER.info("Access granted to "+user.getEmail());
            }else{
                LOGGER.warning("Failed to grant Access");
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute(BJMConstants.USER, user);
            LOGGER.info("Session set successfully after login");
            
            String profileFile = user.getProfileFile();
            nextPage="home/UserHome?faces-redirect=true";
            } catch (RuntimeException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            return null;
        }
        
        
        
        return nextPage;
    }
   
    
}
