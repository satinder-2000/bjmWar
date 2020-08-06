/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.bjm.model.search.ForumsSurveysView;

/**
 *
 * @author root
 */
@Stateless
@Startup
public class IndexTimer {

    static final Logger LOGGER = Logger.getLogger(IndexTimer.class.getName());
    
    //@Resource
    //SessionContext sessionContext;

    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Resource(name = "indexDir")
    private String indexDir;
    
    @Resource(name = "maxResults")
    private Integer maxResults;
    
    //private IndexWriter writer;
    private List<ForumsSurveysView> forumsSurveys;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void init() {
        //sessionContext.getTimerService().c
        final TimerConfig createIndex = new TimerConfig("createIndex", false);
        timerService.createCalendarTimer(new ScheduleExpression().minute("*/1"), createIndex);
        LOGGER.info("IndexTimer created..");

    }

    @Timeout
    public void timeout(Timer timer) {
        LOGGER.log(Level.INFO, "Building Index at {0}", new Date());
        try {
            createIndex();
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
        timer.cancel();
    }
    
    
    
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
}

    
