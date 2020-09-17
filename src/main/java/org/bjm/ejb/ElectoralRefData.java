/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.map.MultiValueMap;
import org.bjm.model.LokSabha;
import org.bjm.model.VidhanSabha;

/**
 *
 * @author root
 */
@Stateful
@Startup
public class ElectoralRefData implements ElectoralRefDataLocal {
    
    private static Logger LOGGER=Logger.getLogger(ElectoralRefData.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    private MultiValueMap lokSabhaMap= new MultiValueMap();
    private MultiValueMap vidhanSabhaMap= new MultiValueMap();
    
    
    @PostConstruct
    public void init(){
        TypedQuery<LokSabha> tQ1=em.createQuery("select l from LokSabha l", LokSabha.class);
        List<LokSabha> listLS=tQ1.getResultList();
        listLS.forEach(ls -> {
            lokSabhaMap.put(ls.getStateCode(), ls.getConstituency());
        });
        LOGGER.log(Level.INFO, "LokSabha loaded {0}", lokSabhaMap.size());
        
        
        TypedQuery<VidhanSabha> tQ2=em.createQuery("select v from VidhanSabha v", VidhanSabha.class);
        List<VidhanSabha> listVS=tQ2.getResultList();
        listVS.forEach(vs -> {
            vidhanSabhaMap.put(vs.getStateCode(), vs.getConstituency());
        });
        LOGGER.log(Level.INFO, "VidhanSabha loaded {0}", vidhanSabhaMap.size());
        
    }

    @Override
    public List<String> getLokSabhas(String stateCode) {
        Collection<String> coll=lokSabhaMap.getCollection(stateCode);
        return new ArrayList(coll);
    }

    @Override
    public List<String> getVidhanSabhas(String stateCode) {
        Collection<String> coll=vidhanSabhaMap.getCollection(stateCode);
        if (coll==null){
            return null;//No data found with this State Code. Could be a UT such as Chandigarh
        }else{
            return new ArrayList(coll);
        }
    }

    

    
}
