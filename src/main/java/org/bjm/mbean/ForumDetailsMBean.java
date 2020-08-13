/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.bjm.model.Forum;
import org.bjm.model.ForumComment;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ImageVO;

/**
 *
 * @author root
 */
@Named(value = "forumDetailsMBean")
@ViewScoped
public class ForumDetailsMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ForumDetailsMBean.class.getName());
    
    private static final int COMMENT_LENGTH=1500; 
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    private Forum forum;
    
    private String userComment;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String forumIdStr=request.getParameter("forumId");
        int forumId=Integer.parseInt(forumIdStr);
        forum=forumBeanLocal.getForumById(forumId);
        HttpSession session=request.getSession();
        User user =(User)session.getAttribute(BJMConstants.USER);
        if (user!=null){//Good to allow read only view as well. 
            String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
            ImageVO imageVO=new ImageVO(imgType,user.getImage());
            session.setAttribute(BJMConstants.TEMP_IMAGE, imageVO);
        }
        String creatorImgType=forum.getUser().getProfileFile().substring(forum.getUser().getProfileFile().indexOf('.') + 1);
        ImageVO creatorImgVO=new ImageVO(creatorImgType,forum.getUser().getImage());
        session.setAttribute(BJMConstants.FORUM_CREATOR, creatorImgVO);
        
        LOGGER.info("Forum loaded in MBean with id : "+forum.getId());
    }

    public Forum getForum() {
        return forum;
    }
    
    public String postComment(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        String comment=getUserComment();
        if (comment==null||comment.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("noComment"), rb.getString("noComment")));
        }else if(comment.length()>COMMENT_LENGTH){
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("forumCmtOverChars"), rb.getString("forumCmtOverChars")));
        }else{
            ForumComment fc = new ForumComment();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            User user =(User)session.getAttribute(BJMConstants.USER);
            fc.setUser(user);

            fc.setText(getUserComment());
            forum = forumBeanLocal.addForumComment(forum, fc);
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("commentAdded"), rb.getString("commentAdded")));
            userComment="";
        }
        return null;
    }
    
    public List<ForumComment> getOtherForumComments(){
        List<ForumComment> forumComments=forumBeanLocal.getForumComments(forum.getId());
        Map<Integer,ImageVO> forumCommentsImageMap=new HashMap<>();
        for (ForumComment fc: forumComments){
            User user=fc.getUser();
            String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
            ImageVO imageVO=new ImageVO(imgType,user.getImage());
            forumCommentsImageMap.put(fc.getId(), imageVO);
        }
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        session.setAttribute(BJMConstants.FORUM_COMMENT_IMAGE_MAP, forumCommentsImageMap);
        return forumComments;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
    
    
    
    
    
}
