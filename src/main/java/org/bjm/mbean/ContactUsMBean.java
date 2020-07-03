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
    
    
    private String subject;
    
    private String details;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        LOGGER.info("ContactUsMBean - User loaded");
        
    }
    
    public String sendRequest(){
        
        ContactVO vo=new ContactVO(user.getEmail(),subject, details);
        miscellaneousServicesBeanLocal.sendContactUsMessage(vo);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,rb.getString("requestSent"),rb.getString("requestSent")));
        LOGGER.info("Message Sent need implementation");
        return null;
    }
    

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    
    
}
