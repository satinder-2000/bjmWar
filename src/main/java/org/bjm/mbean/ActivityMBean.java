/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.bjm.model.Activity;
import org.bjm.model.ActivityType;
import org.bjm.ejb.ActivityBeanLocal;

/**
 *
 * @author root
 */
@Named(value = "activityMBean")
@ApplicationScoped
@Startup
public class ActivityMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ActivityMBean.class.getName());
    
    @Inject
    private ActivityBeanLocal activityBeanLocal;
    
    public List<Activity> getRecentActivities(){
        List<Activity> activities=activityBeanLocal.getRecentActivities();
        //LOGGER.info("Activities extracted :"+activities.size());
        return activities;
    }
    
    @Deprecated
    public String showActivity(){
        String toReturn=null;
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String activityIdStr=request.getParameter("activityId");
        int activityId=Integer.parseInt(activityIdStr);
        String activityType=request.getParameter("activityType");
        ActivityType actType=ActivityType.valueOf(activityType);
        if (actType==ActivityType.FORUM_CREATED){
            toReturn= request.getContextPath()+"/forum/ForumDetails.xhtml?faces-redirect=true&forumId="+activityId;
        }else if(actType==ActivityType.SURVEY_CREATED){
            toReturn= request.getContextPath()+"/survey/SurveyDetails.xhtml?faces-redirect=true&surveyId="+activityId;
        }
        return toReturn;
    }
    
    
}
