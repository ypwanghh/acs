/*
 * Copyright (c) 2014 MOOD, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of MOOD, LLC.
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with MOOD.
 *  
 * Create Date : Dec 16, 2014 11:11:50 AM
 */
package com.mood.acs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

/** 
 * ClassName: AcsUtil <br/> 
 * Function: TODO Add Function for this Class here. <br/> 
 * 
 * Date: Dec 16, 2014 11:11:50 AM <br/> 
 * @author WangJam
 * @version $Revision:$
 * @change	$Change:$
 * @lastestModifier $Author:$
 */
public class AcsUtil {

    public static String identifyFileType(String path){
        String tail2Char = path.substring(path.length() - 2, path.length());
        String tail3Char = path.substring(path.length() - 3, path.length());
        String tail4Char = path.substring(path.length() - 4, path.length());
        String tail10Char = path.substring(path.length() - 10, path.length());
        
        if(tail2Char.equalsIgnoreCase("as")){
            return "as";
        }else if(tail2Char.equalsIgnoreCase("pl")){
            return "pl";
        }else if(tail4Char.equalsIgnoreCase("mxml")){
            return "mxml";
        }else if(tail3Char.equalsIgnoreCase("xml")){
            return "xml";
        }else if(tail4Char.equalsIgnoreCase("java")){
            return "java";
        }else if(tail3Char.equalsIgnoreCase("sql")){
            return "sql";
        }else if(tail3Char.equalsIgnoreCase("bat")){
            return "bat";
        }else if(tail3Char.equalsIgnoreCase("pkb")){
            return "pkb";
        }else if(tail3Char.equalsIgnoreCase("pks")){
            return "pks";
        }else if(tail3Char.equalsIgnoreCase("dat")){
            return "dat";
        } else if (tail10Char.equalsIgnoreCase("properties")) {
            return "properties";
        } else {
            return "other";
        }
    }

    public static boolean validFileType4Index(String path) {

        String fileType = identifyFileType(path);

        boolean isAsFile = fileType.equalsIgnoreCase("as");
        boolean isPlFile = fileType.equalsIgnoreCase("pl");
        boolean isMxmlFile = fileType.equalsIgnoreCase("mxml");
        boolean isXMLFile = fileType.equalsIgnoreCase("xml");
        boolean isJavaFile = fileType.equalsIgnoreCase("java");
        boolean isSQLFile = fileType.equalsIgnoreCase("sql");
        boolean isBatFile = fileType.equalsIgnoreCase("bat");
        boolean isPKBFile = fileType.equalsIgnoreCase("pkb");
        boolean isPKSFile = fileType.equalsIgnoreCase("pks");
        boolean isDATFile = fileType.equalsIgnoreCase("dat");
        boolean isPropertiesFile = fileType.equalsIgnoreCase("properties");

        if (isAsFile || isPlFile || isMxmlFile || isXMLFile || isJavaFile || isSQLFile || isBatFile || isPKBFile || isPKSFile || isDATFile
                || isPropertiesFile) {
            return true;
        } else {
            return false;
        }
    }

    public static void listFile(File f, List<File> fileList) {
        if (f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                listFile(files[i], fileList);
            }
        } else {
            fileList.add(f);
        }
    }

    public static StringBuilder revertPath(String path, String split) {
        String[] a = path.split(split);

        StringBuilder tsb = new StringBuilder();
        for (int j = 0; j < a.length - 1; j++) {
            tsb.append(a[j]).append("/");
        }
        return tsb;
    }

    /**
     * split by \.<br/>
     * 
     * @author WangJam
     * @param path
     * @return
     */
    public static String revertPathWithName(String path, String split) {
        String[] a = path.split(split);

        StringBuilder tsb = new StringBuilder();
        for (int j = 0; j < a.length; j++) {
            tsb.append(a[j]).append("/");
        }
        return tsb.substring(0, tsb.length() - 1);
    }

    /**
     * split by \.<br/>
     * 
     * @author WangJam
     * @param revertedPath
     * @return
     */
    public static String revertPath2Name(String revertedPath, String split) {
        String[] a = revertedPath.split(split);
        return a[a.length - 1];
    }

    /**
     * Read file context
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String readFile(String filePath) throws Exception {
        @SuppressWarnings("resource")
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        StringBuffer content = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            content.append(str).append("\n");
        }
        return content.toString();
    }

    public static String readFileWithLineNumber(String path) {

        File myFile = new File(path);
        if (!myFile.exists()) {
            System.err.println("Can't Find " + path);
        }
        String fileStr = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(myFile));
            int line = 1;
            while ((fileStr = in.readLine()) != null) {
                sb.append("\t" + line + " " + fileStr + "\r\n");
                line++;
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return sb.toString();
    }

    public static String readStringWithLineNumber(String doc, String fileType) {

        String fileStr = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new StringReader(doc));
            int line = 1;
            while ((fileStr = in.readLine()) != null) {
                sb.append("\t" + line + " " + fileStr + "\r\n");
                line++;
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        if ("xml".equalsIgnoreCase(fileType) || "mxml".equalsIgnoreCase(fileType) || "dat".equalsIgnoreCase(fileType)) {
            return changeToHtml(sb.toString());
        }else{
            return sb.toString();
        }
    }

    public static String readWholeLineOfTerm(String doc, String query) {

        String fileStr = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new StringReader(doc));
            int line = 1;
            boolean readNextLine = false;
            while ((fileStr = in.readLine()) != null) {
                line++;
                if (readNextLine) {
                    sb.append(" ");
                    sb.append(fileStr.replace("\"", "'").trim());
                    sb.append("|");
                    readNextLine = false;
                }
                if (fileStr.indexOf(query) != -1) {
                    sb.append("Line " + line + " : " + fileStr.replace("\"", "'").trim());
                    readNextLine = true;
                }
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        return sb.toString();
    }

    public static String changeToHtml(String text) {
        text = text.replace("&", "&amp;");
        text = text.replace(" ", "&nbsp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("\"", "&quot;");
        text = text.replace(" ", "&nbsp;&nbsp;&nbsp;&nbsp;");
        text = text.replace("public", "<b>public</b>");
        text = text.replace("class", "<b>class</b>");
        text = text.replace("static", "<b>static</b>");
        text = text.replace("void", "<b>void</b>");
        text = text.replace("&lt;font&nbsp;color='red'&gt;", "<font color='red'>");
        text = text.replace("&lt;/font&gt;", "</font>");
        return text;
    }

}

