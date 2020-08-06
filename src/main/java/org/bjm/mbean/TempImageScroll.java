/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * THIS IS A TEMPORARY CLASS. IN ACTUAL WE NEED TO USE BOOTSTRAP 4 CAROUSEL
 * @author root
 */

@Named
@SessionScoped
public class TempImageScroll implements Serializable{
    
    
    private int size=16;
    private int index=1;
    
    private boolean prevDisabled=true;
    private boolean nextDisabled=false;
    
    
    public String prev(){
        --index;
        if (index==1){
            nextDisabled=false;
            prevDisabled=true;
        }else{
            nextDisabled=false;
            prevDisabled=false;
        }
        return ""+index;
    }
    
    public String next(){
        ++index;
        if (index>=size){
            nextDisabled=true;
            prevDisabled=false;
            return ""+index;
        }else{
            nextDisabled=false;
            prevDisabled=false;
            return ""+index;
        }
    }

    public boolean isPrevDisabled() {
        return prevDisabled;
    }

    public void setPrevDisabled(boolean prevDisabled) {
        this.prevDisabled = prevDisabled;
    }

    public boolean isNextDisabled() {
        return nextDisabled;
    }

    public void setNextDisabled(boolean nextDisabled) {
        this.nextDisabled = nextDisabled;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
    
    
}
