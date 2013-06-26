package com.bk.vnanalyzer;

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
    public static Set<String> getStopWords(String file){
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
        return stopWords;
    }
}
