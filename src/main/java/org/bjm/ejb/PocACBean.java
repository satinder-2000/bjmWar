/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.LokSabha;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Singleton
@Startup
public class PocACBean implements PocACBeanLocal {
    
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public List<LokSabha> getLokSabhas(String stateCd) {
        TypedQuery<LokSabha> tQ=em.createQuery("select ls from LokSabha ls where ls.stateCode=?1", LokSabha.class);
        tQ.setParameter(1, stateCd);
        return tQ.getResultList();
    }

    @Override
    public List<VidhanSabha> getVidhanSabhas(String stateCd) {
        TypedQuery<VidhanSabha> tQ=em.createQuery("select vs from VidhanSabha vs where vs.stateCode=?1", VidhanSabha.class);
        tQ.setParameter(1, stateCd);
        return tQ.getResultList();
    }

    @Override
    public List<String> getStateCodes() {
        TypedQuery<String> tQ=em.createQuery("select s.code from State s", String.class);
        return tQ.getResultList();
    }

   
}
