/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.flow.FlowScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ElectoralBeanLocal;
import org.bjm.ejb.ElectoralRefDataLocal;
import org.bjm.model.LokSabha;
import org.bjm.model.LsCandidate;
import org.bjm.model.VsCandidate;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@FlowScoped("LsNominate")
@Named(value = "lokSabhaElectoralMBean")
public class LokSabhaElectoralMBean implements Serializable {
    
    private static Logger LOGGER=Logger.getLogger(LokSabhaElectoralMBean.class.getName());
    
    @Inject
    private ElectoralRefDataLocal erdl;
    
    @Inject
    private ElectoralBeanLocal ebl;
    
    private User user;
    
    private List<String> constituencies;
    private String constituency;
    private List<String> candidates;
    private String candidateSelected;
    private String candidateNew;
    private LsCandidate lsCandidate;
    private boolean newNomination;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        String stateCode=user.getStateCode();
        constituencies=erdl.getLokSabhas(stateCode);
        if (user.getConstituency()!=null){
          constituency=user.getConstituency().getConstituency();  
        }else{
         constituency=constituencies.get(0);
 
        }
        LOGGER.log(Level.INFO, "LokSabha contituenties loaded for State: {0}", stateCode);
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "Constituency is {0}", constituency);
        String stateCode=user.getStateCode();
        int lsId=ebl.getLokSabhaIdByConstituency(stateCode, constituency);
        candidates=ebl.getLSCandidates(lsId);
         LOGGER.log(Level.INFO, "candidates size is {0}", candidates.size());  
    }
    
    public String amendNomination(){
        return "LsNominate?faces-redirect=true";
    }
    
    public String processNomination(){
        if (!candidateNew.isEmpty()){//this will take precedence. TODO: Consider if a Flow is needed in case of similar name to the existing one, we can use Fizzy library.
            newNomination=true;
            lsCandidate=new LsCandidate();
            LokSabha ls=ebl.getLokSabhaByConstituency(user.getStateCode(), constituency);
            lsCandidate.setLokSabha(ls);
            lsCandidate.setName(candidateNew);
            //ebl.nominateNewLSCandidate(newC);
        }else{
            newNomination=false;
            int constituencyId=ebl.getLokSabhaIdByConstituency(user.getStateCode(), constituency);
            lsCandidate=ebl.getLSCandidate(constituencyId, candidateSelected);
            //.addNominationLSCandidate(lc);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nominationSuccess"), rb.getString("nominationSuccess")));
        return "LsNominateConfirm?faces-redirect=true";

        
        
    }
    
    public void submitNomination(){
        if(newNomination){
            LsCandidate newC=new LsCandidate();
            LokSabha ls=ebl.getLokSabhaByConstituency(user.getStateCode(), constituency);
            newC.setLokSabha(ls);
            newC.setName(candidateNew);
            int idC=ebl.nominateNewLSCandidate(user,newC);
            LOGGER.log(Level.INFO, "New LS Candidate nominated with ID : {0}", idC);
        }else{
            int constituencyId=ebl.getLokSabhaIdByConstituency(user.getStateCode(), constituency);
            LsCandidate lc=ebl.getLSCandidate(constituencyId, candidateSelected);
            ebl.addNominationLSCandidate(user,lc);
            LOGGER.log(Level.INFO, "Existing LS Candidate nominated with ID : {0}", lc.getId());
        }
    }
    
    public String getReturnValue() {
        submitNomination();
        return "/flowreturns/LsNominate-return?faces-redirect=true";
    }
    
    
    public List<String> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<String> constituencies) {
        this.constituencies = constituencies;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }

    public String getCandidateSelected() {
        return candidateSelected;
    }

    public void setCandidateSelected(String candidateSelected) {
        this.candidateSelected = candidateSelected;
    }

    public String getCandidateNew() {
        return candidateNew;
    }

    public void setCandidateNew(String candidateNew) {
        this.candidateNew = candidateNew;
    }

    public LsCandidate getLsCandidate() {
        return lsCandidate;
    }

    public void setLsCandidate(LsCandidate lsCandidate) {
        this.lsCandidate = lsCandidate;
    }

    
    
    
}
