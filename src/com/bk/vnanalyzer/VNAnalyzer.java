package com.bk.vnanalyzer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;
import vn.hus.nlp.tokenizer.VietTokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * User: caomanhdat
 * Date: 5/22/13
 * Time: 3:28 PM
 */
public class VNAnalyzer extends StopwordAnalyzerBase {

    private byte typeSaving = 0;

    public VNAnalyzer(Version version) {
        super(version);
    }

    public VNAnalyzer(Version version, CharArraySet stopwords) {
        super(version, stopwords);
    }


    @Override
    protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
        final Tokenizer src = new VNTokenizer(reader);
        TokenStream tok = new StandardFilter(matchVersion, src);
        tok = new LowerCaseFilter(matchVersion, tok);
        tok = new StopFilter(matchVersion, tok, stopwords);
        return new TokenStreamComponents(src, tok);
    }


}
