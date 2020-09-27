/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.LokSabha;
import org.bjm.model.LsCandidate;
import org.bjm.model.VsCandidate;
import org.bjm.model.User;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Stateless
public class ElectoralBean implements ElectoralBeanLocal {

    private static final Logger LOGGER=Logger.getLogger(ElectoralBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailerBeanLocal emailerBeanLocal;

    @Override
    public List<String> getLSCandidates(int constituencyId) {
        LOGGER.info("constituencyId is "+constituencyId);
        TypedQuery<String> tQ=em.createQuery("select lc.name from LsCandidate lc where lc.lokSabha.id=?1", String.class);
        tQ.setParameter(1, constituencyId);
        List<String> toReturn=tQ.getResultList();
        LOGGER.info("toReturn size is : "+toReturn.size());
        return toReturn;
    }
    
    @Override
    public int getLokSabhaIdByConstituency(String stateCode,String constituency) {
        int toReturn=0;
        TypedQuery<LokSabha> tQ=em.createQuery("select ls from LokSabha ls where ls.stateCode=?1 and ls.constituency=?2", LokSabha.class);
        tQ.setParameter(1, stateCode);
        tQ.setParameter(2, constituency);
        LokSabha ls=tQ.getSingleResult();
        toReturn =ls.getId();
        return toReturn;
    }

    @Override
    public LokSabha getLokSabhaByConstituency(String stateCode, String constituency) {
        TypedQuery<LokSabha> tQ=em.createQuery("select ls from LokSabha ls where ls.stateCode=?1 and ls.constituency=?2", LokSabha.class);
        tQ.setParameter(1, stateCode);
        tQ.setParameter(2, constituency);
        LokSabha ls=tQ.getSingleResult();
        return ls;
    }

    @Override
    public int nominateNewLSCandidate(User user, LsCandidate lc) {
        lc.setRepresent(false);
        lc.setNameConfCount(0);
        em.persist(lc);
        em.merge(lc.getLokSabha());
        em.flush();
        emailerBeanLocal.sendNominateNewLSCandidateEmail(user, lc);
        LOGGER.log(Level.INFO, "New LS Candidate Nominated with ID: {0}", lc.getId());
        return lc.getId();
    }

    @Override
    public void addNominationLSCandidate(User user,LsCandidate lc) {
        lc.setNameConfCount(lc.getNameConfCount()+1);
        lc=em.merge(lc);
        emailerBeanLocal.sendAddNominationLSCandidateEmail(user,lc);
        LOGGER.log(Level.INFO, "LsCandidate {0} now has {1} nominations", new Object[]{lc.getId(), lc.getNameConfCount()});
        em.flush();
    }

    @Override
    public LsCandidate getLSCandidate(int constituencyId, String cName) {
        TypedQuery<LsCandidate> tQ=em.createQuery("select lc from LsCandidate lc where lc.name=?1 and lc.lokSabha.id=?2", LsCandidate.class);
        tQ.setParameter(1, cName);
        tQ.setParameter(2, constituencyId);
        LsCandidate lc=tQ.getSingleResult();
        LOGGER.log(Level.INFO, "LS Candidate retrieved with ID: {0}", lc.getId());
        return lc;
    }

    @Override
    public List<String> getVSCandidates(int constituencyId) {
        LOGGER.info("constituencyId is "+constituencyId);
        TypedQuery<String> tQ=em.createQuery("select vc.name from VsCandidate vc where vc.vidhanSabha.id=?1", String.class);
        tQ.setParameter(1, constituencyId);
        List<String> toReturn=tQ.getResultList();
        LOGGER.info("toReturn size is : "+toReturn.size());
        return toReturn;
    }

    @Override
    public int getVidhanSabhaIdByConstituency(String stateCode, String constituency) {
        int toReturn=0;
        TypedQuery<VidhanSabha> tQ=em.createQuery("select vs from VidhanSabha vs where vs.stateCode=?1 and vs.constituency=?2", VidhanSabha.class);
        tQ.setParameter(1, stateCode);
        tQ.setParameter(2, constituency);
        VidhanSabha ls=tQ.getSingleResult();
        toReturn =ls.getId();
        return toReturn;
    }

    @Override
    public VidhanSabha getVidhanSabhaByConstituency(String stateCode, String constituency) {
        TypedQuery<VidhanSabha> tQ=em.createQuery("select vs from VidhanSabha vs where vs.stateCode=?1 and vs.constituency=?2", VidhanSabha.class);
        tQ.setParameter(1, stateCode);
        tQ.setParameter(2, constituency);
        VidhanSabha vs=tQ.getSingleResult();
        return vs;
    }

    @Override
    public int nominateNewVSCandidate(User user, VsCandidate vc) {
        vc.setRepresent(false);
        vc.setNameConfCount(0);
        em.persist(vc);
        em.merge(vc.getVidhanSabha());
        em.flush();
        emailerBeanLocal.sendnominateNewVSCandidateEmail(user, vc);
        LOGGER.log(Level.INFO, "New VS Candidate Nominated with ID: {0}", vc.getId());
        return vc.getId();
    }

    @Override
    public void addNominationVSCandidate(User user, VsCandidate vc) {
        vc.setNameConfCount(vc.getNameConfCount()+1);
        vc=em.merge(vc);
        emailerBeanLocal.sendAddNominationVSCandidateEmail(user, vc);
        LOGGER.log(Level.INFO, "VsCandidate {0} now has {1} nominations", new Object[]{vc.getId(), vc.getNameConfCount()});
        em.flush();
    }

    @Override
    public VsCandidate getVSCandidate(int constituencyId, String cName) {
        TypedQuery<VsCandidate> tQ=em.createQuery("select vc from VsCandidate vc where vc.name=?1 and vc.vidhanSabha.id=?2", VsCandidate.class);
        tQ.setParameter(1, cName);
        tQ.setParameter(2, constituencyId);
        VsCandidate lc=tQ.getSingleResult();
        LOGGER.log(Level.INFO, "VS Candidate retrieved with ID: {0}", lc.getId());
        return lc;
    }

    @Override
    public List<String> getLSConstituencies(String stateCd) {
        TypedQuery<String> tQ=em.createQuery("select l.constituency from LokSabha l where l.stateCode=?1 order by l.constituency", String.class);
        tQ.setParameter(1, stateCd);
        return tQ.getResultList();
    }

    @Override
    public List<String> getVSConstituencies(String stateCd) {
        TypedQuery<String> tQ=em.createQuery("select v.constituency from VidhanSabha v where v.stateCode=?1 order by v.constituency", String.class);
        tQ.setParameter(1, stateCd);
        return tQ.getResultList();
    }

   

    
    
    
}
