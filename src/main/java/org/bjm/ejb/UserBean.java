/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.ejb.util.PasswordUtil;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Stateless
public class UserBean implements UserBeanLocal {
    
    private static final Logger LOGGER=Logger.getLogger(UserBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailerBeanLocal emailerBeanLocal;

    @Override
    public User createUser(User user) {
        user.setCreatedOn(LocalDateTime.now());
        user.setUpdatedOn(LocalDateTime.now());
        user.setFsReminder(4);
        em.persist(user);
        emailerBeanLocal.sendUserRegConfirmEmail(user);
        LOGGER.log(Level.INFO, "User persisted with ID : {0}", user.getId());
        return user;
    }

    @Override
    public boolean isEmailRegistered(String email) {
        TypedQuery<User> tQ=em.createQuery("select u from User u where u.email=?1", User.class);
        tQ.setParameter(1, email);
        try{
            tQ.getSingleResult();
            return true;
        }catch(NoResultException ex){
            //good for us
            return false;
        }
                
        
    }

    @Override
    public User getUserByEmail(String email) {
        TypedQuery<User> tQ=em.createQuery("select u from User u where u.email=?1", User.class);
        tQ.setParameter(1, email);
        return tQ.getSingleResult();
    }

    @Override
    public User amendUser(User user) {
        user.setUpdatedOn(LocalDateTime.now());
        em.merge(user);
        em.flush();
        return user;
    }

    @Override
    public User createAccess(User user) {
        String email=user.getEmail();
        String password=user.getPassword();
        user.setPassword(PasswordUtil.generateSecurePassword(password, email));
        user.setUpdatedOn(LocalDateTime.now());
        user.setFailedAttempts(0);
        em.merge(user);
        em.flush();
        //Email now
        emailerBeanLocal.sendAccessConfirmEmail(email);
        return user;
    }

    @Override
    public User getUser(String email, String password) {
        LOGGER.log(Level.INFO, "Access sought for:{0} with {1}", new Object[]{email, password});
        User toReturn=null;
        TypedQuery<User> aTq=em.createQuery("select u from User u where u.email=?1", User.class);
        aTq.setParameter(1, email);
        List<User> aList= aTq.getResultList();
        if (!aList.isEmpty()){
            User user=aList.get(0);
            //first check the attempts.
            int attempts=user.getFailedAttempts();
            if (attempts==3){
                user.setExceptionMsg("Your Account has been locked.");
                return user;
            }
            boolean passwordVerified=PasswordUtil.verifyUserPassword(password, user.getPassword(), email);
            if (passwordVerified && attempts==0){
               toReturn=user; 
            }else if (passwordVerified && attempts>0){
                user.setFailedAttempts(0);
                //em.getTransaction().begin();
                user= em.merge(user);
                em.flush();
                toReturn=user;
            }else if (!passwordVerified){
                int count=++attempts;
                user.setFailedAttempts(count);
                em.merge(user);
                em.flush();
                LOGGER.log(Level.INFO, "Attemps in the DB now: {0}", user.getFailedAttempts());
                ;
                if (count==3)user.setExceptionMsg("Account Locked. 3 Login attempts expired");
                else user.setExceptionMsg("Login failed. "+(3-count)+" attemps left");
                toReturn=user;
            }
        }
        
        return toReturn;
    }

    @Override
    public void dispatchAccessReset(String email) {
        emailerBeanLocal.sendPasswordResetEmail(email);
    }

    @Override
    public User changePassword(User user) {
        user.setUpdatedOn(LocalDateTime.now());
        user.setPassword(PasswordUtil.generateSecurePassword(user.getPassword(), user.getEmail()));
        user= em.merge(user);
        //em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public User updateUserFSReminder(User user) {
        user.setUpdatedOn(LocalDateTime.now());
        user=em.merge(user);
        em.flush();
        return user;
    }

    
}
