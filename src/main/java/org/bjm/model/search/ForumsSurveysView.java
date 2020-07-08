/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model.search;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author root
 */
@Entity
@Table(name = "FORUMS_SURVEYS")
public class ForumsSurveysView implements Serializable{
    
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    @Transient
    private String descriptionText;
    
    private LocalDateTime created; 
    
    @Transient
    private String createdStr;
    
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

    public String getDescriptionText() {
        //String s1=description.replaceAll("\"\\\\<.*?\\\\>\", \"\"", "");
        //String s2=s1.replaceAll("&amp;","&");
        //String s3=s2.replaceAll("&gt;",">");
        //String s4=s3.replaceAll("&lt;","<");
        
        descriptionText=description.replaceAll("\\<.*?>", "").replaceAll("&amp;","&").replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("Â","#").replaceAll("Ã.{5}", "'").replaceAll("&nbsp;", " ");;
        if (descriptionText.length()>=150){
            descriptionText=descriptionText.substring(0, 150);
        }
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }
    
    

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    

    public String getCreatedStr() {
        createdStr=created.getDayOfMonth()+"/"+created.getMonthValue()+"/"+created.getYear()+" "+created.getHour()+":"+created.getMinute();
        return createdStr;
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
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
    
    
    
    
    
}
