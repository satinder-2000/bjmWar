/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "LOK_SABHA")
public class LokSabha implements Serializable {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "STATE_CODE")
    private String stateCode;

    @Column(name = "CONSTITUENCY")
    private String constituency;

    @OneToMany(mappedBy = "lokSabha")
    private List<LsCandidate> candidates;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "LOK_SABHA_ID")
    private List<VidhanSabha> vidhanSabhas;

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

    public List<LsCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<LsCandidate> candidates) {
        this.candidates = candidates;
    }

    public List<VidhanSabha> getVidhanSabhas() {
        return vidhanSabhas;
    }

    public void setVidhanSabhas(List<VidhanSabha> vidhanSabhas) {
        this.vidhanSabhas = vidhanSabhas;
    }
    
    
}
