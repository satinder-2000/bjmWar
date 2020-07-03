/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author root
 */

@Entity
@Table(name = "SURVEY_VOTE")
public class SurveyVote implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "VOTE_TYPE")
    private VoteType voteType;
    
    @Column(name = "COMMENT")
    private String comment;
    
    @Column(name = "DATED")
    private LocalDateTime dated;
    
    @Transient
    private String datedStr;
    
    @Transient
    private Date dateCommented;
    
    @ManyToOne
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
    
    

    public Date getDateCommented() {
        return dateCommented;
    }

    public void setDateCommented(Date dateCommented) {
        this.dateCommented = dateCommented;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
    
    
    
}
