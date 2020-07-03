/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.model.Survey;

/**
 *
 * @author root
 */
@Named(value = "surveysMBean")
@ViewScoped
public class SurveysMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(SurveysMBean.class.getName());
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;
    
    
    public List<Survey> getSurveys(){
        return surveyBeanLocal.getSurveys();
    }
    
}
