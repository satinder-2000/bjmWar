/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ElectoralRefDataLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@ViewScoped
@Named(value = "vidhanSabhaElectoralMBean")
public class VidhanSabhaElectoralMBean implements Serializable {
    
     private static Logger LOGGER=Logger.getLogger(VidhanSabhaElectoralMBean.class.getName());
    
    @Inject
    private ElectoralRefDataLocal erdl;
    
    private User user;
    
    private List<String> constituencies;
    private String constituency;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        String stateCode=user.getStateCode();
        constituencies=erdl.getVidhanSabhas(stateCode);
        if (constituencies==null){//No data found with this State Code. Could be a UT such as Chandigarh
            constituencies=new ArrayList<>();
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
            constituencies.add(rb.getString("noVSConstituency"));
        }
        LOGGER.log(Level.INFO, "VidhanSabha contituenties loaded for State: {0}", stateCode);
    }

    public List<String> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<String> constituencies) {
        this.constituencies = constituencies;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }
    
    
    
}
