/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.Activity;

/**
 *
 * @author root
 */
@Local
public interface ActivityBeanLocal {
    
    public Activity logActivity(Activity activity);
    
    public List<Activity> getRecentActivities();
    
}
