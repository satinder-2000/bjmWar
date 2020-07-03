/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.vo;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class SearchResultVO implements Serializable{
    
    
    private String id;
    private String title;
    private String description;
    private String created;
    private String type;
    private String subtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @Override
    public String toString() {
        return "SearchResultVO{" + "id=" + id + ", title=" + title + ", description=" + description + ", created=" + created + ", type=" + type + ", subtype=" + subtype + '}';
    }
    
    
    
    
      
    
}
