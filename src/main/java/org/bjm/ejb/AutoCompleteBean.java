/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Stateless
public class AutoCompleteBean {

    private static final int SIZE = 4;
    private static final String ALL_CHARS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    public List<VidhanSabha> getVidhanSabhas(String stateCode, String constituency, int maxresults) {
        TypedQuery<VidhanSabha> tQ = em.createQuery("select vs from VidhanSabha vs where vs.stateCode=?1 and vs.constituency like =?2%", VidhanSabha.class);
        tQ.setParameter(1, stateCode);
        tQ.setParameter(2, constituency);
        tQ.setMaxResults(maxresults);
        return tQ.getResultList();
    }

    public String completeString(String input) {
        StringBuilder sb = new StringBuilder(input);
        for (int i = 0; i < SIZE; i++) {
            Random random = new Random();
            int index = random.nextInt(ALL_CHARS.length());
            sb.append(ALL_CHARS.charAt(index));
        }
        return sb.toString();
    }

}
