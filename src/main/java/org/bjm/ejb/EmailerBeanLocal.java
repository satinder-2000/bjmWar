/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import javax.ejb.Local;
import org.bjm.model.Forum;
import org.bjm.model.LsCandidate;
import org.bjm.model.VsCandidate;
import org.bjm.model.Survey;
import org.bjm.model.User;
import org.bjm.vo.ContactVO;

/**
 *
 * @author root
 */
@Local
public interface EmailerBeanLocal {
    
    public void sendUserRegConfirmEmail(User user, String lang);
    
    public void sendAccessConfirmEmail(String email, String lang);

    public void sendSurveyCreatedMail(Survey survey, String lang);
    
    public void sendForumCreatedMail(Forum forum, String lang);
    
    public void sendPasswordResetEmail(String email, String lang);
    
    public void sendContactUsEmailToAdmin(ContactVO contactVO, String lang);
    
    public void sendNominateNewLSCandidateEmail(User user, LsCandidate lc, String lang);
    
    public void sendAddNominationLSCandidateEmail(User user, LsCandidate lc, String lang);
    
    public void sendnominateNewVSCandidateEmail(User user, VsCandidate lc, String lang);
    
    public void sendAddNominationVSCandidateEmail(User user, VsCandidate lc, String lang);
    
}
