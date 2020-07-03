/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import javax.ejb.Local;
import org.bjm.model.Visitor;

/**
 *
 * @author root
 */
@Local
public interface VisitorBeanLocal {
    
    @Deprecated
    public boolean performIPCheckIfSaved(String ipAddress);
    
    public Visitor getVisitor(String ipAddress);

    public void saveVisitor(Visitor visitor);

    public Visitor updateVisitor(Visitor visitor);
    
}
