/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Blog;
import org.bjm.model.view.AbuseReport;
import org.bjm.vo.ContactVO;

/**
 *
 * @author root
 */
@Stateless
public class MiscellaneousServicesBean implements MiscellaneousServicesBeanLocal {
    
    
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
    public List<AbuseReport> getAbusesReportedByUser(int userId) {
        TypedQuery<AbuseReport> tQ=em.createQuery("select ar from AbuseReport ar where ar.reportedById=?1", AbuseReport.class);
        tQ.setParameter(1, userId);
        return tQ.getResultList();
    }

    @Override
    public void sendContactUsMessage(ContactVO contactVO) {
        emailerBeanLocal.sendContactUsEmailToAdmin(contactVO);
    }

     
    
}
