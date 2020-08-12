/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb.timer;

import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author root
 */
@Stateless
public class ShowCurrentTime {
    
    //@Schedule(second = "*", minute = "*", hour="*", persistent = false)
    public void showTime(){
        System.out.println(new Date());
    }
    
}
