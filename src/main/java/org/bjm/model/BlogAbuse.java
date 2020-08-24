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
import org.bjm.model.Forum;
import org.bjm.model.ForumComment;

/**
 *
 * @author root
 */
@Entity
@Table(name = "BLOG_ABUSE")
public class BlogAbuse implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "REPORT_TEXT")
    private String reportText;
    
    @Column(name = "REPORTED_ON")
    private LocalDateTime reportedOn;
    
    @ManyToOne
    @JoinColumn(name = "BLOG_COMMENT_ID")
    private BlogComment blogComment;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User reportedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public LocalDateTime getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(LocalDateTime reportedOn) {
        this.reportedOn = reportedOn;
    }

    public BlogComment getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(BlogComment blogComment) {
        this.blogComment = blogComment;
    }

    

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }
    
    
    
    
    
}
