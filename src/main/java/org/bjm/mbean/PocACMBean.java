/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.PocACBeanLocal;
import org.bjm.model.LokSabha;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@ViewScoped
@Named(value = "pocACMBean")
public class PocACMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(PocACMBean.class.getName());
    
    @Inject
    private PocACBeanLocal pacbl;
    
    private String stateCd;
    private List<LokSabha> lsList;
    private List<VidhanSabha> vsList;
    private List<String> lSStr;
    private List<String> vSStr;
    private List<String> stateCodes;
    private String lsSelected;
    private String vsSelected;
    
    @PostConstruct
    public void init(){
        lsList=pacbl.getLokSabhas(stateCd);
        vsList=pacbl.getVidhanSabhas(stateCd);
        lSStr=new ArrayList<>();
        lSStr.add("Choose One");
        vSStr=new ArrayList<>();
        vSStr.add("Choose One");
        for (LokSabha l:lsList){
            lSStr.add(l.getConstituency());
        }
        for (VidhanSabha v:vsList){
            vSStr.add(v.getConstituency());
        }
        stateCodes=pacbl.getStateCodes();
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "State Code is {0}", stateCd);
        lsList=pacbl.getLokSabhas(stateCd);
        LOGGER.log(Level.INFO, "LS Constituencies loaded for {0} and {1} in number", new Object[]{stateCd, lsList.size()});
        vsList=pacbl.getVidhanSabhas(stateCd);
        LOGGER.log(Level.INFO, "VS Constituencies loaded for {0} and {1} in number", new Object[]{stateCd, lsList.size()});
    }
    
    /*public void ajaxTypeListenerVS(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "State Code is {0}", stateCd);
        vsList=pacbl.getVidhanSabhas(stateCd);
        LOGGER.log(Level.INFO, "VS Constituencies loaded for {0} and {1} in number", new Object[]{stateCd, lsList.size()});
    }*/
    
    public List<String> autoLs(String query){
        LOGGER.info("Size of lSStr is "+lSStr.size());
        List<String> results = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            for (String s : lSStr){
                if(s.startsWith(query)){
                    results.add(s);
                }
            }
        }
        return results;
    }
    
    public List<String> autoVs(String query){
        LOGGER.info("Size of vSStr is "+vSStr.size());
        List<String> results = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            for (String s : vSStr){
                if(s.startsWith(query)){
                    results.add(s);
                }
            }
        }
        return results;
    }

    public List<String> getlSStr() {
        return lSStr;
    }

    public void setlSStr(List<String> lSStr) {
        this.lSStr = lSStr;
    }

    public List<String> getvSStr() {
        return vSStr;
    }

    public void setvSStr(List<String> vSStr) {
        this.vSStr = vSStr;
    }

    

    public String getStateCd() {
        return stateCd;
    }

    public void setStateCd(String stateCd) {
        this.stateCd = stateCd;
    }

    public String getLsSelected() {
        return lsSelected;
    }

    public void setLsSelected(String lsSelected) {
        this.lsSelected = lsSelected;
    }

    public String getVsSelected() {
        return vsSelected;
    }

    public void setVsSelected(String vsSelected) {
        this.vsSelected = vsSelected;
    }

    public List<String> getStateCodes() {
        return stateCodes;
    }

    public void setStateCodes(List<String> stateCodes) {
        this.stateCodes = stateCodes;
    }

    public List<LokSabha> getLsList() {
        return lsList;
    }

    public void setLsList(List<LokSabha> lsList) {
        this.lsList = lsList;
    }

    public List<VidhanSabha> getVsList() {
        return vsList;
    }

    public void setVsList(List<VidhanSabha> vsList) {
        this.vsList = vsList;
    }
    
    
    
    
    
}
