/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
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
import org.bjm.model.EmailMessage;
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
    
    @Resource(name="protocol")
    String protocol;
    
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
    
    HashMap<String, List<EmailMessage>> emailMessages_hi;
    HashMap<String, List<EmailMessage>> emailMessages_en;
    
    @PostConstruct
    public void init(){
        LOGGER.info(sender+ " is Sender and accessConfirmURI is "+accessConfirmURI);
        templatesMap = referenceDataBeanLocal.getEmailTemplatesMap();
        emailMessages_hi=referenceDataBeanLocal.getEmailMessages("hi");
        emailMessages_en=referenceDataBeanLocal.getEmailMessages("en");
    }

    @Override
    public void sendUserRegConfirmEmail(User user, String lang) {
        List<EmailMessage> regMessages=null;
        if (lang.equals("hi")){
            regMessages=emailMessages_hi.get(EmailTemplateType.USER_REGISTRATION.name());
        }else{
            regMessages=emailMessages_en.get(EmailTemplateType.USER_REGISTRATION.name());
        }
        StringBuilder sb=new StringBuilder(user.getEmail()+",\n");
            Map<String, String> map=new HashMap();
            for (EmailMessage msg:regMessages){
                map.put(msg.getMessageTitle(), msg.getText());
            }
            //IN the email we need values in the following order
            //registrationUser, successfullyReg, setPassword, createAccess
            sb.append(map.get("registrationUser")).append("\n");
            sb.append(map.get("successfullyReg")).append("\n");
            sb.append(map.get("setPassword")).append("\n");
            sb.append(protocol).append(webURI).append(accessConfirmURI).append(user.getEmail());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(map.get("subject"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendAccessConfirmEmail(String email, String lang) {
        List<EmailMessage> accessMessages=null;
        if (lang.equals("hi")){
            accessMessages=emailMessages_hi.get(EmailTemplateType.ACCESS_CONFIRM.name());
        }else{
            accessMessages=emailMessages_en.get(EmailTemplateType.ACCESS_CONFIRM.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(email).append("\n");
        sb.append(map.get("thankYou")).append("\n");
        sb.append(map.get("welcome")).append("\n");
        sb.append(protocol).append(webURI).append(welcomeURI);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("एक्सेस की पुष्");
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendSurveyCreatedMail(Survey survey, String lang) {
        List<EmailMessage> surveyMessages=null;
        if (lang.equals("hi")){
            surveyMessages=emailMessages_hi.get(EmailTemplateType.SURVEY_CREATED.name());
        }else{
            surveyMessages=emailMessages_en.get(EmailTemplateType.SURVEY_CREATED.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:surveyMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("surveyCreated")).append("\n");
        sb.append(survey.getUser().getEmail()).append(",\n");
        sb.append(map.get("successCreate")).append("\n");
        sb.append(map.get("viewLink")).append("\n");
        sb.append(protocol).append(webURI).append(surveyURI).append(survey.getId());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(survey.getUser().getEmail()));
            message.setSubject(map.get("surveyCreated"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
        
    }

    @Override
    public void sendForumCreatedMail(Forum forum, String lang) {
       
        List<EmailMessage> surveyMessages=null;
        if (lang.equals("hi")){
            surveyMessages=emailMessages_hi.get(EmailTemplateType.SURVEY_CREATED.name());
        }else{
            surveyMessages=emailMessages_en.get(EmailTemplateType.SURVEY_CREATED.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:surveyMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("forumCreated")).append("\n");
        sb.append(forum.getUser().getEmail()).append(",\n");
        sb.append(map.get("successCreate")).append("\n");
        sb.append(map.get("viewLink")).append("\n");
        sb.append(protocol).append(webURI).append(forumURI).append(forum.getId());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(forum.getUser().getEmail()));
            message.setSubject(map.get("forumCreated"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String lang) {
        String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes()); 
        List<EmailMessage> accessMessages=null;
        if (lang.equals("hi")){
            accessMessages=emailMessages_hi.get(EmailTemplateType.PASSWORD_RESET.name());
        }else{
            accessMessages=emailMessages_en.get(EmailTemplateType.PASSWORD_RESET.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(email).append("\n");
        sb.append(map.get("passReset")).append("\n");
        sb.append(map.get("requestMade")).append("\n");
        sb.append(map.get("clickLink")).append("\n");
        sb.append(protocol).append(webURI).append(passwordResetURI).append(encodedEmail);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("पासवर्ड रीसेट्");
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendContactUsEmailToAdmin(ContactVO contactVO, String lang) {
        List<EmailMessage> accessMessages=null;
        if (lang.equals("hi")){
            accessMessages=emailMessages_hi.get(EmailTemplateType.CONTACT_US.name());
        }else{
            accessMessages=emailMessages_en.get(EmailTemplateType.CONTACT_US.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("msgFromUser")).append("\n");
        sb.append(map.get("admin")).append("\n");
        sb.append(map.get("adminMsg")).append("\n");
        sb.append(map.get("subject")).append(":").append(contactVO.getSubject()).append("\n");
        sb.append(contactVO.getMessage());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sender));//Sending to the Admin
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(contactVO.getSenderEmail()));
            message.setSubject(contactVO.getSubject());
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
            
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }

    }

    @Override
    public void sendNominateNewLSCandidateEmail(User user, LsCandidate lc, String lang) {
        List<EmailMessage> lsMessages=null;
        if (lang.equals("hi")){
            lsMessages=emailMessages_hi.get(EmailTemplateType.LS_NOMINATE.name());
        }else{
            lsMessages=emailMessages_en.get(EmailTemplateType.LS_NOMINATE.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:lsMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("nominated")).append("\n");
        sb.append(user.getEmail()).append(",\n");
        sb.append(map.get("msg")).append("\n");
        sb.append(map.get("name")).append(": ").append(lc.getName()).append("\n");
        sb.append(map.get("constituency")).append(": ").append(lc.getLokSabha().getConstituency()).append("\n");
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(map.get("nominated"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendAddNominationLSCandidateEmail(User user, LsCandidate lc, String lang) {
        sendNominateNewLSCandidateEmail(user,lc, lang);
    }

    @Override
    public void sendnominateNewVSCandidateEmail(User user, VsCandidate vc, String lang) {
        List<EmailMessage> vsMessages=null;
        if (lang.equals("hi")){
            vsMessages=emailMessages_hi.get(EmailTemplateType.VS_NOMINATE.name());
        }else{
            vsMessages=emailMessages_en.get(EmailTemplateType.VS_NOMINATE.name());
        }
        Map<String, String> map=new HashMap();
        for (EmailMessage msg:vsMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("nominated")).append("\n");
        sb.append(user.getEmail()).append(",\n");
        sb.append(map.get("msg")).append("\n");
        sb.append(map.get("name")).append(": ").append(vc.getName()).append("\n");
        sb.append(map.get("constituency")).append(": ").append(vc.getVidhanSabha().getConstituency()).append("\n");
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(map.get("nominated"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOGGER.severe(mex.getMessage());
        }
    }

    @Override
    public void sendAddNominationVSCandidateEmail(User user, VsCandidate lc, String lang) {
        sendnominateNewVSCandidateEmail(user, lc, lang);
    }
}
