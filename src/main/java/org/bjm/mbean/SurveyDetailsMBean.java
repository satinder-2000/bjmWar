/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.servlet.http.HttpSession;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.model.Survey;
import org.bjm.model.SurveyVote;
import org.bjm.model.User;
import org.bjm.model.VoteType;
import org.bjm.util.BJMConstants;
import org.bjm.util.ImageVO;


/**
 *
 * @author root
 */
@Named(value = "surveyDetailsMBean")
@ViewScoped
public class SurveyDetailsMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(SurveyDetailsMBean.class.getName());
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;
    
    private Survey survey;
    
    private SurveyVote surveyVote;
    private int votesTillDate;
    
    private String agreePct;
    private String disagreePct;
    private String undecidedPct;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String surveyIdStr=request.getParameter("surveyId");
        int surveyId=Integer.parseInt(surveyIdStr);
        survey=surveyBeanLocal.getSurveyById(surveyId);
        HttpSession session=request.getSession();
        User user =(User)session.getAttribute(BJMConstants.USER);
        if (user!=null){//Good to allow read only view as well. 
            String imgType = user.getProfileFile().substring(user.getProfileFile().indexOf('.') + 1);
            ImageVO imageVO = new ImageVO(imgType, user.getImage());
            session.setAttribute(BJMConstants.TEMP_IMAGE, imageVO);
        }
        String creatorImgType=survey.getUser().getProfileFile().substring(survey.getUser().getProfileFile().indexOf('.') + 1);
        ImageVO creatorImgVO=new ImageVO(creatorImgType,survey.getUser().getImage());
        session.setAttribute(BJMConstants.SURVEY_CREATOR, creatorImgVO);
        
        surveyVote=new SurveyVote();
        agreePct =""+0.0;
        disagreePct =""+0.0;
        undecidedPct=""+0.0;
        LOGGER.log(Level.INFO, "Survey loaded in MBean with id : {0}", survey.getId());
        
    }
    
    public String postSurveyVote() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(BJMConstants.USER);
        //First check if the user has already casted a Vote
        SurveyVote voteDB = surveyBeanLocal.getSurveyVoteByUser(survey.getId(), user.getId());
        if (voteDB != null) {
            context.addMessage("surveyVote", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("multiVote"), rb.getString("multiVote")));
            return null;
        } else {
            String comment = surveyVote.getComment();
            if (comment.isEmpty()) {
                context.addMessage("surveyVote", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("noVoteComment"), rb.getString("noVoteComment")));
                return null;
            } else if (comment.length() < 15 || comment.length() > 450) {
                context.addMessage("surveyVote", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("voteCommentLength"), rb.getString("voteCommentLength")));
                return null;
            } else {
                surveyVote.setUser(user);
                surveyVote.setVoteType(surveyVote.getVoteType());
                survey = surveyBeanLocal.addSurveyVote(survey, surveyVote);
                context.addMessage("surveyVote", new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("voteAdded"), rb.getString("voteAdded")));
                surveyVote = new SurveyVote();
                return null;
            }
        }
    }
    
    public List<SurveyVote> getOtherSurveyVotes(){
        List<SurveyVote> surveyVotes=surveyBeanLocal.getSurveyVotes(survey.getId());
        if (surveyVotes.size()>0){
            votesTillDate=surveyVotes.size();
            Map<Integer,ImageVO> surveyVotesImageMap=new HashMap<>();
            int agreeCt = 0;
            int disagreeCt = 0;
            int undecidedCt = 0;
            for (SurveyVote sv : surveyVotes) {
                if (sv.getVoteType() == VoteType.AGREE) {
                    agreeCt++;
                } else if (sv.getVoteType() == VoteType.DISAGREE) {
                    disagreeCt++;
                } else if (sv.getVoteType() == VoteType.UNDECIDED) {
                    undecidedCt++;
                }
                User voter = sv.getUser();
                String imgType = voter.getProfileFile().substring(voter.getProfileFile().indexOf('.') + 1);
                ImageVO imageVO = new ImageVO(imgType, voter.getImage());
                surveyVotesImageMap.put(sv.getId(), imageVO);
            }
            //Percentages now
            int total = agreeCt + disagreeCt + undecidedCt;
            double agree = agreeCt * 100 / total;
            double disagree = disagreeCt * 100 / total;
            double undecided = undecidedCt * 100 / total;
            agreePct = "" + agree;
            disagreePct = "" + disagree;
            undecidedPct = "" + undecided;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            session.setAttribute(BJMConstants.SURVEY_VOTE_IMAGE_MAP, surveyVotesImageMap);
            
        }else{
            votesTillDate=0;
        }
        return surveyVotes;
        
       
        
        
        
    }

    public Survey getSurvey() {
        return survey;
    }

    public SurveyVote getSurveyVote() {
        return surveyVote;
    }

    public void setSurveyVote(SurveyVote surveyVote) {
        this.surveyVote = surveyVote;
    }

    

    public String getAgreePct() {
        return agreePct;
    }

    public void setAgreePct(String agreePct) {
        this.agreePct = agreePct;
    }

    public String getDisagreePct() {
        return disagreePct;
    }

    public void setDisagreePct(String disagreePct) {
        this.disagreePct = disagreePct;
    }

    public String getUndecidedPct() {
        return undecidedPct;
    }

    public void setUndecidedPct(String undecidedPct) {
        this.undecidedPct = undecidedPct;
    }

    public int getVotesTillDate() {
        return votesTillDate;
    }

    public void setVotesTillDate(int votesTillDate) {
        this.votesTillDate = votesTillDate;
    }

    

    
    
    
    
}
