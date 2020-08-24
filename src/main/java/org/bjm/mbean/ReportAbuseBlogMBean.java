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
import org.bjm.ejb.MiscellaneousServicesBeanLocal;
import org.bjm.model.BlogAbuse;
import org.bjm.model.BlogComment;
import org.bjm.model.ForumAbuse;
import org.bjm.model.ForumComment;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@Named(value = "reportAbuseBlogMBean")
@ViewScoped
public class ReportAbuseBlogMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ReportAbuseBlogMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal miscellaneousServicesBeanLocal;
    
    private int commentId;
    
    private BlogAbuse blogAbuse;
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String commentIdStr=request.getParameter("commentId");
        commentId=Integer.parseInt(commentIdStr);
        blogAbuse=new BlogAbuse();
    }
    
    public String reportAbuse(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(BJMConstants.USER);
        blogAbuse.setReportedBy(user);
        BlogComment bc=miscellaneousServicesBeanLocal.getBlogCommentById(commentId);
        blogAbuse.setBlogComment(bc);
        blogAbuse=miscellaneousServicesBeanLocal.addBlogAbuse(blogAbuse);
        LOGGER.info("Blog Abuse added with ID - "+blogAbuse.getId());
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("abuseReported"), rb.getString("abuseReported")));
        blogAbuse=new BlogAbuse();
        return null;
    }

    public BlogAbuse getBlogAbuse() {
        return blogAbuse;
    }

    public void setBlogAbuse(BlogAbuse blogAbuse) {
        this.blogAbuse = blogAbuse;
    }

    
    
}
