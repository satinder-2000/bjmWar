/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.AutoCompleteBean;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Named
@SessionScoped
public class AutoCompleteMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(AutoCompleteMBean.class.getName());
    
    private static final int MAX_RESULTS=10;
    
    
    private String stateCode="UP";
    private String constituency;
    
    private List<VidhanSabha> results;
    
    
    @Inject
    private AutoCompleteBean acb;
    
    
    public List<String> completeVidhanSabhas(String input){
        LOGGER.log(Level.INFO, "Constituency is : {0}", input);
        /*results=acb.getVidhanSabhas(stateCode, input, MAX_RESULTS);
        List<String> resultsStr=new ArrayList<>();
        results.forEach(vs -> {
            resultsStr.add(vs.getConstituency());
        });*/
        
        List<String> resultsStr=new ArrayList();
        for (int i = 0; i < MAX_RESULTS; i++) {
            resultsStr.add(acb.completeString(input));
        }
        return resultsStr;
    }
    
    public String submit(){
        LOGGER.log(Level.INFO, "Submit cConstituency is : {0}", constituency);
        return null;
        
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }
    
    
    
    
}
