/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb.facade;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.bjm.ejb.ReferenceDataBean;
import org.bjm.model.EmailTemplateType;
import org.bjm.model.ForumCategory;
import org.bjm.model.State;
import org.bjm.model.SurveyCategory;

/**
 *
 * @author root
 */
@Stateless
public class ReferenceDataFacade implements ReferenceDataFacadeLocal {
    
    @Inject
    private ReferenceDataBean referenceDataBean;

    @Override
    public List<State> getStates() {
        return referenceDataBean.getStates();
    }

    @Override
    public Map<EmailTemplateType, String> getEmailTemplatesMap() {
        return referenceDataBean.getEmailTemplatesMap();
    }

    @Override
    public String getEmailTemplate(EmailTemplateType emailTemplateType) {
        return referenceDataBean.getEmailTemplate(emailTemplateType);
    }

    @Override
    public List<String> getForumCategories() {
        return referenceDataBean.getForumCategories();
    }

    @Override
    public List<String> getForumSubCategories(String category) {
        return referenceDataBean.getForumSubCategories(category);
    }

    @Override
    public ForumCategory getForumCategory(String cat, String subCat) {
        return referenceDataBean.getForumCategory(cat, subCat);
    }

    @Override
    public List<String> getSurveyCategories() {
        return referenceDataBean.getSurveyCategories();
    }

    @Override
    public List<String> getSurveySubCategories(String category) {
        return referenceDataBean.getSurveySubCategories(category);
    }

    @Override
    public SurveyCategory getSurveyCategory(String cat, String subCat) {
        return referenceDataBean.getSurveyCategory(cat, subCat);
    }
}
