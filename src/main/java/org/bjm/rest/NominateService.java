/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.rest;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.bjm.ejb.ElectoralBeanLocal;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Path("nominate")
public class NominateService {
    
    private static final Logger LOGGER=Logger.getLogger(NominateService.class.getName());
    
    @Inject
    private ElectoralBeanLocal ebl;
    
    @GET
    @Path("vs")
    public String getVidhanSabhaConstituencies(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        String stateCd=user.getStateCode();
        List<String> constituenciesV=ebl.getVSConstituencies(stateCd);
        StringBuffer sb=new StringBuffer('[');
        for (int i=0;i<constituenciesV.size();i++){
            if (i<(constituenciesV.size()-1)){//last value
               sb.append("\""+constituenciesV.get(i)+"\"");
            }else{
               sb.append("\""+constituenciesV.get(i)+"\","); 
            }
        }
        String toReturn=sb.toString();
        LOGGER.log(Level.INFO, "Returning: {0}", toReturn);
        return toReturn;
    }
    
    @GET
    @Path("ls")
    public String getLokSabhaConstituencies(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        String stateCd=user.getStateCode();
        List<String> constituenciesL=ebl.getLSConstituencies(stateCd);
        StringBuffer sb=new StringBuffer('[');
        for (int i=0;i<constituenciesL.size();i++){
            if (i<(constituenciesL.size()-1)){//last value
               sb.append("\""+constituenciesL.get(i)+"\"");
            }else{
               sb.append("\""+constituenciesL.get(i)+"\","); 
            }
        }
        String toReturn=sb.toString();
        LOGGER.log(Level.INFO, "Returning: {0}", toReturn);
        return toReturn;
    }
    
}
