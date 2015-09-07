/*
 * Copyright (c) 2014 MOOD, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of MOOD, LLC.
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with MOOD.
 *  
 * Create Date : Dec 1, 2014 6:07:02 PM
 */
package com.mood.acs.front.ctrl;

import java.io.File;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mood.acs.util.AcsContext;
import com.mood.acs.util.AcsUtil;

/** 
 * ClassName: SourceCodePageCtrl <br/> 
 * Function: TODO Add Function for this Class here. <br/> 
 * 
 * Date: Dec 1, 2014 6:07:02 PM <br/> 
 * @author WangJam
 * @version $Revision:$
 * @change	$Change:$
 * @lastestModifier $Author:$
 */
@Controller
@SuppressWarnings("all")
public class SourceCodePageCtrl {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/search")
    public ModelAndView sourceCodePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("/search");

        String project = ServletRequestUtils.getStringParameter(request, "pj", "");
        String queryValue = ServletRequestUtils.getStringParameter(request, "q", "");

        logger.debug("User Select Project : " + project);
        logger.debug("User Query : " + queryValue);

        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/search";

        if (request.getSession() != null && request.getSession().getAttribute(request.getSession().getId()) != null) {
            String sessionId = request.getSession().getId();
            String remoteUser = request.getRemoteUser();
            String remoteHost = request.getRemoteHost();
            String remoteAddr = request.getRemoteAddr();
            Long lastAccessedTime = request.getSession().getLastAccessedTime();

            logger.debug("Session Id: " + sessionId);
            logger.debug("Remote User: " + remoteUser);
            logger.debug("Remote Host: " + remoteHost);
            logger.debug("Remote Address: " + remoteAddr);
            logger.debug("Last Accessed Time: " + lastAccessedTime);
        }

        if (queryValue.equals("")) {
            return mav;
        }

        mav.addObject("queryValue", queryValue);
        if(project.equals("")){
            mav.addObject("checkedMain", "checked");
        }else if(project.equals("999")){
            mav.addObject("checkedMain", "checked");
        }else if(project.equals("400")){
            mav.addObject("checked400", "checked");
        }else if(project.equals("350")){
            mav.addObject("checked350", "checked");
        }else if(project.equals("340")){
            mav.addObject("checked340", "checked");
        }else if(project.equals("330")){
            mav.addObject("checked330", "checked");
        }
        
