package com.ch.voxel;

/**
 * Has a constructor with three parameters for x, y, and z coordinates and eight
 * instance variables for boolean flags indicating various properties of the block.
 * Fields:
 * 	- z (int): represents the vertical position of a block in a 3D space.
 * 	- rt (boolean): represents whether the block has been right-clicked on by a player.
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
