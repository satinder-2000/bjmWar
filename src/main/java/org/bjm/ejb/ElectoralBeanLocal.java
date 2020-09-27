/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.LokSabha;
import org.bjm.model.LsCandidate;
import org.bjm.model.VsCandidate;
import org.bjm.model.User;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Local
public interface ElectoralBeanLocal {
    
    public List<String> getLSCandidates(int constituencyId);
    
    public List<String> getLSConstituencies(String stateCd);
    
    public int getLokSabhaIdByConstituency(String stateCode,String constituency);
    
    public LokSabha getLokSabhaByConstituency(String stateCode,String constituency);
    
    public int nominateNewLSCandidate(User user, LsCandidate lc);
    
    public void addNominationLSCandidate(User user, LsCandidate lc);
    
    public LsCandidate getLSCandidate(int constituencyId, String cName);
    
    public List<String> getVSCandidates(int constituencyId);
    
    public List<String> getVSConstituencies(String stateCd);
    
    public int getVidhanSabhaIdByConstituency(String stateCode,String constituency);
    
    public VidhanSabha getVidhanSabhaByConstituency(String stateCode,String constituency);
    
    public int nominateNewVSCandidate(User user, VsCandidate lc);
    
    public void addNominationVSCandidate(User user, VsCandidate lc);
    
    public VsCandidate getVSCandidate(int constituencyId, String cName);
    
    
    
}
