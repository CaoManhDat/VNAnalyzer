package com.bk.vnanalyzer;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * User: caomanhdat
 * Date: 5/22/13
 * Time: 11:26 AM
 */
public class ViStopWordsProvider {
    public static CharArraySet getStopWords(String file){
        Set<String> stopWords = new HashSet<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                stopWords.add(line.replaceAll("_"," "));
                line = br.readLine();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return new CharArraySet(Version.LUCENE_43,stopWords,true);
    }
}
