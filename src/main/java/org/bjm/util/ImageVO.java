/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.util;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class ImageVO implements Serializable {
    
    private String imgType;
    
    public byte[] image;
    
    public ImageVO(String imgType,byte[] image){
        this.imgType=imgType;
        this.image=image;
        
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    
    
    
    
}
