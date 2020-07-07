/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.Survey;
import org.bjm.model.SurveyAbuse;
import org.bjm.model.SurveyVote;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Local
public interface SurveyBeanLocal {

    public Survey createSurvey(Survey survey, User user);

    public List<Survey> getSurveys();
    
    public List<Survey> getSurveysByUser(int userId);

    public Survey getSurveyById(int surveyId);

    public Survey addSurveyVote(Survey survey, SurveyVote surveyVote);
    
    public Survey updateSurveyVote(Survey survey, SurveyVote surveyVote);

    public List<SurveyVote> getSurveyVotes(int surveyId);
    
    public SurveyAbuse addSurveyAbuse(SurveyAbuse surveyAbuse);

    public SurveyVote getSurveyVoteById(int voteId);
    
    public SurveyVote getSurveyVoteByUser(int surveyId, int userId); 
}
