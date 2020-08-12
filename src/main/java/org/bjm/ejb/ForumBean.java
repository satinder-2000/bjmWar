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
import org.bjm.model.Activity;
import org.bjm.model.ActivityType;
import org.bjm.model.Forum;
import org.bjm.model.ForumAbuse;
import org.bjm.model.ForumComment;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Stateless
public class ForumBean implements ForumBeanLocal {
    
    private static final Logger LOGGER=Logger.getLogger(ForumBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailerBeanLocal emailerBeanLocal;
    
    @Inject
    private ActivityBeanLocal activityBeanLocal;

    @Override
    public Forum createForum(Forum forum, User user) {
        forum.setCreated(LocalDateTime.now());
        forum.getUser().getUserForums().add(forum);
        em.persist(forum);
        em.merge(forum.getUser());
        em.flush();
        LOGGER.info("New Forum created with ID : "+forum.getId());
        emailerBeanLocal.sendForumCreatedMail(forum);
        Activity activity=new Activity();
        activity.setUserId(user.getId());
        activity.setActivityId(forum.getId());
        activity.setActivityType(ActivityType.FORUM_CREATED);
        activity.setDated(LocalDateTime.now());
        activity.setDescription("Forum created :"+forum.getTitle());
        activityBeanLocal.logActivity(activity);
        return forum;
    }

    @Override
    public List<Forum> getForums() {
        TypedQuery<Forum> tQ=em.createQuery("select f from Forum f order by f.created desc", Forum.class);
        return tQ.getResultList();
    }

    @Override
    public Forum getForumById(int forumId) {
        return em.find(Forum.class, forumId);
    }

    @Override
    public Forum addForumComment(Forum forum, ForumComment fc) {
        forum.getForumComments().add(fc);
        fc.setForum(forum);
        fc.setDated(LocalDateTime.now());
        em.persist(fc);
        Forum toReturn= em.merge(forum);//returning with new Comment
        return toReturn;
        
    }

    @Override
    public List<ForumComment> getForumComments(int forumId) {
        TypedQuery<ForumComment> tQ=em.createQuery("select fc from ForumComment fc where fc.forum.id=?1 order by fc.dated desc", ForumComment.class);
        tQ.setParameter(1, forumId);
        return tQ.getResultList();
    }

    @Override
    public List<Forum> getForumsByUser(int userId) {
        TypedQuery<Forum> tQ=em.createQuery("select f from Forum f where f.user.id=?1", Forum.class);
        tQ.setParameter(1, userId);
        return tQ.getResultList();
    }

    @Override
    public ForumAbuse addForumAbuse(ForumAbuse forumAbuse) {
        forumAbuse.setReportedOn(LocalDateTime.now());
        em.persist(forumAbuse);
        em.flush();
        return forumAbuse;
    }

    @Override
    public ForumComment getForumCommentById(int commentId) {
        return em.find(ForumComment.class, commentId);
    }

    
}
