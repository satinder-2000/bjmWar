/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model.view;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 *
 * @author root
 */
@Entity(name = "ABUSE")
@IdClass(AbuseId.class)
public class AbuseReport implements Serializable {
    
    @Id
    @Column(name = "ABUSE_TYPE")
    private String abuseType;
    
    @Id
    @Column(name = "COMMENT_VOTE_ID")
    private int commentVoteId;
    
    @Column(name = "REPORTED_ON")
    private LocalDateTime reportedOn;
    
    @Column(name = "REPORTED_BY_ID")
    private int reportedById;
    
    @Column(name = "REPORT_TEXT")
    private String reportText;

    public String getAbuseType() {
        return abuseType;
    }

    public void setAbuseType(String abuseType) {
        this.abuseType = abuseType;
    }

    public int getCommentVoteId() {
        return commentVoteId;
    }

    public void setCommentVoteId(int commentVoteId) {
        this.commentVoteId = commentVoteId;
    }

    public LocalDateTime getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(LocalDateTime reportedOn) {
        this.reportedOn = reportedOn;
    }

    public int getReportedById() {
        return reportedById;
    }

    public void setReportedById(int reportedById) {
        this.reportedById = reportedById;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }
    
    
    
    
    
    
}
