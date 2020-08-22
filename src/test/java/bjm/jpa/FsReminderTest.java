/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bjm.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 *
 * @author root
 */
public class FsReminderTest {
    
    private static final String PERSISTENCE_UNIT_NAME = "bjmPUTest";
    private EntityManagerFactory factory;
    
    public static void main(String[] args){
        FsReminderTest test=new FsReminderTest();
        test.factory=Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = test.factory.createEntityManager();
    }
    
}
