package com.ch.voxel;

/**
 * Represents a three-dimensional block in a grid-based environment.
 *
 * - z (int): represents a three-dimensional coordinate.
 *
 * - rt (boolean): Represents a boolean flag indicating a right-facing orientation
 * or state.
 */
public class Block {
	
	public int x, y, z;
	public boolean ft, bk, tp, bt, lt, rt;
	
	public Block(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.ft = false;
		this.bk = false;
		this.tp = false;
		this.bt = false;
		this.lt = false;
		this.rt = false;
	}

}
