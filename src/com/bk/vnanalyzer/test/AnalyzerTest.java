package com.bk.vnanalyzer.test;

import com.bk.vnanalyzer.VNAnalyzer;
import com.bk.vnanalyzer.ViStopWordsProvider;
import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * User: caomanhdat
 * Date: 6/26/13
 * Time: 8:39 AM
 */
public class AnalyzerTest {
    IndexReader indexReader;
    @Before
    public void createDocument() throws IOException {
        Directory d = new RAMDirectory();
        Analyzer viAnalyzer = new VNAnalyzer(Version.LUCENE_36,ViStopWordsProvider.getStopWords("resources/stopwords.txt"));

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,viAnalyzer);
        IndexWriter indexWriter = new IndexWriter(d, config);

        Document doc1 = new Document();
        doc1.add(new Field("content","đại học bách khoa hà nội", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        Document doc2 = new Document();
        doc2.add(new Field("content","viện công nghệ thông tin đại học bách khoa hà nội", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);
        indexWriter.close();

        indexReader = IndexReader.open(d);
    }


    @Test
    public void termFreqTest() throws IOException {

        HashMap<String,Integer> termFreq = new HashMap<String, Integer>();
        termFreq.put("viện",1);
        termFreq.put("bách khoa",2);
        termFreq.put("công nghệ thông tin",1);
        termFreq.put("hà nội",2);
        termFreq.put("đại học",2);


        TermEnum termEnum = indexReader.terms();
        while (termEnum.next()) {
            Term term = termEnum.term();
            Assert.assertEquals((int)termFreq.get(term.text()),termEnum.docFreq());
        }
    }

    @Test
    public void termOffsetTest() throws IOException {

        String docContent = indexReader.document(0).get("content");
        TermFreqVector[] tfvs = indexReader.getTermFreqVectors(0);

        for (TermFreqVector tfv : tfvs) {
            TermPositionVector tpv = (TermPositionVector)tfv;

            String[] termTexts = tfv.getTerms();

            for (int j = 0; j < termTexts.length; j++) {
                TermVectorOffsetInfo[] tvoi = tpv.getOffsets(j); // all position that term appear
                for(TermVectorOffsetInfo termOffset : tvoi){
                    Assert.assertEquals(docContent.indexOf(termTexts[j]), termOffset.getStartOffset());
                    Assert.assertEquals(docContent.indexOf(termTexts[j])+termTexts[j].length(),termOffset.getEndOffset());
                }
            }
        }
    }
}
