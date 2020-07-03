/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.bjm.ejb.SearchEngineBeanLocal;
import org.bjm.model.search.ForumsSurveysView;
import org.bjm.vo.SearchResultVO;

/**
 *
 * @author root
 */
@Named(value = "searchMBean")
@SessionScoped
public class SearchMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(SearchMBean.class.getName());
    
    @Inject
    private SearchEngineBeanLocal sebl;
    
    private String query;
    private String queryCopy;
    
    private List<ForumsSurveysView> results;
    
    public String search() {
        results=new ArrayList<>();
        if (query==null || query.isEmpty()){
            query="*:*";
        }
        queryCopy=query;
        results=sebl.getSearchResult(queryCopy);
        //query=null;//Imp else it will cached as the MBean is session scoped.
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path=request.getPathInfo();
        String viewId= request.getParameter("vuId");
        queryCopy=null;
        String toReturn=null;
        if (viewId.startsWith("/index.xhtml")){
            toReturn="SearchResults.xhtml?faces-redirect=true";
        }else{
            toReturn="/SearchResults.xhtml?faces-redirect=true";
        }
        return toReturn;
    }
    
    public String generateDetails(){
        String toReturn=null;
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String itemIdStr=request.getParameter("itemId");
        if (itemIdStr.startsWith("F")){//redirect to Forum Details
            String forumId=itemIdStr.substring(1);
            //toReturn=request.getContextPath().concat("/forum/ForumDetails.xhtml?faces-redirect=true&forumId=").concat(forumId);
            toReturn="forum/ForumDetails.xhtml?faces-redirect=true&forumId=".concat(forumId);
        }else if (itemIdStr.startsWith("S")){//redirect to Survey Details
            String surveyId=itemIdStr.substring(1);
            //toReturn=request.getContextPath().concat("/survey/SurveyDetails.xhtml?faces-redirect=true&surveyId=").concat(surveyId);
            toReturn="survey/SurveyDetails.xhtml?faces-redirect=true&surveyId=".concat(surveyId);
        }else {//Not found page. Though it will never happen
            toReturn=request.getContextPath().concat("/error404.xhtml?faces-redirect=true");
        }
        return toReturn;
    }
    

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ForumsSurveysView> getResults() {
        this.query="";
        return results;
    }

    public void setResults(List<ForumsSurveysView> results) {
        this.results = results;
    }
}
