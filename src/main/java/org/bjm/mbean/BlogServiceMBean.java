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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.MiscellaneousServicesBeanLocal;
import org.bjm.model.Blog;
import org.bjm.model.BlogComment;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ImageVO;

/**
 *
 * @author root
 */
@Named(value = "blogServiceMBean")
@SessionScoped
public class BlogServiceMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(BlogServiceMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal miscServicesBeanLocal;
    
    private List<Blog> allBlogs;
    
    private List<BlogComment> blogComments;
    
    private Blog blog;
    
    private BlogComment blogComment;
    
    private User user; 
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        allBlogs=miscServicesBeanLocal.getAllBlogs();
        blogComment=new BlogComment();
        LOGGER.info("Loaded Blogs : "+allBlogs.size());
        
    }

    public List<Blog> getAllBlogs() {
        return allBlogs;
    }

    public void setAllBlogs(List<Blog> allBlogs) {
        this.allBlogs = allBlogs;
    }
    
    public String addBlog(){
        blog=new Blog();
        return "/blog/AddBlog.xhtml?faces-redirect=true";
    }
    
    public String submitBlog(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(BJMConstants.USER);
        blog.setUser(user);
        LOGGER.severe("The method has not been implemented");
        return "/blog/AddBlogConfirm.xhtml?faces-redirect=true";
    }
    
    public String loadBlogSample(){
        System.out.println("Inside loadBlogSample()");
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String blodIdStr=request.getParameter("blogId");
        int blogId=Integer.parseInt(blodIdStr);
        Map<Integer,ImageVO> blogCommentsImageMap=new HashMap<>();
        for (Blog b : allBlogs){
            if (blogId==b.getId()){
                setBlog(b);
                //load existing comments as well
                List<BlogComment> fromDB=miscServicesBeanLocal.getBlogComments(getBlog().getId());
                if (fromDB!=null){
                   blogComments=fromDB;
                   for(BlogComment bc:blogComments){
                       User user=bc.getUser();
                       String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
                       ImageVO imageVO=new ImageVO(imgType,user.getImage());
                       blogCommentsImageMap.put(bc.getId(),imageVO);
                   }
                   HttpSession session=request.getSession();
                   session.setAttribute(BJMConstants.BLOG_COMMENT_IMAGE_MAP, blogCommentsImageMap);
                }
                //blogComment=new BlogComment();
                //break;
            }
        }
        StringBuilder sb=new StringBuilder("/blogSample/");
        sb.append(getBlog().getFile()).append(".xhtml?faces-redirect=true");
        return sb.toString();
    }

    
    public String loadBlog(){
        
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
        HttpSession session=request.getSession(true);
        User user=(User)session.getAttribute(BJMConstants.USER);
        if (user==null){
           System.out.println("Inside loadBlog() and the User is null"); 
        }else{
            System.out.println("Inside loadBlog() and the User is "+user.getEmail());
        }
        
        String blodIdStr=request.getParameter("blogId");
        int blogId=Integer.parseInt(blodIdStr);
        Map<Integer,ImageVO> blogCommentsImageMap=new HashMap<>();
        for (Blog b : allBlogs){
            if (blogId==b.getId()){
                setBlog(b);
                //load existing comments as well
                List<BlogComment> fromDB=miscServicesBeanLocal.getBlogComments(getBlog().getId());
                if (fromDB!=null){
                   blogComments=fromDB;
                   for(BlogComment bc:blogComments){
                       user=bc.getUser();
                       String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
                       ImageVO imageVO=new ImageVO(imgType,user.getImage());
                       blogCommentsImageMap.put(bc.getId(),imageVO);
                   }
                   //HttpSession session=request.getSession();
                   session.setAttribute(BJMConstants.BLOG_COMMENT_IMAGE_MAP, blogCommentsImageMap);
                }
                //blogComment=new BlogComment();
                //break;
            }
        }
        StringBuilder sb=new StringBuilder("/blog/");
        sb.append(getBlog().getFile()).append(".xhtml?faces-redirect=true");
        return sb.toString();
        
    }
    
    public String postBlogComment(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        String comment=blogComment.getText();
        if(comment.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("blogCmtEmpty"), rb.getString("blogCmtEmpty"))); 
        }else if (comment.length()>BJMConstants.BLOG_COMMENT_LENGTH){
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("blogCmtEmpty"), rb.getString("blogCmtEmpty"))); 
        }else{//every this is fine. proceed to submit
            //set the User on the blogComment
            blogComment.setUser(user);
            blog=miscServicesBeanLocal.addBlogComment(blog, blogComment);
            blogComments=blog.getBlogComments();
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("blogCmtSuccess"), rb.getString("blogCmtSuccess"))); 
        }
                
        return null;
    }
    
    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public BlogComment getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(BlogComment blogComment) {
        this.blogComment = blogComment;
    }

    /**
     * 
     * @return List of BlogComments. Will be null if there is no comment.
     */
    public List<BlogComment> getBlogComments() {
        return blogComments;
    }

    public void setBlogComments(List<BlogComment> blogComments) {
        this.blogComments = blogComments;
    }
    
    

}
