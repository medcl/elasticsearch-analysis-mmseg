package com.chenlb.mmseg4j.analysis;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;


/**
 * 最多分词方式.
 * 
 * @author chenlb 2009-4-6 下午08:43:46
 */
public class MaxWordAnalyzer extends MMSegAnalyzer {

    public MaxWordAnalyzer() {
        super();
    }


    public MaxWordAnalyzer(Dictionary dic) {
        super(dic);
    }


    protected Seg newSeg() {
        return new MaxWordSeg(getDict());
    }
}
