/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model.view;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author root
 */
public class AbuseId implements Serializable {
    
    private String abuseType;
    
    private int commentVoteId;

    public AbuseId(String abuseType, int commentVoteId) {
        this.abuseType = abuseType;
        this.commentVoteId = commentVoteId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.abuseType);
        hash = 59 * hash + this.commentVoteId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbuseId other = (AbuseId) obj;
        return true;
    }
    
    
    
}
