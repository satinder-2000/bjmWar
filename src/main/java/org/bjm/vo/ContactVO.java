/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.vo;

/**
 *
 * @author root
 */
public class ContactVO {
    
    private String senderEmail;
    private String subject;
    private String details;

    public ContactVO(String senderEmail, String subject, String details) {
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.details = details;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    
    
    
}
