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
import javax.persistence.Table;

/**
 *
 * @author root
 */

@Entity
@Table(name = "STATE")
public class State implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "CODE")
    private String code;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="POST_CODE_PREFIX")
    private String postCodePrefix;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostCodePrefix() {
        return postCodePrefix;
    }

    public void setPostCodePrefix(String postCodePrefix) {
        this.postCodePrefix = postCodePrefix;
    }
    
    
    
    
    
    
    
}
