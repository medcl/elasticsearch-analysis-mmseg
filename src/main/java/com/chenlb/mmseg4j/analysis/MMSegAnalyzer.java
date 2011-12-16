package com.chenlb.mmseg4j.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

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

	protected Dictionary dic;

	/**
	 * @param path 词库路径
	 * @see Dictionary#getInstance(String)
	 */
	public MMSegAnalyzer(String path) {
		dic = Dictionary.getInstance(path);
	}
	
	/**
	 * @param path 词库目录
	 * @see Dictionary#getInstance(File)
	 */
	public MMSegAnalyzer(File path) {
		dic = Dictionary.getInstance(path);
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
	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {
		
		MMSegTokenizer mmsegTokenizer = (MMSegTokenizer) getPreviousTokenStream();
		if(mmsegTokenizer == null) {
			mmsegTokenizer = new MMSegTokenizer(newSeg(), reader);
			setPreviousTokenStream(mmsegTokenizer);	//保存实例
		} else {
			mmsegTokenizer.reset(reader);
		}
		
		return mmsegTokenizer;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream ts = new MMSegTokenizer(newSeg(), reader);
		return ts;
	}
}
