/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Blog;
import org.bjm.model.BlogAbuse;
import org.bjm.model.BlogComment;
import org.bjm.vo.ContactVO;

/**
 *
 * @author root
 */
@Stateless
public class MiscellaneousServicesBean implements MiscellaneousServicesBeanLocal {
    
    private static final Logger LOGGER=Logger.getLogger(MiscellaneousServicesBean.class.getName());
    
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailerBeanLocal emailerBeanLocal;

    @Override
    public List<Blog> getAllBlogs() {
        TypedQuery<Blog> tQ=em.createQuery("select b from Blog b order by b.dated desc", Blog.class);
        return tQ.getResultList();
    }

    @Override
    public Blog addBlog(Blog blog) {
        blog.setDated(LocalDateTime.now());
        em.persist(blog);
        em.flush();
        return blog;
    }

    @Override
    public void sendContactUsMessage(ContactVO contactVO) {
        emailerBeanLocal.sendContactUsEmailToAdmin(contactVO);
    }

    @Override
    public List<BlogComment> getBlogComments(int blogId) {
        TypedQuery<BlogComment> tQ=em.createQuery("select bc from BlogComment bc where bc.blog.id=?1 order by bc.dated desc", BlogComment.class);
        tQ.setParameter(1, blogId);
        return tQ.getResultList();
    }

    @Override
    public Blog addBlogComment(Blog blog, BlogComment blogComment) {
        if(blog==null) LOGGER.severe("blog is null");
        if(blogComment==null) LOGGER.severe("blogComment is null");
        if(blog.getBlogComments()==null)LOGGER.severe("blog.getBlogComments() is null"); 
        blog.getBlogComments().add(blogComment);
        blogComment.setBlog(blog);
        blogComment.setDated(LocalDateTime.now());
        em.persist(blogComment);
        Blog toReturn=em.merge(blog);
        return toReturn;
    }

    @Override
    public BlogAbuse addBlogAbuse(BlogAbuse blogAbuse) {
        blogAbuse.setReportedOn(LocalDateTime.now());
        em.persist(blogAbuse);
        em.merge(blogAbuse.getBlogComment());
        em.flush();
        LOGGER.info("BlogAbuse persisted with id: "+blogAbuse.getId());
        return blogAbuse;
    }

    @Override
    public List<BlogAbuse> getBlogAbuses() {
        TypedQuery<BlogAbuse> tQ=em.createQuery("select ba from BlogAbuse ba", BlogAbuse.class);
        return tQ.getResultList();
    }

    @Override
    public BlogComment getBlogCommentById(int commentId) {
        TypedQuery<BlogComment> tQ=em.createQuery("select bc from BlogComment bc where bc.id=?1", BlogComment.class);
        tQ.setParameter(1, commentId);
        return tQ.getSingleResult();
    }

     
    
}
