package com.chenlb.mmseg4j.analysis;

import org.apache.lucene.analysis.Analyzer;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;


/**
 * 默认使用 max-word
 *
 * @see {@link SimpleAnalyzer}, {@link ComplexAnalyzer}, {@link MaxWordAnalyzer}
 *
 * @author chenlb
 */
public class MMSegAnalyzer extends Analyzer {

    private Dictionary dic;


    public MMSegAnalyzer() {
        this.dic = Dictionary.getInstance();
    }


    public MMSegAnalyzer(Dictionary dic) {
        super();
        this.dic = dic;
    }


    protected Seg newSeg() {
        return new MaxWordSeg(dic);
    }


    public Dictionary getDict() {
        return dic;
    }


    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new MMSegTokenizer(newSeg()));
    }
}
