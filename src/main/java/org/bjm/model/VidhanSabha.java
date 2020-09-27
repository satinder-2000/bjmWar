/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "VIDHAN_SABHA")
public class VidhanSabha implements Serializable {
    
    @Id
    @Column(name = "ID")
    private int id;
    
    @Column(name = "STATE_CODE")
    private String stateCode;
    
    @Column(name = "CONSTITUENCY")
    private String constituency;
    
    @OneToMany(mappedBy = "vidhanSabha")
    private List<VsCandidate> candidates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public List<VsCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<VsCandidate> candidates) {
        this.candidates = candidates;
    }
    
    

}
