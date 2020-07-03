/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
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
@Named(value = "accessConfirmMBean")
@ViewScoped
public class AccessConfirmMBean implements Serializable {
    
final static Logger LOGGER=Logger.getLogger(AccessConfirmMBean.class.getName());
    
    
    
    @Inject
    UserBeanLocal userBeanLocal;
    
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
    @PostConstruct
    public void init(){
        LOGGER.info("View initialised for Access");
        loadAccess();
    }

    private void loadAccess() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = request.getParameter("id");
        LOGGER.log(Level.INFO, "Access Confirm request received from {0}", email);
        user=userBeanLocal.getUserByEmail(email);
        if (user!=null){
            user.setEmailOrg(email);
            LOGGER.log(Level.INFO, "Access Initialised {0}", user.getEmail());
        }
    }
    
    public String processForm(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        String toReturn=null;
        String password=user.getPassword();
        String passwordConfirm=user.getPasswordConfirm();
        if (password.trim().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("pwd1",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("noPassword"),rb.getString("noPassword")));
        }else{
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
            toReturn =null;
        }else{
            user=userBeanLocal.createAccess(user);
            toReturn="/home/UserWelcome?faces-redirect=true";
        }
        LOGGER.log(Level.INFO, "toReturn is :{0}", toReturn);
        return toReturn;
    }
    
}
