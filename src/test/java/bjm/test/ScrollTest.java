/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bjm.test;

import org.bjm.mbean.TempImageScroll;

/**
 *
 * @author root
 */
public class ScrollTest {
    
    public static void main(String[] args){
        TempImageScroll scroll=new TempImageScroll();
        /*do{
           System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
            scroll.next(); 
        }while(!scroll.isNextDisabled());*/
        int size=scroll.getSize();
        /*for (int i=1; i<=size; i++){
            
        }
        for (int i=size; i>=0; i--){
            System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
            scroll.prev();
        }*/
        
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.next();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        scroll.prev();
        System.out.println(scroll.getIndex()+" isNextDisabled="+scroll.isNextDisabled()+" isPrevDisabled="+scroll.isPrevDisabled());
        
        
    }
    
}
