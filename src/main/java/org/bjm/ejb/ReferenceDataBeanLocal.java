/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.bjm.model.EmailMessage;
import org.bjm.model.EmailTemplateType;
import org.bjm.model.ForumCategory;
import org.bjm.model.LokSabha;
import org.bjm.model.State;
import org.bjm.model.SurveyCategory;

/**
 *
 * @author root
 */
@Local
public interface ReferenceDataBeanLocal {
    
    public List<State> getStates();
    
    public List<LokSabha> getLokSabhasForState(String stateCode);

    public Map<EmailTemplateType, String> getEmailTemplatesMap();
    
    public String getEmailTemplate(EmailTemplateType emailTemplateType);
    
    public List<String> getForumCategories(String lang);
    
    public List<String> getForumSubCategories(String category, String lang);
    
    public ForumCategory getForumCategory(String cat, String subCat);
    
    public List<String> getSurveyCategories(String lang);
    
    public List<String> getSurveySubCategories(String category, String lang);
    
    public SurveyCategory getSurveyCategory(String cat, String subCat);
    
    public HashMap<String,List<EmailMessage>> getEmailMessages(String langCode);
    
}
