/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.StringWriter;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.bjm.model.EmailTemplateType;
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
@Singleton
public class EmailerBean implements EmailerBeanLocal {
    
    static final Logger LOGGER = Logger.getLogger(EmailerBean.class.getName());

    @Resource(name = "mail/bjm")
    Session session;
    
    @Resource(name = "sender")
    String sender;
    
    @Resource(name = "accessConfirmURI")
    String accessConfirmURI;
    
    @Resource(name = "WebURI")
    String webURI;
    
    @Resource(name= "welcomeURI")
    String welcomeURI;
    
    @Resource(name = "surveyURI")
    String surveyURI;
    
    @Resource(name = "forumURI")
    String forumURI;
    
    @Resource(name = "passwordResetURI")
    String passwordResetURI;
    
    
    
    @Inject
    ReferenceDataBeanLocal referenceDataBeanLocal;
    
    Map<EmailTemplateType, String> templatesMap;
    
    @PostConstruct
    public void init(){
        LOGGER.info(sender+ " is Sender and accessConfirmURI is "+accessConfirmURI);
        templatesMap = referenceDataBeanLocal.getEmailTemplatesMap();
    }

    @Override
    public void sendUserRegConfirmEmail(User user) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Please confirm Email and set Password");

            String htmlText = templatesMap.get(EmailTemplateType.USER_REGISTRATION);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("userName", user.getFirstname().concat(" ").concat(user.getLastname()));
            vc.put("userEmail", user.getEmail());
            vc.put("accessConfirmURI", webURI+accessConfirmURI);
            ve.evaluate(vc, sw, EmailTemplateType.USER_REGISTRATION.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendAccessConfirmEmail(String email) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Access is confirmed");

            String htmlText = templatesMap.get(EmailTemplateType.ACCESS_CONFIRM);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("welcomeURI", webURI+welcomeURI);
            vc.put("userEmail", email);
            ve.evaluate(vc, sw, EmailTemplateType.ACCESS_CONFIRM.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendSurveyCreatedMail(Survey survey) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(survey.getUser().getEmail()));
            message.setSubject("Survey created successfully");

            String htmlText = templatesMap.get(EmailTemplateType.SURVEY_CREATED);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            String userName=survey.getUser().getFirstname()+" "+survey.getUser().getLastname();
            vc.put("userName",userName);
            vc.put("surveyURI", webURI+surveyURI);
            vc.put("surveyId", survey.getId());
            vc.put("SurveyTitle", survey.getTitle());
            ve.evaluate(vc, sw, EmailTemplateType.SURVEY_CREATED.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
        
    }

    @Override
    public void sendForumCreatedMail(Forum forum) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(forum.getUser().getEmail()));
            message.setSubject("Forum created successfully");
            User user=forum.getUser();
            String name=user.getFirstname()+" "+user.getLastname();

            String htmlText = templatesMap.get(EmailTemplateType.FORUM_CREATED);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("userName", name);
            vc.put("forumURI", webURI+forumURI);
            vc.put("forumId", forum.getId());
            vc.put("ForumTitle", forum.getTitle());
            ve.evaluate(vc, sw, EmailTemplateType.FORUM_CREATED.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Your request for Password reset.");

            String htmlText = templatesMap.get(EmailTemplateType.PASSWORD_RESET);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("passwordResetURI", webURI+passwordResetURI);
            String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());
            vc.put("emailId", encodedEmail);
            ve.evaluate(vc, sw, EmailTemplateType.PASSWORD_RESET.toString(), htmlText);
            
            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendContactUsEmailToAdmin(ContactVO contactVO) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sender));//Sending to the Admin
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(contactVO.getSenderEmail()));
            message.setSubject(contactVO.getSubject());
            String htmlText = templatesMap.get(EmailTemplateType.CONTACT_US);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("admin", sender);
            vc.put("userEmail", contactVO.getSenderEmail());
            vc.put("subject", contactVO.getSubject());
            vc.put("message", contactVO.getMessage());
            ve.evaluate(vc, sw, EmailTemplateType.CONTACT_US.toString(), htmlText);
            message.setContent(sw.getBuffer().toString(), "text/html");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
            
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendNominateNewLSCandidateEmail(User user, LsCandidate lc) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Candidate Nomination - Lok Sabha");

            String htmlText = templatesMap.get(EmailTemplateType.LS_NOMINATE);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("userName", user.getFirstname().concat(" ").concat(user.getLastname()));
            vc.put("candidateName", lc.getName());
            vc.put("constituency", lc.getLokSabha().getConstituency());
            vc.put("welcomeURI", webURI);
            ve.evaluate(vc, sw, EmailTemplateType.LS_NOMINATE.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendAddNominationLSCandidateEmail(User user, LsCandidate lc) {
        sendNominateNewLSCandidateEmail(user,lc);
    }

    @Override
    public void sendnominateNewVSCandidateEmail(User user, VsCandidate lc) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Candidate Nomination - Vidhan Sabha");

            String htmlText = templatesMap.get(EmailTemplateType.VS_NOMINATE);
            VelocityEngine ve = new VelocityEngine();
            StringWriter sw = new StringWriter();
            VelocityContext vc = new VelocityContext();
            vc.put("userName", user.getFirstname().concat(" ").concat(user.getLastname()));
            vc.put("candidateName", lc.getName());
            vc.put("constituency", lc.getVidhanSabha().getConstituency());
            vc.put("welcomeURI", webURI);
            ve.evaluate(vc, sw, EmailTemplateType.VS_NOMINATE.toString(), htmlText);

            message.setContent(sw.getBuffer().toString(), "text/html");

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendAddNominationVSCandidateEmail(User user, VsCandidate lc) {
        sendnominateNewVSCandidateEmail(user, lc);
    }
}
