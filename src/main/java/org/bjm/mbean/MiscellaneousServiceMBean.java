/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.MiscellaneousServicesBeanLocal;
import org.bjm.model.Essay;

/**
 *
 * @author root
 */
@Named(value = "miscServiceMBean")
@ViewScoped
public class MiscellaneousServiceMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(MiscellaneousServiceMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal miscServicesBeanLocal;
    
    private List<Essay> allEssays;
    
    @PostConstruct
    public void init(){
        allEssays=miscServicesBeanLocal.getAllEssays();
        LOGGER.info("Loaded Essays : "+allEssays.size());
    }

    public List<Essay> getAllEssays() {
        return allEssays;
    }

    public void setAllEssays(List<Essay> allEssays) {
        this.allEssays = allEssays;
    }

}
