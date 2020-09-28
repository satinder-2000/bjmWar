/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author root
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DOB")
    private LocalDate dob;
    
    @Transient
    private String dobStr;
    
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "MOBILE")
    private String mobile;
    
    @Column(name = "PROFILE_FILE")
    private String profileFile;
    
    @Column(name = "IMAGE")
    private byte[] image;
    
    @Column(name = "STATE_CODE")
    private String stateCode;
    
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    
    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;
    
    @Column(name = "PASSWORD")
    private String password="";

    @Transient
    private String passwordConfirm;
    
    @Column(name = "FAILED_ATTEMPTS")
    private int failedAttempts;
    
    @Column(name="FS_REMINDER")
    private int fsReminder;
    
    @Column(name="LAST_FS_REMINDER")
    private LocalDateTime lastFsReminder;
    
    @OneToOne()
    @JoinColumn(name = "LOK_SABHA_ID", referencedColumnName = "ID")
    private LokSabha constituency;
    
    
    
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY
    )
    private List<Survey> userSurveys=new ArrayList<>();
    
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ForumComment> forumComments;
    
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SurveyVote> surveyVotes;
    
    
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY
    )
    private List<Forum> userForums=new ArrayList<>();
    
    
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY
    )
    private List<Blog> userBlogs=new ArrayList();
    
     @Transient
    private String name;
    
    @Transient
    private String emailOrg;
    
     @Transient
    private String exceptionMsg;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getDobStr() {
        if (dob!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            dobStr=dob.format(formatter);
        }
        return dobStr;
    }

    public void setDobStr(String dobStr) {
        this.dobStr = dobStr;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(String profileFile) {
        this.profileFile = profileFile;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    
    

    public List<Survey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(List<Survey> userSurveys) {
        this.userSurveys = userSurveys;
    }

    public List<Forum> getUserForums() {
        return userForums;
    }

    public void setUserForums(List<Forum> userForums) {
        this.userForums = userForums;
    }

    public List<ForumComment> getForumComments() {
        return forumComments;
    }

    public void setForumComments(List<ForumComment> forumComments) {
        this.forumComments = forumComments;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public List<SurveyVote> getSurveyVotes() {
        return surveyVotes;
    }

    public void setSurveyVotes(List<SurveyVote> surveyVotes) {
        this.surveyVotes = surveyVotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailOrg() {
        return emailOrg;
    }

    public void setEmailOrg(String emailOrg) {
        this.emailOrg = emailOrg;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public int getFsReminder() {
        return fsReminder;
    }

    public void setFsReminder(int fsReminder) {
        this.fsReminder = fsReminder;
    }

    public LocalDateTime getLastFsReminder() {
        return lastFsReminder;
    }

    public void setLastFsReminder(LocalDateTime lastFsReminder) {
        this.lastFsReminder = lastFsReminder;
    }

    public LokSabha getConstituency() {
        return constituency;
    }

    public void setConstituency(LokSabha constituency) {
        this.constituency = constituency;
    }
    
    
    

}