        return mav;
    }

    @RequestMapping("/data")
    @ResponseBody
    public String ajaxGetData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        String project = ServletRequestUtils.getStringParameter(request, "pj", "");
        String queryValue = ServletRequestUtils.getStringParameter(request, "q", "").replace("'", "\"") + " AND version:" + project;

        logger.debug("User Select Project : " + project);
        logger.debug("User Query : " + queryValue);

        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(AcsContext.FILE_INDEX)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44, AcsContext.MY_ENGLISH_STOP_WORDS_SET);
        QueryParser parser = new QueryParser(Version.LUCENE_44, "fileContents", analyzer);
        parser.setAllowLeadingWildcard(true);
        parser.setAutoGeneratePhraseQueries(true);
        parser.setLowercaseExpandedTerms(true);
        parser.setPhraseSlop(0);
        Query query = parser.parse(queryValue);
        TopDocs results = searcher.search(query, 1000);
        ScoreDoc[] score = results.scoreDocs;
        if (score.length == 0) {
            System.out.println("Sorry, no results found.");
            logger.debug("User couldn't query : " + queryValue);
        } else {
            System.out.println("Search[ " + queryValue + " ]get " + score.length + " results.");
            logger.debug("User query : " + queryValue + ", get ", score.length + " results.");
        }

        StringBuilder sb = new StringBuilder("{\"page\":1,\"total\":1,\"records\":" + score.length + ",\"rows\":[");

        String prefixStr = null;
        if ("340".equals(project)) {
            prefixStr = AcsContext.RO_FILE_TARGET_345;
        } else if ("350".equals(project)) {
            prefixStr = AcsContext.RO_FILE_TARGET_350;
        } else if ("400".equals(project)) {
            prefixStr = AcsContext.RO_FILE_TARGET_400;
        } else if ("999".equals(project)) {
            prefixStr = AcsContext.RO_FILE_TARGET_999;
        }

        for (int i = 0; i < score.length; i++) {
            try {
                Document doc = searcher.doc(score[i].doc);
                System.out.print("This is " + (i+1) + " result which you searched, file name is:");
                System.out.println(doc.get("filePath"));

                String path = doc.get("filePath");
                StringBuilder pathSb = AcsUtil.revertPath(path, AcsContext.FILESEPARATOR);
                String fileName = AcsUtil.revertPath2Name(path, AcsContext.FILESEPARATOR);
                String filePathForShow = pathSb.substring(0, pathSb.length() - 1);
                String queryValuePass2ShowFile = "";
                if (query.toString().indexOf("+") != -1) {
                    for (String field : query.toString().split("[+]")) {
                        String[] fieldArr = field.split(":");
                        String name = "";
                        String value = "";
                        if (fieldArr.length >= 2) {
                            name = fieldArr[0].trim();
                            value = fieldArr[1].trim().replace("\"", "");
                        }
                        if ("fileContents".equals(name)) {
                            queryValuePass2ShowFile = queryValuePass2ShowFile + " " + value;
                        }
                    }
                } else {
                    queryValuePass2ShowFile = queryValue.replace("\"", "");
                }

                StringBuilder fileNameSb = new StringBuilder(
                        "<span style=\'float: left; cursor: pointer; text-decoration: underline;\' title=\'View source: ");
                fileNameSb.append(fileName).append("\' onclick=\'addTab(&quot;").append(pathSb.toString() + fileName)
                        .append("&quot;,&quot;showfile?pj=").append(project).append("&amp;target=")
                        .append(fileName.toLowerCase().replace(".", "+")).append("&amp;q=").append(queryValuePass2ShowFile)
                        .append("&amp;type=").append(doc.get("fileType").toUpperCase()).append("&amp;prod=").append(doc.get("prod"))
                        .append("&amp;path=")
                        .append(filePathForShow.substring(prefixStr.length(), filePathForShow.length()) + "/" + fileName)
                        .append("&amp;rpath=").append(pathSb.toString() + fileName).append("&quot;,&quot;").append(fileName)
                        .append("&quot;, &quot;#list&quot;)\'>").append(fileName)
                        .append("</span>");

                sb.append("{\"id\":\"").append(pathSb.toString() + fileName).append("\",\"pid\":\"").append(doc.get("pid"))
                        .append("\",\"pcode\":\"").append(doc.get("prodCode")).append("\",\"type\":\"")
                        .append(doc.get("fileType").toUpperCase()).append("\",\"path\":\"")
                        .append(filePathForShow.substring(prefixStr.length(), filePathForShow.length()))
                        .append("\",\"name\":\"").append(fileNameSb.toString()).append("\",\"grep\":\"").append("").append("\"},");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String records = sb.substring(0, sb.length() - 1) + "]}";
        return records;
    }

    @RequestMapping("/showfile")
    public ModelAndView showfileContext(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("/showfile");

        String project = ServletRequestUtils.getStringParameter(request, "pj", "");
        String queryValue = ServletRequestUtils.getStringParameter(request, "q", "");
        String target = ServletRequestUtils.getStringParameter(request, "target", "");
        String path = ServletRequestUtils.getStringParameter(request, "path", "");
        String fileName = AcsUtil.revertPath2Name(path, "/");
        String absolutePath = ServletRequestUtils.getStringParameter(request, "rpath", "");
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        String typeLowCase = type.toLowerCase();
        String prod = ServletRequestUtils.getStringParameter(request, "prod", "");

        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(AcsContext.FILE_INDEX)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44, AcsContext.MY_ENGLISH_STOP_WORDS_SET);
        QueryParser nameParser = new QueryParser(Version.LUCENE_44, "fileName", analyzer);
        Query query = nameParser.parse(fileName + " AND version:" + project);
        TopDocs results = searcher.search(query, 1000);
        ScoreDoc[] score = results.scoreDocs;
        String highLightText = null;
        String highLightContext = null;

        for (int i = 0; i < score.length; i++) {
            try {
                Document doc = searcher.doc(score[i].doc);
                String docContent = doc.get("fileContents");
                String docPath = doc.get("filePath");
                String docType = doc.get("fileType");
                String docProd = doc.get("prod");

                String docPathStr = AcsUtil.revertPathWithName(docPath, AcsContext.FILESEPARATOR);
                String docFileName = AcsUtil.revertPath2Name(docPath, AcsContext.FILESEPARATOR);

                if (docProd.equals(prod) && docType.equalsIgnoreCase(type) && docFileName.equals(fileName)
                        && docPathStr.equals(absolutePath)) {
                    SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
                    QueryParser contentParser = new QueryParser(Version.LUCENE_44, "fileContents", analyzer);
                    Query contentQuery = nameParser.parse(queryValue.replace("?", ""));
                    Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(contentQuery));
                    highlighter.setTextFragmenter(new SimpleFragmenter(docContent.length()));
                    if (docContent != null) {
                        TokenStream tokenStream = analyzer.tokenStream("fileContents", new StringReader(docContent));
                        highLightText = highlighter.getBestFragment(tokenStream, docContent);
                        if (highLightText == null) {
                            highLightText = docContent;
                        }
                        highLightContext = AcsUtil.readStringWithLineNumber(highLightText, docType);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        mav.addObject("project", project);
        mav.addObject("queryValue", queryValue);
        mav.addObject("target", target);
        mav.addObject("path", path);
        mav.addObject("type", type);
        mav.addObject("typeLowCase", typeLowCase);
        mav.addObject("prod", prod);
        mav.addObject("context", highLightContext);
        return mav;
    }

    @RequestMapping("/showgrep")
    public ModelAndView showGrep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        // TODO
        return mav;
    }
}
