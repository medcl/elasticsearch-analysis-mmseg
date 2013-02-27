package com.chenlb.mmseg4j.rule;

import com.chenlb.mmseg4j.Chunk;

/**
 * Largest Sum of Degree of Morphemic Freedom of One-Character. <p/>
 * 
 * 各单字词词频的对数之和*100
 * 
 * @see http://technology.chtsai.org/mmseg/
 * 
 * @author chenlb 2009-3-16 上午11:28:30
 */
public class LargestSumDegreeFreedomRule extends Rule {

	private int largestSumDegree = Integer.MIN_VALUE;
	@Override
	public void addChunk(Chunk chunk) {
		if(chunk.getSumDegree() >= largestSumDegree) {
			largestSumDegree = chunk.getSumDegree();
			super.addChunk(chunk);
		}
	}

	@Override
	public void reset() {
		largestSumDegree = Integer.MIN_VALUE;
		super.reset();
	}

	@Override
	protected boolean isRemove(Chunk chunk) {
		
		return chunk.getSumDegree() < largestSumDegree;
	}

}
