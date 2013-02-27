package com.chenlb.mmseg4j.rule;

import com.chenlb.mmseg4j.Chunk;

/**
 * Maximum Matching.<p/>
 * 
 * chuck中各个词的长度之和
 * 
 * @see http://technology.chtsai.org/mmseg/
 * 
 * @author chenlb 2009-3-16 上午09:47:51
 */
public class MaxMatchRule extends Rule{

	private int maxLen;
	
	public void addChunk(Chunk chunk) {
		if(chunk.getLen() >= maxLen) {
			maxLen = chunk.getLen();
			super.addChunk(chunk);
		}
	}
	
	@Override
	protected boolean isRemove(Chunk chunk) {
		
		return chunk.getLen() < maxLen;
	}

	public void reset() {
		maxLen = 0;
		super.reset();
	}
}
