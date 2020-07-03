/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.UserBeanLocal;

/**
 *
 * @author root
 */
@Named(value ="accessResetMBean" )
@ViewScoped
public class AccessResetMBean implements Serializable {
    
    final static Logger LOGGER=Logger.getLogger(AccessResetMBean.class.getName());
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    private String email;
    
    
    
    public String dispatchResetLink(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        if (email.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("emailNotEntered"),rb.getString("emailNotEntered")));
            return null;
        }else{
            boolean status=userBeanLocal.isEmailRegistered(email);
            if (!status){
                FacesContext.getCurrentInstance().addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("emailNotRegistered"),rb.getString("emailNotRegistered")));
                return null;
            }else{
                userBeanLocal.dispatchAccessReset(email);
                FacesContext.getCurrentInstance().addMessage("email",new FacesMessage(FacesMessage.SEVERITY_INFO,rb.getString("requestSent"),rb.getString("requestSent")));
                return null;
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
    
}
