package com.chenlb.mmseg4j.rule;

import com.chenlb.mmseg4j.Chunk;

/**
 * Smallest Variance of Word Lengths.<p/>
 * 
 * 标准差的平方
 * 
 * @see http://technology.chtsai.org/mmseg/
 * 
 * @author chenlb 2009-3-16 上午11:28:27
 */
public class SmallestVarianceRule extends Rule {

	private double smallestVariance = Double.MAX_VALUE;
	
	@Override
	public void addChunk(Chunk chunk) {
		if(chunk.getVariance() <= smallestVariance) {
			smallestVariance = chunk.getVariance();
			super.addChunk(chunk);
		}
	}

	@Override
	public void reset() {
		smallestVariance = Double.MAX_VALUE;
		super.reset();
	}

	@Override
	protected boolean isRemove(Chunk chunk) {
		
		return chunk.getVariance() > smallestVariance;
	}

}
