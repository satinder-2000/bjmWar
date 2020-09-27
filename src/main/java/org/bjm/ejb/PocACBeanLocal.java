/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.LokSabha;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Local
public interface PocACBeanLocal {
    
    public List<LokSabha> getLokSabhas(String stateCd);
    
    public List<VidhanSabha> getVidhanSabhas(String stateCd);
    
    public List<String> getStateCodes();
    
    
    
}
