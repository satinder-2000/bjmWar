/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.VisitorBeanLocal;
import org.bjm.model.Visitor;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "welcomeMBean")
@ViewScoped
public class WelcomeMBean implements Serializable {
    
    
    @Inject
    private VisitorBeanLocal visitorBeanLocal;
    
    
    private Visitor visitor;
    
    @PostConstruct
    public void init(){
        //Get Visitor from Session
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpSession session=request.getSession();
        visitor=(Visitor)session.getAttribute(BJMConstants.VISITOR);
        //Load RB per lang preference- MAGIC
        Locale locale=(Locale)session.getAttribute("locale");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        ResourceBundle rb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{msg}", ResourceBundle.class);
    }
    
    
    public String processUpdate(){
        
        //read rememberMe
        if (visitor.isRememberMe()){
            visitor.setTime(LocalDateTime.now());
            visitor=visitorBeanLocal.updateVisitor(visitor);
        }
        // redirect to Site Home??
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpSession session=request.getSession(true);
        session.removeAttribute(BJMConstants.VISITOR);
        return "home?faces-redirect=true";
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
    
    
    
}
