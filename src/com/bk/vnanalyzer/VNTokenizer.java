package com.bk.vnanalyzer;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import vn.hus.nlp.tokenizer.TokenizerProvider;
import vn.hus.nlp.tokenizer.VietTokenizer;
import vn.hus.nlp.tokenizer.tokens.TaggedWord;


public class VNTokenizer extends Tokenizer{

    List<TaggedWord> taggedWords;
    private int numWord;

    private int index = 0;
    private int offset = 0;
    private final CharTermAttribute termAttr;
    private final PositionIncrementAttribute posAttr;
    private final OffsetAttribute offsetAttr;



    public VNTokenizer(Reader input){
        getTaggedWords(input);

        this.termAttr = addAttribute(CharTermAttribute.class);
        this.posAttr = addAttribute(PositionIncrementAttribute.class);
        this.offsetAttr = addAttribute(OffsetAttribute.class);
    }



    private void getTaggedWords(Reader input){
        try{

            VietTokenizerWrapper.getVietTokenizer().getTokenizer().tokenize(input);
            taggedWords = VietTokenizerWrapper.getVietTokenizer().getTokenizer().getResult();
        }catch (IOException e){
            System.err.println("Error Tokenizer Input : " + input);
            taggedWords = new ArrayList<TaggedWord>();
        }
        numWord = taggedWords.size();
        offset = 0;
        index = 0;
    }


    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
        if (index == numWord)
            return false;
        TaggedWord wordTag = taggedWords.get(index);
        String termTex = wordTag.getText();

        termAttr.copyBuffer(termTex.toCharArray(),0,termTex.length());
        posAttr.setPositionIncrement(1);
        offsetAttr.setOffset(offset, offset+termTex.length());

        offset += termTex.length();
        offset++;
        index++;
        return true;
    }

    @Override
    public final void end() {
        // set final offset
        int finalOffset = correctOffset(offset);
        offsetAttr.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset(Reader input) throws IOException {
        super.reset(input);
        getTaggedWords(input);
    }
}
