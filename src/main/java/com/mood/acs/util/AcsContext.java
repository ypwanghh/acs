/*
 * Copyright (c) 2015 MOOD, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of MOOD, LLC.
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with MOOD.
 *  
 * Create Date : Jan 31, 2015 5:54:32 PM
 */
package com.mood.acs.util;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/** 
 * ClassName: AcsContext <br/> 
 * Function: TODO Add Function for this Class here. <br/> 
 * 
 * Date: Jan 31, 2015 5:54:32 PM <br/> 
 * @author WangJam
 * @version $Revision:$
 * @change	$Change:$
 * @lastestModifier $Author:$
 */
public class AcsContext {

    public static final String WINDOWS_BAT_PATH = "[Script Of Get/Update Source Codes From CVS/SVN/P4]";
    public static final String LINUX_BAT_PATH = "[Script Of Get/Update Source Codes From CVS/SVN/P4]";
    public static final String FILE_INDEX = "[Lucene Index Path]";

    public static final String RO_FILE_TARGET_345 = "[Application Source Codes Path, like 3.4.5 Version]";
    public static final String RO_FILE_TARGET_350 = "[Application Source Codes Path, like 3.5.0 Version]";
    public static final String RO_FILE_TARGET_400 = "[Application Source Codes Path, like 4.0.0 Version]";
    public static final String RO_FILE_TARGET_999_DB = "[Application Source Codes Path, like Main Version]";
    public static final String RO_FILE_TARGET_999 = "[Application Source Codes Path, like Main Version]";

    public static final CharArraySet MY_ENGLISH_STOP_WORDS_SET;

    static {
        final List<String> stopWords = Arrays.asList("a", "an", "and", "are", "at", "be", "but", "by", "for", "if", "in", "into", "is",
                "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was",
                "will", "with");
        final CharArraySet stopSet = new CharArraySet(Version.LUCENE_CURRENT, stopWords, false);
        MY_ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }

    public static final String BACK_SLASH_OF_WINDOWS = "\\\\";
    public static final String BACK_SLASH_OF_UNIX = "/";
    
    public static final String FILESEPARATOR = System.getProperty("file.separator").equals(BACK_SLASH_OF_UNIX) ? BACK_SLASH_OF_UNIX
            : BACK_SLASH_OF_WINDOWS;

}

