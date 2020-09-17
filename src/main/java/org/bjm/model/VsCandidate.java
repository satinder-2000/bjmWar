/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "VS_CANDIDATE")
public class VsCandidate implements Serializable {
    
    @Id
    @Column(name = "ID")
    private int id;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="REPRESENT")
    private boolean represent;
    
    @Column(name="NAME_CONF_COUNT")
    private int nameConfCount;
    
    
    @ManyToOne
    @JoinColumn(name = "VIDHAN_SABHA_ID")
    private VidhanSabha vidhanSabha;

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

    public VidhanSabha getVidhanSabha() {
        return vidhanSabha;
    }

    public void setVidhanSabha(VidhanSabha vidhanSabha) {
        this.vidhanSabha = vidhanSabha;
    }

    
    
    
    
    
}
