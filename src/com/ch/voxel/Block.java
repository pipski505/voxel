package com.ch.voxel;

/**
 * Represents a 3D block in a virtual environment with a specified position and various
 * boolean properties.
 *
 * - z (int): represents a 3D coordinate.
 *
 * - rt (boolean): Represents a boolean value indicating a right side of a block.
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
