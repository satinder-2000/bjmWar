/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author root
 */
@Entity
@Table(name = "FORUM")
public class Forum implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "TITLE")
    private String title;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Transient
    private String descriptionText;
    
    @Column(name = "CREATED")
    private LocalDateTime created;
    
    @Transient
    private String createdStr;
    
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name="FORUM_CATEGORY_ID")
    private ForumCategory forumCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "forum")
    private List<ForumComment> forumComments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (description!=null){
           description=description.replaceAll("Ã.{5}", "'"); 
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescriptionText() {
        if (description != null) {
            descriptionText = description.replaceAll("\\<.*?>", "").replaceAll("&amp;", "&").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("Â", "#").replaceAll("Ã.{5}", "'").replaceAll("&nbsp;", " ");;
            if (descriptionText.length() >= 150) {
                descriptionText = descriptionText.substring(0, 150);
            }
        }
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedStr() {
        createdStr=created.getDayOfMonth()+"/"+created.getMonthValue()+"/"+created.getYear()+" "+created.getHour()+":"+created.getMinute();
        return createdStr;
    }
    
    

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ForumComment> getForumComments() {
        return forumComments;
    }

    public void setForumComments(List<ForumComment> forumComments) {
        this.forumComments = forumComments;
    }
    
    
    
            
       
}
