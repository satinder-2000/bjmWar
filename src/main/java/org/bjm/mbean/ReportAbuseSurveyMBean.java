/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.model.SurveyAbuse;
import org.bjm.model.SurveyVote;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "reportAbuseSurveyMBean")
@ViewScoped
public class ReportAbuseSurveyMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ReportAbuseSurveyMBean.class.getName());
    
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;
    
    private int voteId;
    
    private SurveyAbuse surveyAbuse;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String voteIdStr=request.getParameter("voteId");
        voteId=Integer.parseInt(voteIdStr);
        surveyAbuse=new SurveyAbuse();
    }
    
    
    public String reportAbuse(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        surveyAbuse.setReportedBy(user);
        SurveyVote sv=surveyBeanLocal.getSurveyVoteById(voteId); 
        surveyAbuse.setSurveyVote(sv);
        surveyAbuse=surveyBeanLocal.addSurveyAbuse(surveyAbuse);
        LOGGER.info("Survey Abuse added with ID - "+surveyAbuse.getId());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanks. Abuse reported.", "Thanks. Abuse reported."));
        surveyAbuse=new SurveyAbuse();
        return null;
    }

    public SurveyAbuse getSurveyAbuse() {
        return surveyAbuse;
    }

    public void setSurveyAbuse(SurveyAbuse surveyAbuse) {
        this.surveyAbuse = surveyAbuse;
    }
    
    
    
}
