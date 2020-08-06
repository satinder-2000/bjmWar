/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
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
@Table(name = "SURVEY")
public class Survey implements Serializable {
    
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
    
    @Column(name = "DATED")
    private LocalDateTime dated;
    
    @Transient
    private String datedStr;
    
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name="SURVEY_CATEGORY_ID")
    private SurveyCategory surveyCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SurveyVote> surveyVotes;

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
        if (description!=null){
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
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SurveyVote> getSurveyVotes() {
        return surveyVotes;
    }

    public void setSurveyVotes(List<SurveyVote> surveyVotes) {
        this.surveyVotes = surveyVotes;
    }

    public SurveyCategory getSurveyCategory() {
        return surveyCategory;
    }

    public void setSurveyCategory(SurveyCategory surveyCategory) {
        this.surveyCategory = surveyCategory;
    }

    
    
    
    
}
