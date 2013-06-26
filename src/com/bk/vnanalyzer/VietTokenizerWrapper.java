package com.bk.vnanalyzer;

import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * User: caomanhdat
 * Date: 6/26/13
 * Time: 9:14 AM
 */
public class VietTokenizerWrapper {
    //==================================================
    // Final fields, inner classes
    //==================================================

    //==================================================
    // Fields
    //==================================================

    private static VietTokenizer vietTokenizer = null;

    //==================================================
    // Constructors
    //==================================================

    //==================================================
    // Getter/Setter methods
    //==================================================

    public static VietTokenizer getVietTokenizer(){
        if(vietTokenizer == null) vietTokenizer = new VietTokenizer();
        return vietTokenizer;
    }

    //==================================================
    // Public methods
    //==================================================

    //==================================================
    // Private/Protected methods
    //==================================================

    //==================================================
    // Override/Implement methods
    //==================================================
}
