package com.bk.vnanalyzer.test;

import com.bk.vnanalyzer.VNAnalyzer;
import com.bk.vnanalyzer.ViStopWordsProvider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: caomanhdat
 * Date: 6/26/13
 * Time: 4:05 PM
 */
public class Test {
    //==================================================
    // Final fields, inner classes
    //==================================================

    //==================================================
    // Fields
    //==================================================

    //==================================================
    // Constructors
    //==================================================

    //==================================================
    // Getter/Setter methods
    //==================================================

    //==================================================
    // Public methods
    //==================================================

    public static void main(String[] args) throws IOException {
        Directory d = new RAMDirectory();
        Analyzer viAnalyzer = new VNAnalyzer(Version.LUCENE_36, ViStopWordsProvider.getStopWords("resources/stopwords.txt"));

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,viAnalyzer);
        IndexWriter indexWriter = new IndexWriter(d, config);

        Document doc1 = new Document();
        doc1.add(new Field("content","đại học bách khoa hà nội", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        Document doc2 = new Document();
        doc2.add(new Field("content","viện công nghệ thông tin đại học bách khoa hà nội", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

        for(int i = 0; i < 4; i++){
            BufferedReader bufferedReader = new BufferedReader(new FileReader("doc/"+i+".txt"));
            StringBuffer bufferContent = new StringBuffer("");
            String line;
            while( (line = bufferedReader.readLine()) != null){
                bufferContent.append(line+"\n");
            }
            Document doc = new Document();
            doc.add(new Field("content",new FileReader("doc/"+i+".txt")));
            indexWriter.addDocument(doc);
        }



        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);

        indexWriter.close();

        IndexSearcher searcher = new IndexSearcher(IndexReader.open(d));
        TopDocs result = searcher.search(new TermQuery(new Term("content", "bkviews")), 5);
        for(int i = 0; i < result.scoreDocs.length; i++){
            System.out.println("Search hit : " + result.scoreDocs[i].doc);
        }

    }

    //==================================================
    // Private/Protected methods
    //==================================================

    //==================================================
    // Override/Implement methods
    //==================================================
}
