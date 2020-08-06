/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.Visitor;

/**
 *
 * @author root
 */
@Stateless
public class VisitorBean implements VisitorBeanLocal {
    
    static final Logger LOGGER=Logger.getLogger(VisitorBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    EntityManager em;

    @Override
    public boolean performIPCheckIfSaved(String ipAddress) {
        TypedQuery<Visitor> vTq=em.createQuery("select v from Visitor v where v.ipAddress=?1", Visitor.class);
        vTq.setParameter(1, ipAddress);
        try{
            Visitor v=vTq.getSingleResult();
            return true;
        }catch(NoResultException ex){
            return false;
        }
    }

    @Override
    public void saveVisitor(Visitor visitor) {
        visitor.setTime(LocalDateTime.now());
        visitor.setLang("en");
        em.persist(visitor);
        em.flush();
        LOGGER.info("IPAddress saved - "+visitor.getIpAddress());
    }

    @Override
    public Visitor getVisitor(String ipAddress) {
        TypedQuery<Visitor> vTq=em.createQuery("select v from Visitor v where v.ipAddress=?1", Visitor.class);
        vTq.setParameter(1, ipAddress);
        Visitor toReturn=null;
        try{
            toReturn=vTq.getSingleResult();
        }catch(NoResultException ex){
            LOGGER.info("No Visitor found with IP Address "+ipAddress);
        }
        return toReturn;
    }

    @Override
    public Visitor updateVisitor(Visitor visitor) {
        visitor.setTime(LocalDateTime.now());
        em.merge(visitor);
        em.flush();
        return visitor;
        
    }

    
}
