/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.bjm.ejb.ActivityBean;
import org.bjm.model.Activity;

/**
 *
 * @author root
 */
@Stateless
public class ActivityFacade implements ActivityFacadeLocal {
    
    @Inject
    private ActivityBean activityBean;

    @Override
    public Activity logActivity(Activity activity) {
        return activityBean.logActivity(activity);
    }

    @Override
    public List<Activity> getRecentActivities() {
        return activityBean.getRecentActivities();
    }

   
}
