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
    
    public void sendUserRegConfirmEmail(User user);
    
    public void sendAccessConfirmEmail(String email);

    public void sendSurveyCreatedMail(Survey survey);
    
    public void sendForumCreatedMail(Forum forum);
    
    public void sendPasswordResetEmail(String email);
    
    public void sendContactUsEmailToAdmin(ContactVO contactVO);
    
    public void sendNominateNewLSCandidateEmail(User user, LsCandidate lc);
    
    public void sendAddNominationLSCandidateEmail(User user, LsCandidate lc);
    
    public void sendnominateNewVSCandidateEmail(User user, VsCandidate lc);
    
    public void sendAddNominationVSCandidateEmail(User user, VsCandidate lc);
    
}
