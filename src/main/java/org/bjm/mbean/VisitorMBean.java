package org.bjm.mbean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bjm.ejb.VisitorBeanLocal;
import org.bjm.model.Visitor;

/**
 *
 * @author root
 */
@Named(value = "visitorMBean")
@RequestScoped
public class VisitorMBean {
    
    private static final Logger LOGGER=Logger.getLogger(VisitorMBean.class.getName());
    
    @Inject
    private VisitorBeanLocal visitorBeanLocal;
    
   private boolean rememberMe;
    
    @PostConstruct
    public void init(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        String ipAddress = request.getRemoteAddr();
        LOGGER.log(Level.INFO, "ipAddress is {0}", ipAddress);
        boolean ipSaved=visitorBeanLocal.performIPCheckIfSaved(ipAddress);
        if (ipSaved){
            LOGGER.info("And found. Redirecting to login");
            HttpServletResponse response=(HttpServletResponse) ctx.getResponse();
            try {
                response.sendRedirect("/Home.xhtml?faces-redirect=true");
            } catch (IOException ex) {
                Logger.getLogger(VisitorMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public String saveVisitor(){
        
        if (rememberMe){//If Visitor checks the Box, store the IP Address in the Database. The home page will not be generated and control will go to the index.xhtml.
            Visitor visitor = new Visitor();
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
            String ipAddress = request.getRemoteAddr();
            visitor.setIpAddress(ipAddress);
            visitor.setTime(LocalDateTime.now());
            LOGGER.info("ipAddress is " + ipAddress);
            visitorBeanLocal.saveVisitor(visitor);
        }
        return "/Home?faces-redirect=true";
        
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
