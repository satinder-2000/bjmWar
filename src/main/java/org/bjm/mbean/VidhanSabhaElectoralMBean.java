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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bjm.ejb.ElectoralBeanLocal;
import org.bjm.ejb.ElectoralRefDataLocal;
import org.bjm.model.LokSabha;
import org.bjm.model.VsCandidate;
import org.bjm.model.User;
import org.bjm.model.VidhanSabha;
import org.bjm.util.BJMConstants;

/**
 *
 * @author root
 */
@FlowScoped("VsNominate")
@Named(value = "vidhanSabhaElectoralMBean")
public class VidhanSabhaElectoralMBean implements Serializable {
    
     private static Logger LOGGER=Logger.getLogger(VidhanSabhaElectoralMBean.class.getName());
    
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
    private VsCandidate vsCandidate;
    private boolean newNomination;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        String stateCode=user.getStateCode();
        constituencies=erdl.getVidhanSabhas(stateCode);
        if (constituencies!=null){
            for (String c : constituencies) {
                if (c.equals(user.getConstituency().getConstituency())) {
                    constituency = c;
                    break;
                }
            }
        }else if (constituencies!=null && constituencies.size()>0){
           constituency=constituencies.get(0);
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
           constituency=rb.getString("noVSConstituency");
        }
        
        LOGGER.log(Level.INFO, "VidhanSabha contituenties loaded for State: {0}", stateCode);
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "Constituency is {0}", constituency);
        String stateCode=user.getStateCode();
        int vsId=ebl.getVidhanSabhaIdByConstituency(stateCode, constituency);
        candidates=ebl.getVSCandidates(vsId);
         LOGGER.log(Level.INFO, "candidates size is {0}", candidates.size());  
    }
    
    public String amendNomination(){
        return "VsNominate?faces-redirect=true";
    }
    
    public String processNomination(){
        if (!candidateNew.isEmpty()){//this will take precedence. TODO: Consider if a Flow is needed in case of similar name to the existing one, we can use Fizzy library.
            newNomination=true;
            vsCandidate=new VsCandidate();
            VidhanSabha vs=ebl.getVidhanSabhaByConstituency(user.getStateCode(), constituency);
            vsCandidate.setVidhanSabha(vs);
            vsCandidate.setName(candidateNew);
        }else{
            newNomination=false;
            int constituencyId=ebl.getVidhanSabhaIdByConstituency(user.getStateCode(), constituency);
            vsCandidate=ebl.getVSCandidate(constituencyId, candidateSelected);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nominationSuccess"), rb.getString("nominationSuccess")));
        return "VsNominateConfirm?faces-redirect=true";

        
        
    }
    
    public void submitNomination(){
        if(newNomination){
            VsCandidate newC=new VsCandidate();
            VidhanSabha vs=ebl.getVidhanSabhaByConstituency(user.getStateCode(), constituency);
            newC.setVidhanSabha(vs);
            newC.setName(candidateNew);
            int idC=ebl.nominateNewVSCandidate(user,newC);
            LOGGER.log(Level.INFO, "New VS Candidate nominated with ID : {0}", idC);
        }else{
            int constituencyId=ebl.getVidhanSabhaIdByConstituency(user.getStateCode(), constituency);
            VsCandidate vc=ebl.getVSCandidate(constituencyId, candidateSelected);
            ebl.addNominationVSCandidate(user,vc);
            LOGGER.log(Level.INFO, "Existing VS Candidate nominated with ID : {0}", vc.getId());
        }
    }
    
    public String getReturnValue() {
        submitNomination();
        return "/flowreturns/VsNominate-return?faces-redirect=true";
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

    public VsCandidate getLsCandidate() {
        return vsCandidate;
    }

    public void setLsCandidate(VsCandidate lsCandidate) {
        this.vsCandidate = lsCandidate;
    }

    
    
    
}
