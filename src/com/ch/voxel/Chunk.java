package com.ch.voxel;

import java.util.ArrayList;
import java.util.List;

import com.ch.Model;
import com.ch.SimplexNoise;
import com.ch.Util;
import com.ch.math.Matrix4f;

/**
 * Represents a 3D chunk of data in a game environment, generating block positions
 * based on Simplex Noise and updating block relationships.
 */
public class Chunk {

	public static final int CHUNK_SIZE = 64;
	private static final int CHUNK_SIZE_SQUARED = CHUNK_SIZE * CHUNK_SIZE;
	private static final int CHUNK_SIZE_CUBED = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;

	private Block[] blocks;
	public int x, y, z;
	private Model model;
	
	/**
	 * Returns a model when called, creating a new model if `to_gen_model` is true and
	 * resetting `to_gen_model` to false after creation.
	 *
	 * @returns a pre-generated model object, or the existing model if it has already
	 * been generated.
	 */
	public Model getModel() {
		if (to_gen_model) {
			createModel();
			to_gen_model = false;
		}
		return model;
	}
	
	/**
	 * Returns a 4x4 matrix representing the translation of a 3D object in the world,
	 * where the object's position is determined by the `x`, `y`, and `z` coordinates
	 * scaled by a `CHUNK_SIZE` value.
	 *
	 * @returns a 4x4 translation matrix representing the object's position in 3D space.
	 */
	public Matrix4f getModelMatrix() {
		return new Matrix4f().initTranslation(x * CHUNK_SIZE, y * CHUNK_SIZE, z * CHUNK_SIZE);
	}

	public Chunk(int _x, int _y, int _z) {
		
		this.x = _x;
		this.y = _y;
		this.z = _z;
		
		blocks = new Block[CHUNK_SIZE_CUBED];
		
		for (int i = 0; i < CHUNK_SIZE_CUBED; i++) {
			int z = i / CHUNK_SIZE_SQUARED;
			int ii = i - (z * CHUNK_SIZE_SQUARED);
			int y = ii / CHUNK_SIZE;
			int x = ii % CHUNK_SIZE;
			if (SimplexNoise.noise((x + this.x * CHUNK_SIZE) / 10f, (y + this.y * CHUNK_SIZE) / 10f, (z + this.z * CHUNK_SIZE) / 10f) > 0.1f) {
			blocks[i] = new Block(x, y, z);
			}
		}
	}
	
	

	/**
	 * Updates the neighbor flags of each block in a chunk based on the presence or absence
	 * of adjacent blocks in the same or neighboring chunks.
	 */
	public void updateBlocks() {
		for (int i = 0; i < CHUNK_SIZE_CUBED; i++) {
			Block b = blocks[i];
				if (b != null) {
				int n_x = i - 1;
				int p_x = i + 1;
				int n_y = i - CHUNK_SIZE;
				int p_y = i + CHUNK_SIZE;
				int n_z = i - CHUNK_SIZE_SQUARED;
				int p_z = i + CHUNK_SIZE_SQUARED;
				
				if (b.x - 1 < 0) {
					//TODO: check neighbor chunk
					b.lt = false;
				} else {
					Block bl = blocks[n_x];
					if (bl == null)
						b.lt = true;
					else
						b.lt = false;
				}
				if (b.y - 1 < 0) {
					//TODO: check neighbor chunk
					b.bt = false;
				} else {
					Block bl = blocks[n_y];
					if (bl == null)
						b.bt = true;
					else
						b.bt = false;
				}
				if (b.z - 1 < 0) {
					//TODO: check neighbor chunk
					b.ft = false;
				} else {
					Block bl = blocks[n_z];
					if (bl == null)
						b.ft = true;
					else
						b.ft = false;
				}
				
				if (b.x + 1 >= CHUNK_SIZE) {
					//TODO: check neighbor chunk
					b.rt = false;
				} else {
					Block bl = blocks[p_x];
					if (bl == null)
						b.rt = true;
					else
						b.rt = false;
				}
				if (b.y + 1 >= CHUNK_SIZE) {
					//TODO: check neighbor chunk
					b.tp = false;
				} else {
					Block bl = blocks[p_y];
					if (bl == null)
						b.tp = true;
					else
						b.tp = false;
				}
				if (b.z + 1 >= CHUNK_SIZE) {
					//TODO: check neighbor chunk
					b.bk = false;
				} else {
					Block bl = blocks[p_z];
					if (bl == null)
						b.bk = true;
					else
						b.bk = false;
				}
			}
		}
	}
	
//	class Vertex3i {
//		
//		public int x, y, z; 
//		public float u, v;
//		
//		public Vertex3i(x, y, z, u, v) {
//			
//		}
//		
//	}
	
	private ArrayList<Float> vertices = new ArrayList<>();
	private ArrayList<Integer> indices = new ArrayList<>();
	private boolean to_gen_model;
	
	
	public void toGenModel() { toGenModel(false); };
	
