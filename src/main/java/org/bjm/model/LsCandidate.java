/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "LS_CANDIDATE")
public class LsCandidate implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="REPRESENT")
    private boolean represent;
    
    @Column(name="NAME_CONF_COUNT")
    private int nameConfCount;
    
    
    @ManyToOne
    @JoinColumn(name = "LOK_SABHA_ID")
    private LokSabha lokSabha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRepresent() {
        return represent;
    }

    public void setRepresent(boolean represent) {
        this.represent = represent;
    }

    public int getNameConfCount() {
        return nameConfCount;
    }

    public void setNameConfCount(int nameConfCount) {
        this.nameConfCount = nameConfCount;
    }

    public LokSabha getLokSabha() {
        return lokSabha;
    }

    public void setLokSabha(LokSabha lokSabha) {
        this.lokSabha = lokSabha;
    }
    
    
    
    
}
