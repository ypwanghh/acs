/*
 * Copyright (c) 2014 MOOD, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of MOOD, LLC.
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with MOOD.
 *  
 * Create Date : Nov 30, 2014 9:37:22 PM
 */
package com.mood.acs.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mood.acs.util.AcsContext;
import com.mood.acs.util.AcsUtil;

/**
 * ClassName: LuceneProvider <br/>
 * Function: TODO Add Function for this Class here. <br/>
 * Date: Nov 30, 2014 9:37:22 PM <br/>
 * 
 * @author WangJam
 * @version $Revision:$
 * @change $Change:$
 * @lastestModifier $Author:$
 */
public class LuceneProvider {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        try {
            LuceneProvider t = new LuceneProvider();
            t.createIndex();
            searchByKeyWords("JAVA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createIndex() throws Exception {
        Long startTime = System.currentTimeMillis();
        File indexDir = new File(AcsContext.FILE_INDEX);
        Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, luceneAnalyzer);
        config.setOpenMode(OpenMode.CREATE);
        Directory directory = FSDirectory.open(indexDir);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        createDocument(indexWriter, setUpFileTargetMap());
        indexWriter.close(true);
        Long endTime = System.currentTimeMillis();
        System.out.println("Take " + (endTime - startTime) + " milliseconds to create index files.");
    }

    /**
     * User could setup file target at this map.<br/>
     * 
     * @author WangJam
     * @return
     */ 
    private Map<String, String[]> setUpFileTargetMap() {
        Map<String, String[]> fileTargetMap = new HashMap<String, String[]>();
        String[] codePathMain = new String[] { AcsContext.RO_FILE_TARGET_999, AcsContext.RO_FILE_TARGET_999_DB };
        String[] codePath400 = new String[] { AcsContext.RO_FILE_TARGET_400 };
        String[] codePath350 = new String[] { AcsContext.RO_FILE_TARGET_350 };
        String[] codePath345 = new String[] { AcsContext.RO_FILE_TARGET_345 };
        fileTargetMap.put("RO999", codePathMain);
        fileTargetMap.put("RO400", codePath400);
        fileTargetMap.put("RO350", codePath350);
        fileTargetMap.put("RO340", codePath345);
        return fileTargetMap;
    }

    /**
     * Create index document<br/>
     * 
     * @author WangJam
     * @param indexWriter
     * @throws Exception
     */
    private void createDocument(IndexWriter indexWriter, Map<String, String[]> fileTargetMap) throws Exception {
        Iterator<Entry<String, String[]>> itor = fileTargetMap.entrySet().iterator();
        while (itor.hasNext()) {
            Entry<String, String[]> e = itor.next();
            addFieldsForDoc(indexWriter, e.getValue(), e.getKey().substring(0, 2), e.getKey().substring(2, 5));
        }
    }

    /**
     * add fields to index document.<br/>
     * 
     * @author WangJam
     * @param indexWriter
     * @throws Exception
     */ 
    private void addFieldsForDoc(IndexWriter indexWriter, String[] filePathArr, String prodCode, String version) throws Exception {
        List<File> fileList = new ArrayList<File>();
        for (String filePath : filePathArr) {
            AcsUtil.listFile(new File(filePath), fileList);
        }
        if (fileList != null && fileList.size() > 0) {
            for (File file : fileList) {
                if (AcsUtil.validFileType4Index(file.getAbsolutePath())) {
                    String content = AcsUtil.readFile(file.getAbsolutePath());
                    System.out.println("File Name: " + file.getName() + " path: " + file.getAbsolutePath() + " is indexing....");
                    Document doc = new Document();
                    doc.add(new TextField("filePath", file.getAbsolutePath(), Field.Store.YES));
                    doc.add(new TextField("id", file.getName(), Field.Store.YES));
                    doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
                    doc.add(new TextField("size", file.getTotalSpace() + "b", Field.Store.YES));
                    doc.add(new TextField("fileType", AcsUtil.identifyFileType(file.getAbsolutePath()).toUpperCase(), Field.Store.YES));
                    doc.add(new TextField("prod", prodCode, Field.Store.YES));
                    doc.add(new TextField("pid", "1", Field.Store.YES));
                    doc.add(new TextField("prodCode", prodCode, Field.Store.YES));
                    doc.add(new TextField("version", version, Field.Store.YES));
                    doc.add(new TextField("fileContents", content, Field.Store.YES));
                    indexWriter.addDocument(doc);
                }
            }
            indexWriter.commit();
        }
    }

    public static ScoreDoc[] searchByKeyWords(String keyWords) throws Exception {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(AcsContext.FILE_INDEX)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44, AcsContext.MY_ENGLISH_STOP_WORDS_SET);
        QueryParser parser = new QueryParser(Version.LUCENE_44, "fileContents", analyzer);
        parser.setAllowLeadingWildcard(true);
        parser.setAutoGeneratePhraseQueries(true);
        parser.setLowercaseExpandedTerms(true);
        parser.setPhraseSlop(0);
        Query query = parser.parse(keyWords + " AND version:999");
        TopDocs results = searcher.search(query, 1000);
        ScoreDoc[] score = results.scoreDocs;
        if (score.length == 0) {
            System.out.println("Sorry, no result for your searching.");
        } else {
            System.out.println("Search [" + keyWords + "] get " + score.length + " results");
        }
        return score;
    }
    
    /**
     * delete index folder
     * 
     * @throws Exception
     */
    public void deleteIndexs() throws Exception {
        System.out.println("----------Delete index file begin----------");
        Long startTime = System.currentTimeMillis();

        File indexDir = new File(AcsContext.FILE_INDEX);
        Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, luceneAnalyzer);
        config.setOpenMode(OpenMode.CREATE);
        Directory directory = FSDirectory.open(indexDir);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteAll();
        indexWriter.commit();
        indexWriter.close(true);
        
        Long endTime = System.currentTimeMillis();
        System.out.println("Take " + (endTime - startTime) + " milliseconds to delete index files.");
        System.out.println("----------Delete index file end----------");
    }

    /**
     * delete index folder
     * 
     * @throws Exception
     */
    boolean flag;
    File file;
    public void deleteFolder() throws Exception {
        System.out.println("----------Delete index file begin----------");
        Long startTime = System.currentTimeMillis();
        this.deleteFolder(AcsContext.FILE_INDEX);
        Long endTime = System.currentTimeMillis();
        System.out.println("Take " + (endTime - startTime) + " milliseconds to delete index files.");
        System.out.println("----------Delete index file end----------");
    }

    public boolean deleteFolder(String sPath) {
        flag = false;
        file = new File(sPath);
        if (!file.exists()) {
            return flag;
        } else {
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {
                return deleteDirectory(sPath);
            }
        }
    }

    public boolean deleteFile(String sPath) {
        flag = false;
        file = new File(sPath);
        if (file.isFile() && file.exists()) {
            System.out.println("delete file: " + file.getAbsolutePath() + file.getName());
            file.delete();
            flag = true;
        }
        return flag;
    }

    public boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public void runbat() {
        System.out.println("----------Run File Update Begin----------");
        try {
            Process ps = Runtime.getRuntime().exec(AcsContext.WINDOWS_BAT_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "GBK"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            ps.waitFor();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("----------Run File Update End----------");
    }
}