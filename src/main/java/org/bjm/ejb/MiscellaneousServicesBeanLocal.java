/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.Blog;
import org.bjm.model.BlogAbuse;
import org.bjm.model.BlogComment;
import org.bjm.vo.ContactVO;

/**
 *
 * @author root
 */
@Local
public interface MiscellaneousServicesBeanLocal {
    
    public List<Blog> getAllBlogs();
    
    public Blog addBlog(Blog blog);
    
    public List<BlogComment> getBlogComments(int blogId);
    
    public BlogComment getBlogCommentById(int commentId);
    
    public Blog addBlogComment(Blog blog, BlogComment blogComment);
    
    public BlogAbuse addBlogAbuse(BlogAbuse blogAbuse);
    
    public List<BlogAbuse> getBlogAbuses();
    
    public void sendContactUsMessage(ContactVO contactVO);
    
}
