/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.bjm.model.search.ForumsSurveysView;

/**
 *
 * @author root
 */
@Stateful
@Startup
public class SearchEngineBean implements SearchEngineBeanLocal, Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(SearchEngineBean.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Resource(name = "indexDir")
    private String indexDir;
    
    @Resource(name = "maxResults")
    private Integer maxResults;
    
    //private IndexWriter writer;
    private List<ForumsSurveysView> forumsSurveys;

    @PostConstruct
    public void init(){
        try {
            createIndex();
            LOGGER.info("SearchEngineBean loaded ");
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    @Override
    public void createIndex() throws IOException{
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDirectory, iwc);
        writer.deleteAll();
        forumsSurveys=new ArrayList<>();
        TypedQuery<ForumsSurveysView> tQ=em.createQuery("select f from ForumsSurveysView f", ForumsSurveysView.class);
        forumsSurveys.addAll(tQ.getResultList());
        //Now create Index from Data
        for (ForumsSurveysView sfv: forumsSurveys){
            Document doc = new Document();
            StringBuilder sb=new StringBuilder();
            doc.add(new TextField("ID", sfv.getId(), TextField.Store.YES));
            sb.append(sfv.getId()).append(" ");
            doc.add(new TextField("TITLE", sfv.getTitle(), TextField.Store.YES));
            sb.append(sfv.getTitle()).append(" ");
            doc.add(new TextField("DESCRIPTION", sfv.getDescription().replace("<p>", "").replace("</p>", ""), TextField.Store.YES));
            sb.append(sfv.getDescription()).append(" ");
            doc.add(new TextField("TYPE", sfv.getType(), TextField.Store.YES));
            sb.append(sfv.getType()).append(" ");
            doc.add(new TextField("SUBTYPE", sfv.getSubtype(), TextField.Store.YES));
            sb.append(sfv.getSubtype()).append(" ");
            doc.add(new TextField("CREATEDSTR", sfv.getCreatedStr(), TextField.Store.YES));
            sb.append(sfv.getCreatedStr()).append(" ");
            doc.add(new TextField("TEXT", sb.toString(), TextField.Store.YES));
            writer.addDocument(doc);
        }
        
        LOGGER.info("Index Created with NumDocs = "+writer.numRamDocs());
        writer.close();
        
    }
    
    

    @Override
    public List<ForumsSurveysView> getSearchResult(String queryStr) {
        List<ForumsSurveysView> toReturn=new ArrayList<>();
        try {
            Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));
            IndexReader reader = DirectoryReader.open(indexDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser("TEXT", new StandardAnalyzer());
            Query q=queryParser.parse(queryStr);
            TopDocs docs= indexSearcher.search(q, maxResults);
            for (ScoreDoc sd: docs.scoreDocs){
                ForumsSurveysView fsv=new ForumsSurveysView();
                Document doc = indexSearcher.doc(sd.doc);
                fsv.setId(doc.get("ID"));
                fsv.setTitle(doc.get("TITLE"));
                fsv.setDescription(doc.get("DESCRIPTION"));
                fsv.setType(doc.get("TYPE"));
                fsv.setSubtype(doc.get("SUBTYPE"));
                fsv.setCreatedStr(doc.get("CREATEDSTR"));
                toReturn.add(fsv);
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchEngineBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SearchEngineBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return toReturn;
    }
    
    
    
}
