/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author root
 */

@Entity
@Table(name = "BLOG_COMMENT")
public class BlogComment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "TEXT")
    private String text;
    
    @Column(name="DATED")
    private LocalDateTime dated;
    
    @Transient
    private String  datedStr;
    
    @ManyToOne
    @JoinColumn(name = "BLOG_ID")
    private Blog blog;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDated() {
        return dated;
    }

    public void setDated(LocalDateTime dated) {
        this.dated = dated;
    }

    public String getDatedStr() {
        datedStr=dated.getDayOfMonth()+"/"+dated.getMonthValue()+"/"+dated.getYear()+" "+dated.getHour()+":"+dated.getMinute();
        return datedStr;
    }

    public void setDatedStr(String datedStr) {
        this.datedStr = datedStr;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
    
}
