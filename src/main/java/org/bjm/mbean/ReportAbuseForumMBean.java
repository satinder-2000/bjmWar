/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ForumBeanLocal;
import org.bjm.model.ForumAbuse;
import org.bjm.model.ForumComment;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "reportAbuseForumMBean")
@ViewScoped
public class ReportAbuseForumMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ReportAbuseForumMBean.class.getName());
    
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    private int commentId;
    
    private ForumAbuse forumAbuse;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String commentIdStr=request.getParameter("commentId");
        commentId=Integer.parseInt(commentIdStr);
        forumAbuse=new ForumAbuse();
    }
    
    public String reportAbuse(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        forumAbuse.setReportedBy(user);
        ForumComment fc=forumBeanLocal.getForumCommentById(commentId);
        forumAbuse.setForumComment(fc);
        forumAbuse=forumBeanLocal.addForumAbuse(forumAbuse);
        LOGGER.info("Forum Abuse added with ID - "+forumAbuse.getId());
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("abuseReported"), rb.getString("abuseReported")));
        forumAbuse=new ForumAbuse();
        return null;
    }

    public ForumAbuse getForumAbuse() {
        return forumAbuse;
    }

    public void setForumAbuse(ForumAbuse forumAbuse) {
        this.forumAbuse = forumAbuse;
    }
    
    
    
    
}
