/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.IOException;
import java.util.List;
import javax.ejb.Local;
import org.bjm.model.search.ForumsSurveysView;

/**
 *
 * @author root
 */
@Local
public interface SearchEngineBeanLocal {
    
    public void createIndex() throws IOException;
    
    public List<ForumsSurveysView> getSearchResult(String query);
    
}
