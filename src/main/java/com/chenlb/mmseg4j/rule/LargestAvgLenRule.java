package com.chenlb.mmseg4j.rule;

import com.chenlb.mmseg4j.Chunk;

/**
 * Largest Average Word Length.<p/>
 * 
 * 长度(Length)/词数
 * 
 * @see http://technology.chtsai.org/mmseg/
 * 
 * @author chenlb 2009-3-16 上午11:28:21
 */
public class LargestAvgLenRule extends Rule {

	private double largestAvgLen;
	
	@Override
	public void addChunk(Chunk chunk) {
		if(chunk.getAvgLen() >= largestAvgLen) {
			largestAvgLen = chunk.getAvgLen();
			super.addChunk(chunk);
		}
	}

	@Override
	protected boolean isRemove(Chunk chunk) {
		return chunk.getAvgLen() < largestAvgLen;
	}

	@Override
	public void reset() {
		largestAvgLen = 0;
		super.reset();
	}

}
