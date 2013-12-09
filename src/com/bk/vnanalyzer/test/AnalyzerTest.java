package com.bk.vnanalyzer.test;

import com.bk.vnanalyzer.VNAnalyzer;
import com.bk.vnanalyzer.ViStopWordsProvider;
import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
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
        VNAnalyzer viAnalyzer = new VNAnalyzer(Version.LUCENE_43,ViStopWordsProvider.getStopWords("resources/stopwords.txt"));

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43,viAnalyzer);
        IndexWriter indexWriter = new IndexWriter(d, config);


        FieldType fieldType = new FieldType();
        fieldType.setIndexed(true);
        fieldType.setStored(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorOffsets(true);

        Document doc1 = new Document();
        doc1.add(new Field("content","sinh sinh viên đại học bách khoa hà nội", fieldType));
        Document doc2 = new Document();
        doc2.add(new Field("content","viện công nghệ thông tin đại học bách khoa hà nội", fieldType));
        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);
        indexWriter.close();

        indexReader = IndexReader.open(d);
    }

    @Test
    public void searchTest() throws IOException {

        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs result = searcher.search(new TermQuery(new Term("content", "bkviews")), 5);
        Assert.assertEquals(1,result.scoreDocs.length);

    }

    @Test
    public void termFreqTest() throws IOException {

        HashMap<String,Integer> termFreq = new HashMap<String, Integer>();
        termFreq.put("sinh viên",1);
        termFreq.put("viện",1);
        termFreq.put("bách khoa",2);
        termFreq.put("công nghệ thông tin",1);
        termFreq.put("hà nội",2);
//        Fields
        Fields fields = MultiFields.getFields(indexReader);
        Terms terms = fields.terms("content");
        TermsEnum termsEnum = terms.iterator(null);
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            System.out.println(text.utf8ToString() + " " + termsEnum.docFreq());
            if(termFreq.get(text.utf8ToString())!= null) Assert.assertEquals((int)termFreq.get(text.utf8ToString()),termsEnum.docFreq());
        }
    }

    @Test
    public void termOffsetTest() throws IOException {

        String docContent = indexReader.document(0).get("content");
        Terms tfvs = indexReader.getTermVector(0,"content");
        TermsEnum terms = tfvs.iterator(null);
        BytesRef term = null;
        while ((term = terms.next()) != null) {
            DocsAndPositionsEnum docsAndPositionsEnum = terms.docsAndPositions(MultiFields.getLiveDocs(indexReader),null);
            docsAndPositionsEnum.nextDoc();
            docsAndPositionsEnum.nextPosition();
            Assert.assertEquals(docContent.indexOf(term.utf8ToString()), docsAndPositionsEnum.startOffset());
            Assert.assertEquals(docContent.indexOf(term.utf8ToString())+term.utf8ToString().length(),docsAndPositionsEnum.endOffset());
        }
    }
}
