/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Activity;
import org.bjm.model.ActivityType;
import org.bjm.model.Survey;
import org.bjm.model.SurveyAbuse;
import org.bjm.model.SurveyVote;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Stateless
public class SurveyBean implements SurveyBeanLocal {
    
    private static final Logger LOGGER=Logger.getLogger(SurveyBean.class.getName());
    
    @Inject
    private EmailerBeanLocal emailerBeanLocal;
    
    @Inject
    private ActivityBeanLocal activityBeanLocal;
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public Survey createSurvey(Survey survey, User user) {
        survey.setDated(LocalDateTime.now());
        survey.getUser().getUserSurveys().add(survey);
        em.persist(survey);
        em.merge(survey.getUser());
        em.flush();
        LOGGER.log(Level.INFO, "New Survey created with ID: {0}", survey.getId());
        emailerBeanLocal.sendSurveyCreatedMail(survey);
        Activity activity=new Activity();
        activity.setUserId(user.getId());
        activity.setActivityId(survey.getId());
        activity.setActivityType(ActivityType.SURVEY_CREATED);
        activity.setDated(LocalDateTime.now());
        activity.setDescription("Survey created :"+survey.getTitle());
        activityBeanLocal.logActivity(activity);
        return survey;
        
        
        
    }

    @Override
    public List<Survey> getSurveys() {
        TypedQuery<Survey> tQ=em.createQuery("select s from Survey s order by s.dated desc", Survey.class);
        return tQ.getResultList();
    }

    @Override
    public Survey getSurveyById(int surveyId) {
        return em.find(Survey.class, surveyId);
    }

    @Override
    public Survey addSurveyVote(Survey survey, SurveyVote surveyVote) {
        survey.getSurveyVotes().add(surveyVote);
        surveyVote.setSurvey(survey);
        surveyVote.setDated(LocalDateTime.now());
        em.persist(surveyVote);
        Survey toReturn=em.merge(survey);
        em.flush();
        LOGGER.info("Survey Vote persisted with ID : "+surveyVote.getId()+" and now Survey "+survey.getId()+"has total votes of "+survey.getSurveyVotes().size());
        return toReturn;
    }
    
    @Override
    public Survey updateSurveyVote(Survey survey, SurveyVote surveyVote) {
        surveyVote.setSurvey(survey);
        surveyVote.setDated(LocalDateTime.now());
        em.merge(surveyVote);
        //em.merge(survey);
        Survey toReturn=em.merge(survey);
        em.flush();
        LOGGER.info("Survey Vote updated with ID : "+surveyVote.getId()+" and now Survey "+survey.getId()+"has total votes of "+survey.getSurveyVotes().size());
        return toReturn;
    }

    @Override
    public List<SurveyVote> getSurveyVotes(int surveyId) {
        TypedQuery<SurveyVote> tQ=em.createQuery("select sv from SurveyVote sv where sv.survey.id=?1 order by sv.dated desc", SurveyVote.class);
        tQ.setParameter(1, surveyId);
        return tQ.getResultList();
    }

    @Override
    public List<Survey> getSurveysByUser(int userId) {
        TypedQuery<Survey> tQ=em.createQuery("select s from Survey s where s.user.id=?1", Survey.class);
        tQ.setParameter(1, userId);
        return tQ.getResultList();
    }

    @Override
    public SurveyAbuse addSurveyAbuse(SurveyAbuse surveyAbuse) {
        surveyAbuse.setReportedOn(LocalDateTime.now());
        em.persist(surveyAbuse);
        em.flush();
        return surveyAbuse;
    }

    @Override
    public SurveyVote getSurveyVoteById(int voteId) {
        return em.find(SurveyVote.class, voteId);
    }

    @Override
    public SurveyVote getSurveyVoteByUser(int surveyId, int userId) {
        SurveyVote toReturn=null;
        //Survey is related to SurveyVote by lazy loading. Need to fix that first
        
        TypedQuery<SurveyVote> tQ=em.createQuery("select sv from SurveyVote sv where sv.survey.id=?1 and sv.user.id=?2", SurveyVote.class);
        tQ.setParameter(1, surveyId);
        tQ.setParameter(2, userId);
        try{
          List<SurveyVote> votes=tQ.getResultList();
          if(votes.size()>0) 
              toReturn= votes.get(0);
        }catch (NoResultException ex){
            //Doesn't matter
        }
        return toReturn;
    }

    

    
}
