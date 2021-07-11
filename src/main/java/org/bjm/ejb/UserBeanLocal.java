/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import javax.ejb.Local;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Local
public interface UserBeanLocal {
    
    public User createUser(User user, String lang);
    
    public boolean isEmailRegistered(String email);

    public User getUserByEmail(String email);

    public User amendUser(User user);
    
    public User createAccess(User user, String lang);
    
    public User getUser(String email, String password);
    
    public void dispatchAccessReset(String email, String lang);
    
    public User changePassword(User user);
    
    public User updateUserFSReminder(User user);
    
}
