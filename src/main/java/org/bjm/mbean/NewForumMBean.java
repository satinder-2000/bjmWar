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
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.Forum;
import org.bjm.model.ForumCategory;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
//import org.bjm.model

/**
 *
 * @author root
 */
@Named(value = "newForumMBean")
@FlowScoped("NewForum")
public class NewForumMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(NewForumMBean.class.getName());
    
    private String SELECT_ONE=null;
    
    @Inject
    private ReferenceDataBeanLocal referenceDataBeanLocal;
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    private List<String> forumCategories;
    private List<String> forumSubcategories;
    private String type;
    private String subType;
    
    private Forum forum;
    
    @PostConstruct
    public void init(){
        forumCategories=new ArrayList<>();
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        SELECT_ONE=rb.getString("selectOne");
        forumCategories.add(SELECT_ONE);
        forumCategories.addAll(referenceDataBeanLocal.getForumCategories());
        forum=new Forum();
        ForumCategory fc=new ForumCategory();
        forum.setForumCategory(fc);
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        //String type=forum.getForumCategory().getType();
        LOGGER.log(Level.INFO, "Category Type is {0}", type);
        forumSubcategories=new ArrayList();
        forumSubcategories.add(SELECT_ONE);
        forumSubcategories.addAll(referenceDataBeanLocal.getForumSubCategories(type));
        
    }
    
    public String prepareForum(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Forum Category first
        if (type.equals(SELECT_ONE)){
            FacesContext.getCurrentInstance().addMessage("forumCat", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("forumCatNotValid"), rb.getString("forumCatNotValid"))); 
        }
        if (subType.equals(SELECT_ONE)){
            FacesContext.getCurrentInstance().addMessage("forumSubcat", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("forumSubCatNotValid"), rb.getString("forumSubCatNotValid")));
        }
        if (!type.equals(SELECT_ONE) && !subType.equals(SELECT_ONE)){//we are good to go
            ForumCategory fc=referenceDataBeanLocal.getForumCategory(type, subType);
            forum.setForumCategory(fc);
        }
        if (forum.getTitle().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("title", new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("forumTitleNotValid") , rb.getString("forumTitleNotValid"))); 
        }
        if (forum.getDescription().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("description", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("forumDescriptionNotValid"), rb.getString("forumDescriptionNotValid"))); 
        }
        
        String toReturn=null;
        List<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessageList();
        int countErrors=0;
        for (FacesMessage m : msgs){
            if (m.getSeverity()==FacesMessage.SEVERITY_ERROR){
                countErrors++;
            }
        }
        if (countErrors > 0) {
            toReturn = null;
        } else {
            LOGGER.info("ForumConfirm?faces-redirect=true");
            toReturn = "ForumConfirm?faces-redirect=true";
        }
        return toReturn;
        
        
    }
    
    
    public String amendForum(){
        return "NewForum?faces-redirect=true";
    }
    
    
    private void submitForum(){
        //Set User on Forum first
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=(HttpSession) request.getSession();
        User user=(User)session.getAttribute(BJMConstants.USER); 
        forum.setUser(user);
        //Crate Forum now
        forum=forumBeanLocal.createForum(forum,user);
    }
    
    public String getReturnValue() {
        submitForum();
        return "/flowreturns/NewForum-return?faces-redirect=true";
    }
    

    public List<String> getForumCategories() {
        return forumCategories;
    }

    public void setForumCategories(List<String> forumCategories) {
        this.forumCategories = forumCategories;
    }

    public List<String> getForumSubcategories() {
        return forumSubcategories;
    }

    public void setForumSubcategories(List<String> forumSubcategories) {
        this.forumSubcategories = forumSubcategories;
    }
    
    

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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
    
    
    
}
