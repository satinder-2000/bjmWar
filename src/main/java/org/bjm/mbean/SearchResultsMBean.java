/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.bjm.ejb.SearchEngineBeanLocal;
import org.bjm.model.search.ForumsSurveysView;

/**
 *
 * @author root
 */
@Named(value = "searchResultsMBean")
public class SearchResultsMBean {
    
    private static final Logger LOGGER=Logger.getLogger(SearchResultsMBean.class.getName());
    
    @Inject
    private SearchEngineBeanLocal sebl;
    String query=null;
    
    private List<ForumsSurveysView> fsList;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        query=request.getParameter("query");
        if (query==null)query="*:*";
        fsList=sebl.getSearchResult(query);
        LOGGER.log(Level.INFO, "Query executed. Result Count is : {0}", fsList.size());
    }

    public List<ForumsSurveysView> getFsList() {
        return fsList;
    }

    public void setFsList(List<ForumsSurveysView> fsList) {
        this.fsList = fsList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    

    
}
