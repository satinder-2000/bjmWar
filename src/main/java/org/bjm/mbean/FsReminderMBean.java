/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Named(value = "fsReminderMBean")
@ViewScoped
public class FsReminderMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(FsReminderMBean.class.getName());
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    private User user;
    
    
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        /*String emailEncoded=request.getParameter("id");
        byte[] decodedBytes = Base64.getDecoder().decode(emailEncoded);
        String email= new String(decodedBytes);
        se64.getDecoder().decode(emailEncoded);*/
        //TEMP CODE
        LOGGER.warning("REMOVE TEMP CODE FROM FsReminderMBean");
        String email= request.getParameter("id");
        user=userBeanLocal.getUserByEmail(email);
    }
    
    public String updateFSReminder(){
        int value=user.getFsReminder();
        LOGGER.log(Level.INFO, "updateFSReminder value is :{0}", value);
        return null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
    
    
    
    
    
}
