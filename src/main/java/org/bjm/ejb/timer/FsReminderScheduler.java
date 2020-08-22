/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb.timer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Stateless
public class FsReminderScheduler {
    
    static final Logger LOGGER=Logger.getLogger(FsReminderScheduler.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    
    
    //@Schedule(second = "0,10,20", minute = "*", hour="*", persistent = false)
    public void sendReminders(){
        LocalDateTime timeNow=LocalDateTime.now();
        List<User> usersToNotify=getUsersToNotify();
        for (User u: usersToNotify){
            notifyUser(u);
        }
        
    }
    
    private List<User> getUsersToNotify(){
        TypedQuery<User> qU=em.createQuery("select u from User u", User.class);
        qU.setParameter(1, new Date(), TemporalType.TIMESTAMP);
        
        return qU.getResultList();
        
    }

    private void notifyUser(User u) {
        LOGGER.log(Level.INFO, "Sending FsReminder to user {0}", u.getId());
    }
    
}
