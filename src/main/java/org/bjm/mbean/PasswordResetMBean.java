/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "passwordResetMBean")
@ViewScoped
public class PasswordResetMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(PasswordResetMBean.class.getName());
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    private User user;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String emailEncoded=request.getParameter("emailId");
        byte[] decodedBytes = Base64.getDecoder().decode(emailEncoded);
        String email= new String(decodedBytes);
        user=userBeanLocal.getUserByEmail(email);
    }
    
    public String resetPassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        String password=user.getPassword();
        String passwordConfirm=user.getPasswordConfirm();
        if (password.trim().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("pwd1",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("noPassword"),rb.getString("noPassword")));
        }
        if (passwordConfirm.trim().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("pwd2",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("noPasswordConfirm"),rb.getString("noPasswordConfirm")));
        }
        
        else{
            //First, RegEx the password
            Pattern pCdIn=Pattern.compile(BJMConstants.PW_REGEX);
            Matcher mPCdIn=pCdIn.matcher(password);
            if (!mPCdIn.find()){
                FacesContext.getCurrentInstance().addMessage("pwd1",new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("invalidPassword"),rb.getString("invalidPassword")));  
            }else{//compare the password now
                if(!password.equals(passwordConfirm)){
                    FacesContext.getCurrentInstance().addMessage("pwd2",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("passwordsMisMatch"),rb.getString("passwordsMisMatch")));
                }
                
            }
        }
        List<FacesMessage> msgs= FacesContext.getCurrentInstance().getMessageList();
        if (msgs!=null && msgs.size()>0){
            return null;
        }else{
            user=userBeanLocal.changePassword(user);
            FacesContext.getCurrentInstance().addMessage("",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,rb.getString("passwordChanged"),rb.getString("passwordChanged")));
            return null;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
    
    
    
    
    
    
}
