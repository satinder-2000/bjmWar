/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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

/**
 *
 * @author root
 */
@Named(value = "blogServiceMBean")
@ViewScoped
public class BlogServiceMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(BlogServiceMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal miscServicesBeanLocal;
    
    private List<Blog> allBlogs;
    
    private Blog blog;
    
    private BlogComment blogComment;
    
    @PostConstruct
    public void init(){
        allBlogs=miscServicesBeanLocal.getAllBlogs();
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

    
    public String loadBlog(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String blodIdStr=request.getParameter("blogId");
        int blogId=Integer.parseInt(blodIdStr);
        for (Blog b : allBlogs){
            if (blogId==b.getId()){
                setBlog(b);
                blogComment=new BlogComment();
                break;
            }
        }
        StringBuilder sb=new StringBuilder("/blog/");
        sb.append(getBlog().getFile()).append(".xhtml?faces-redirect=true");
        return sb.toString();
        
    }
    
    public String postBlogComment(){
        
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
    
    

}
