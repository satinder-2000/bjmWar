/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.VisitorBeanLocal;
import org.bjm.model.Visitor;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "indexMBean")
@SessionScoped
public class IndexMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(IndexMBean.class.getName());
    
    @Inject
    private VisitorBeanLocal visitorBeanLocal;
    
    private Visitor visitor;
    
    
    @PostConstruct
    public void init(){
        //Get IP
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        String ipAddress = request.getRemoteAddr();
        LOGGER.log(Level.INFO, "ipAddress is {0}", ipAddress);
        //Visitor found?
        visitor=visitorBeanLocal.getVisitor(ipAddress);
        if (visitor==null){//No > create new and put Timestamp
            visitor=new Visitor();
            visitor.setTime(LocalDateTime.now());
            visitor.setIpAddress(ipAddress);
        }else{//Get lang/locale for Visitor and set in session //yes > put lang in session > redirect to Welcome.xhtml
            String lang=visitor.getLang();
            HttpSession session=request.getSession(true);
            if (lang.equals("en")) {
                Locale locale = new Locale("en", "GB");
                session.setAttribute("locale", locale);
            } else if (lang.equals("hi")) {
                Locale locale = new Locale("hi", "IN");
                session.setAttribute("locale", locale);
            }
            session.setAttribute(BJMConstants.VISITOR, visitor);
            HttpServletResponse response=(HttpServletResponse) ctx.getResponse();
            RequestDispatcher rd=request.getRequestDispatcher("Welcome.xhtml");
            try {
                rd.forward(request, response);
            } catch (ServletException ex) {
                Logger.getLogger(IndexMBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IndexMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
        
    public String saveLocale(){
        //TEMP MEASURE
        init();
        LOGGER.warning("DO NOT CALL INIT METHOD FROM THE BUSINESS METHOD - IndexMBean");
        
        //TEMP MEASURE
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpSession session=request.getSession(true);
        String lang=request.getParameter("lang");
        Locale locale=null;
        if (lang.equals("en")){
            locale=new Locale("en","GB");
        }else if (lang.equals("hi")){
            locale=new Locale("hi","IN");
        }
        session.setAttribute("locale", locale);
        visitor.setLang(lang);
        visitor.setRememberMe(false);
        visitorBeanLocal.saveVisitor(visitor);
        session.setAttribute(BJMConstants.VISITOR, visitor);
        //FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        //ResourceBundle rb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{msg}", ResourceBundle.class);
        return "Welcome?faces-redirect=true";
    }    
        
        
        
    
    
}