	/**
	 * Generates a 3D model by iterating through blocks, calling the `gen` method to
	 * process each block, and updating model arrays. The function also toggles the
	 * `to_gen_model` flag based on the `now` parameter.
	 *
	 * @param now flag for whether the model generation should be performed immediately
	 * or not.
	 */
	public void toGenModel(boolean now) {

		int max_index = 0;
//		System.out.println("gen model");
		for (int i = 0; i < CHUNK_SIZE_CUBED; i++) {
			Block b = blocks[i];
			if (b != null) {
				max_index = gen(vertices, indices, b, max_index);
					
			}
		}
//		System.out.println("vertice   : " + vertices.size() / 5 + " -- floats : " + vertices.size());
//		System.out.println("indices   : " + indices.size());
//		System.out.println("triangles : " + indices.size() / 3);
//		System.out.println("quads     : " + indices.size() / 6);
//		System.out.println("---------------------------\nloading model arrays");
		
		// cant implement filtering and re-indexing for textured cubes
//		{
//			
//			ArrayList<Integer>
//			
//		}
		
//		return Model.load(Util.toFloatArray(new_vertices), Util.toIntArray(new_indices));
		
		if (now) {
			createModel();
			to_gen_model = false;
		} else {
			to_gen_model = true;
		}
		
	}
	
	/**
	 * Loads a 3D model from a set of vertices and indices, which are converted to float
	 * and integer arrays, respectively, and stores the loaded model in the `model` field.
	 */
	private void createModel() {
		this.model = Model.load(Util.toFloatArray(vertices), Util.toIntArray(indices));
	}
	
	/**
	 * Generates a model by calling `toGenModel` with a boolean parameter set to `true`,
	 * then returns the generated model.
	 *
	 * @returns an instance of the `Model` class.
	 */
	public Model genModel() {
		
		toGenModel(true);
		
		return this.model;
	}

	/**
	 * Generates vertices and indices for a 3D block based on its position and orientation.
	 *
	 * @param vertices collection of 3D vertices used to construct a mesh, and it is
	 * dynamically populated with new vertices as they are generated.
	 *
	 * Transform.
	 *
	 * @param indices list of indices into the `vertices` list that correspond to the
	 * generated vertices.
	 *
	 * Deconstructs into a series of lists, each representing the indices for a specific
	 * block type.
	 * Each list is of length 6, representing the indices for a single block's vertices.
	 * The indices follow a consistent pattern, incrementing by one for each vertex.
	 *
	 * @param block block that is being processed, which contains properties such as
	 * position and flags (`ft`, `bk`, `bt`, `tp`, `lt`, `rt`) that determine the geometry
	 * to be generated.
	 *
	 * Exist as boolean flags.
	 *
	 * @param max_index current index in the `indices` list, and is incremented by the
	 * number of new indices added for each block face.
	 *
	 * @returns the updated maximum index value for the vertices and indices lists.
	 *
	 * Returned by the `gen` function is an integer value.
	 */
	private static int gen(List<Float> vertices, List<Integer> indices, Block block, int max_index) {
		
		float x = block.x;
		float y = block.y;
		float z = block.z;
		
		if (block.ft) {
			float[] tmp_v = { //
				x,   y,   z,   0, 0, //
				x+1, y,   z,   1, 0, //
				x+1, y+1, z,   1, 1, //
				x,   y+1, z,   0, 1, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 1, 2, 0, 2, 3}) indices.add(max_index + i);
			max_index += 4;
		}
		if (block.bk) {
			float[] tmp_v = { //
				x,   y,   z+1,   1, 0, //
				x+1, y,   z+1,   0, 0, //
				x+1, y+1, z+1,   0, 1, //
				x,   y+1, z+1,   1, 1, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 3, 2, 0, 2, 1}) indices.add(max_index + i);
			max_index += 4;
		}
		if (block.bt) {
			float[] tmp_v = { //
				x,   y,   z,     0, 0, //
				x+1, y,   z,   	 1, 0, //
				x+1, y,   z+1,   1, 1, //
				x,   y,   z+1,   0, 1, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 3, 2, 0, 2, 1}) indices.add(max_index + i);
			max_index += 4;
		}
		if (block.tp) {
			float[] tmp_v = { //
				x,   y+1, z,     0, 0, //
				x+1, y+1, z,     1, 0, //
				x+1, y+1, z+1,   1, 1, //
				x,   y+1, z+1,   0, 1, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 1, 2, 0, 2, 3}) indices.add(max_index + i);
			max_index += 4;
		}
		if (block.lt) {
			float[] tmp_v = { //
				x,   y,   z,     1, 0, //
				x,   y+1, z,     1, 1, //
				x,   y+1, z+1,   0, 1, //
				x,   y,   z+1,   0, 0, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 1, 2, 0, 2, 3}) indices.add(max_index + i);
			max_index += 4;
		}
		if (block.rt) {
			float[] tmp_v = { //
				x+1, y,   z,     0, 0, //
				x+1, y+1, z,     0, 1, //
				x+1, y+1, z+1,   1, 1, //
				x+1, y,   z+1,   1, 0, //
			}; //
			for (float f : tmp_v) vertices.add(f);
			for (int i : new int[] {0, 3, 2, 0, 2, 1}) indices.add(max_index + i);
			max_index += 4;
		}
		return max_index;
	}

}
