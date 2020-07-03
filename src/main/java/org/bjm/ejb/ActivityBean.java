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
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Activity;

/**
 *
 * @author root
 */
@Stateful
public class ActivityBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ActivityBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Resource(name = "maxActivities")
    private int maxActivities;
    
    /*@Resource
    TimerService timerService;*/

    private LinkedList<Activity> activityStack;
    
    @PostConstruct
    public void init() {
        //timerService.createTimer(1000, 1000000, "Activity Timer");//TODO Frequency (2nd parm) needs changing.
        loadRecentActivities();
        //activityStack = new LinkedList<>();
        LOGGER.info("ActivityRecorder initialised");
    }
    
    /*@Timeout
    public void execute(Timer timer) {
        loadRecentActivities();
    }*/

    private void loadRecentActivities() {

        List<Activity> tempL = loadFromDB(maxActivities);
        activityStack = new LinkedList<>();
        activityStack.addAll(tempL);
        LOGGER.log(Level.INFO, "Activities loaded from the Database: {0}", activityStack.size());

    }
    
    
    private List<Activity> loadFromDB(int number) {
        TypedQuery<Activity> tQA = em.createQuery("select a from Activity a order by a.id desc", Activity.class);
        tQA.setMaxResults(number);
        List<Activity> rs = tQA.getResultList();
        return rs;

    }

    public Activity logActivity(Activity activity) {
        em.persist(activity);
        em.flush();
        LOGGER.log(Level.INFO, "Activity persisted with ID :{0}", activity.getId());
        if (activityStack.size()==maxActivities){
            activityStack.poll();
        }
        activityStack.addFirst(activity);
        return activity;
    }

    public List<Activity> getRecentActivities() {
        List<Activity> acts=new ArrayList(activityStack);
        //LOGGER.info("Activities to return "+acts.size());
        return acts;
    }

    
}
