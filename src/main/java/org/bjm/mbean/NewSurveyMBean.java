/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ForumBeanLocal;
import org.bjm.ejb.ReferenceDataBeanLocal;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.Forum;
import org.bjm.model.ForumCategory;
import org.bjm.model.Survey;
import org.bjm.model.SurveyCategory;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ImageVO;

/**
 *
 * @author root
 */

@Named(value = "newSurveyMBean")
@FlowScoped("NewSurvey")
public class NewSurveyMBean implements Serializable {
    
    static final Logger LOGGER=Logger.getLogger(NewSurveyMBean.class.getName());
    
    private String SELECT_ONE=null;
    
    @Inject
    private ReferenceDataBeanLocal referenceDataBeanLocal;
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    private Survey survey;
    private List<Forum> userForums;
    int forumId;
    
    private List<String> categories;
    private List<String> subcategories;
    private String type;
    private String subType;
    private boolean userHasForums;
    
    @PostConstruct
    public void init(){
        categories=new ArrayList<>();
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        SELECT_ONE=rb.getString("selectOne");
        categories.add(SELECT_ONE);
        categories.addAll(referenceDataBeanLocal.getForumCategories());
        HttpServletRequest request=(HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(BJMConstants.USER); 
        String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
        ImageVO imageVO=new ImageVO(imgType,user.getImage());
        session.setAttribute(BJMConstants.TEMP_IMAGE, imageVO);
        survey=new Survey();
        LOGGER.info("New Servey initialized");
    }
    
    public String startNew(){
        return "NewSurveyDetails?faces-redirect=true";
    }
    
    public String useForumCategory(){
        Forum f=forumBeanLocal.getForumById(forumId);
        ForumCategory fc=f.getForumCategory();
        type=fc.getType();
        subType=fc.getSubtype();
        SurveyCategory sc=referenceDataBeanLocal.getSurveyCategory(type, subType);
        survey.setSurveyCategory(sc);
        return "SurveyFromForum?faces-redirect=true";
        
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "Category Type is {0}", type);
        subcategories=new ArrayList();
        subcategories.add(SELECT_ONE);
        subcategories.addAll(referenceDataBeanLocal.getSurveySubCategories(type));
        
    }
    
    public String surveyFilled(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Forum Category first
        if (type.equals(SELECT_ONE)){
            FacesContext.getCurrentInstance().addMessage("cat", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("surveyCatNotValid"), rb.getString("surveyCatNotValid"))); 
        }
        if (subType.equals(SELECT_ONE)){
            FacesContext.getCurrentInstance().addMessage("subcat", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("surveySubCatNotValid"), rb.getString("surveySubCatNotValid")));
        }
        if (!type.equals(SELECT_ONE) && !subType.equals(SELECT_ONE)){//we are good to go
            SurveyCategory sc=referenceDataBeanLocal.getSurveyCategory(type, subType);
            survey.setSurveyCategory(sc);
        }
        if (survey.getTitle().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("title",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Title", "Invalid Title")); 
        }
        if (survey.getDescription().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("description",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Details", "Invalid Details")); 
        }
        List<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessageList();
        String toReturn=null;
        
        int countErrors=0;
        for (FacesMessage m : msgs){
            if (m.getSeverity()==FacesMessage.SEVERITY_ERROR){
                countErrors++;
            }
        }
        if (countErrors > 0) {
            toReturn = null;
        } else {
            LOGGER.info("SurveyConfirm?faces-redirect=true");
            toReturn = "SurveyConfirm?faces-redirect=true";
        }
        return toReturn;
        
        
    }
    
    
    public String amendSurvey(){
        return "NewSurveyDetails?faces-redirect=true";
    }
    
    
    public String getReturnValue() {
        submitDetails();
        return "/flowreturns/NewSurvey-return?faces-redirect=true";
    }

    private void submitDetails() {
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(BJMConstants.USER); 
        survey.setUser(user);
        survey=surveyBeanLocal.createSurvey(survey,user);
        survey=new Survey();
        LOGGER.info("New Servey initialized");
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<Forum> getUserForums() {
        if (userForums==null){
            HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session=request.getSession();
            User user=(User)session.getAttribute(BJMConstants.USER);
            userForums=forumBeanLocal.getForumsByUser(user.getId());
            if  (userForums==null || userForums.isEmpty()){
                userHasForums=false;
                userForums=new ArrayList<>();
                ResourceBundle rb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{msg}", ResourceBundle.class);
                Forum f=new Forum();
                f.setId(0);
                f.setTitle(rb.getString("noForumFound"));
                userForums.add(f);
            }else{
               userHasForums=true; 
            }
        }
        return userForums;
    }

    public void setUserForums(List<Forum> userForums) {
        this.userForums = userForums;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
    
    

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public boolean isUserHasForums() {
        return userHasForums;
    }

    public void setUserHasForums(boolean userHasForums) {
        this.userHasForums = userHasForums;
    }

    
    
    
    
    
    
    
}
