/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Activity;

/**
 *
 * @author root
 */
@Stateless
public class ActivityBean implements Serializable, ActivityBeanLocal {
    
    private static final Logger LOGGER=Logger.getLogger(ActivityBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Resource(name = "maxActivities")
    private int maxActivities;
    
    
    @Override
    public Activity logActivity(Activity activity) {
        em.persist(activity);
        em.flush();
        LOGGER.log(Level.INFO, "Activity persisted with ID :{0}", activity.getId());
        return activity;
    }

    @Override
    public List<Activity> getRecentActivities() {
        TypedQuery<Activity> tQA = em.createQuery("select a from Activity a order by a.id desc", Activity.class);
        tQA.setMaxResults(maxActivities);
        List<Activity> rs = tQA.getResultList();
        return rs;
    }

    
}
