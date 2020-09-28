/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.EmailTemplateType;
import org.bjm.model.EmailTemplate;
import org.bjm.model.ForumCategory;
import org.bjm.model.LokSabha;
import org.bjm.model.State;
import org.bjm.model.SurveyCategory;

/**
 *
 * @author root
 */
@Stateful
@Startup
public class ReferenceDataBean implements Serializable, ReferenceDataBeanLocal {
    
    static final Logger LOGGER=Logger.getLogger(ReferenceDataBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    private List<State> states;
    Map<EmailTemplateType, String> emailTemplateMap;
    private List<String> forumCategories;
    private List<String> surveyCategories;
    
    @PostConstruct
    public void init(){
        states=new ArrayList();
        TypedQuery<State> tQ=em.createQuery("select s from State s", State.class);
        states=tQ.getResultList();
        //Forum Categories
        TypedQuery<String> fcTq=em.createQuery("select distinct fc.type from ForumCategory fc", String.class);
        forumCategories=fcTq.getResultList();
        //Survey Categories
        TypedQuery<String> scTq=em.createQuery("select distinct sc.type from SurveyCategory sc", String.class);
        forumCategories=scTq.getResultList();
        
        //Email Templates
        TypedQuery<EmailTemplate> eTQ=em.createQuery("select et from EmailTemplate et", EmailTemplate.class);
        List<EmailTemplate> result = eTQ.getResultList();
        emailTemplateMap = new HashMap<>();
        for (EmailTemplate eT : result) {
            emailTemplateMap.put(EmailTemplateType.valueOf(eT.getTemplateType()), eT.getFile());
        }
        LOGGER.log(Level.INFO, "Loading Reference Data Completed");
        
    }

    @Override
    public List<State> getStates() {
       return states;
    }

    @Override
    public Map<EmailTemplateType, String> getEmailTemplatesMap() {
        return emailTemplateMap;
    }

    @Override
    public String getEmailTemplate(EmailTemplateType emailTemplateType) {
        return emailTemplateMap.get(emailTemplateType);
    }

    @Override
    public List<String> getForumCategories() {
        return forumCategories;
    }
    
    

    @Override
    public List<String> getForumSubCategories(String category) {
        TypedQuery<String> fcTq=em.createQuery("select fc.subtype from ForumCategory fc where fc.type=?1", String.class);
        fcTq.setParameter(1, category);
        return fcTq.getResultList();
    }
    
    @Override
    public ForumCategory getForumCategory(String cat, String subCat){
        TypedQuery<ForumCategory> fcTq=em.createQuery("select fc from ForumCategory fc where fc.type=?1 and fc.subtype=?2", ForumCategory.class);
        fcTq.setParameter(1, cat);
        fcTq.setParameter(2, subCat);
        return fcTq.getSingleResult();
        
    }

    @Override
    public List<String> getSurveyCategories() {
        return surveyCategories;
    }

    @Override
    public List<String> getSurveySubCategories(String category) {
        TypedQuery<String> scTq=em.createQuery("select sc.subtype from SurveyCategory sc where sc.type=?1", String.class);
        scTq.setParameter(1, category);
        return scTq.getResultList();
    }

    public SurveyCategory getSurveyCategory(String cat, String subCat) {
        TypedQuery<SurveyCategory> scTq=em.createQuery("select sc from SurveyCategory sc where sc.type=?1 and sc.subtype=?2", SurveyCategory.class);
        scTq.setParameter(1, cat);
        scTq.setParameter(2, subCat);
        return scTq.getSingleResult();
    }

    @Override
    public List<LokSabha> getLokSabhasForState(String stateCode) {
        TypedQuery<LokSabha> tQ=em.createQuery("select l from LokSabha l where l.stateCode=?1", LokSabha.class);
        tQ.setParameter(1, stateCode);
        return tQ.getResultList();
    }
}
