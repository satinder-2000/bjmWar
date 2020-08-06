/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.IOException;
import java.io.Serializable;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.MiscellaneousServicesBeanLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.vo.ContactVO;

/**
 *
 * @author root
 */
@Named(value = "contactUsMBean")
@ViewScoped
public class ContactUsMBean implements Serializable {
    
    private static final Logger LOGGER= Logger.getLogger(ContactUsMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal miscellaneousServicesBeanLocal;
    
    private User user;
    
    private String userEmail;
    
    
    private String subject;
    
    private String message;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        if (session.getAttribute(BJMConstants.USER)!=null){
           user=(User)session.getAttribute(BJMConstants.USER);
           userEmail=user.getEmail();
           LOGGER.info("ContactUsMBean - User loaded"); 
        }
        
        
    }
    
    public String sendRequest(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        boolean formValid=true;
        //One. Validate Email
        if (userEmail.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailRequired"), rb.getString("emailRequired")));
            formValid=false;
        }else{//Email validations
            Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
            Matcher m = p.matcher(userEmail);
            if (!m.find()) {
                FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailInvalid"), rb.getString("emailInvalid")));
                formValid=false;
            }
        }
        //Two, validate Subject
        if (subject.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("subject", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("contactSubjectNotValid"), rb.getString("contactSubjectNotValid")));
            formValid=false;
        }
        //Three, validate Details
        if (message.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("contactMsgNotValid"), rb.getString("contactMsgNotValid")));
            formValid=false;
        }
        
        if (formValid){//Submit Data
            ContactVO vo = new ContactVO(userEmail, subject, message);
            miscellaneousServicesBeanLocal.sendContactUsMessage(vo);

            FacesContext.getCurrentInstance().addMessage("",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("requestSent"), rb.getString("requestSent")));
            LOGGER.info("Message Sent");
        }
        return null;
    }
    

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    
    
}
