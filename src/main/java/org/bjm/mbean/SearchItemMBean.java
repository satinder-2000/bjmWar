/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author root
 */
@Named(value = "searchItemMBean")
public class SearchItemMBean {
    
    private static final Logger LOGGER=Logger.getLogger(SearchItemMBean.class.getName());
    
    public String generateDetails(){
        String toReturn=null;
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String itemIdStr=request.getParameter("itemId");
        if (itemIdStr.startsWith("F")){//redirect to Forum Details
            String forumId=itemIdStr.substring(1);
            toReturn=request.getContextPath().concat("/forum/ForumDetails.xhtml?faces-redirect=true&forumId=").concat(forumId);
        }else if (itemIdStr.startsWith("S")){//redirect to Survey Details
            String surveyId=itemIdStr.substring(1);
            toReturn=request.getContextPath().concat("/survey/SurveyDetails.xhtml?faces-redirect=true&surveyId=").concat(surveyId);
        }else {//Not found page. Though it will never happen
            toReturn=request.getContextPath().concat("/error404.xhtml?faces-redirect=true");
        }
        return toReturn;
    }
    
}
