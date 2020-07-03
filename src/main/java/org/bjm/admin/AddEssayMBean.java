/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.bjm.ejb.MiscellaneousServicesBeanLocal;
import org.bjm.model.Essay;

/**
 *
 * @author root
 */
@Named(value = "addEssayMBean")
@ViewScoped
public class AddEssayMBean implements Serializable {
    
    private  static Logger LOGGER=Logger.getLogger(AddEssayMBean.class.getName());
    
    @Inject
    private MiscellaneousServicesBeanLocal msbl;
    
    private Part part;
    
    private Essay essay;
    
    @PostConstruct
    public void init(){
        essay=new Essay();
    }
    
    public String addEssay(){
        try {
            //int fileSize=part.getInputStream().available();
            InputStream in = part.getInputStream();
            byte[] buff = new byte[8000];
            int bytesRead = 0;
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            while ((bytesRead = in.read(buff)) != -1) {
                bao.write(buff, 0, bytesRead);
            }
            byte[] data = bao.toByteArray();
            essay.setText(data);
            msbl.addEssay(essay);
            
        } catch (IOException ex) {
            Logger.getLogger(AddEssayMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "File added";
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }
    
    
    
    
    
    
    
}
