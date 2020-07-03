/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.ForumBeanLocal;
import org.bjm.model.Forum;

/**
 *
 * @author root
 */
@Named(value = "forumsMBean")
@ViewScoped
public class ForumsMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ForumsMBean.class.getName());
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    
    public List<Forum> getForums(){
        return forumBeanLocal.getForums();
    }
    
}
