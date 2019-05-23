/**
 * 1 mei 2019
 */
package com.tetris;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Boris Mulder
 *
 */
public class TetrisHelper
{
	public static final int SHAPE_MATRIX_WIDTH = 4;
	public static final int SHAPE_MATRIX_HEIGHT = 4;
	private static final byte[][][] SHAPES = new byte[7][4][SHAPE_MATRIX_WIDTH*SHAPE_MATRIX_HEIGHT];
	
	
	public static void Init()
	{
		//#SHAPE 0
		SHAPES[0][0] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0,
				1, 1, 1, 1,
		};
		
		SHAPES[0][1] = new byte[] {
				0, 1, 0, 0,
				0, 1, 0, 0,
				0, 1, 0, 0,
				0, 1, 0, 0,
		};
		//#SHAPE 0
		
		
		//#SHAPE 1
		SHAPES[1][0] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				1, 0, 0, 0,
				1, 1, 1, 0,
		};
		
		SHAPES[1][1] = new byte[] {
				0, 0, 0, 0,
				1, 1, 0, 0,
				1, 0, 0, 0,
				1, 0, 0, 0,
		};
		
		SHAPES[1][2] = new byte[] {
				0, 0, 0, 0,
				1, 1, 1, 0,
				0, 0, 1, 0,
				0, 0, 0, 0,
		};
		
		SHAPES[1][3] = new byte[] {
				0, 0, 0, 0,
				0, 1, 0, 0,
				0, 1, 0, 0,
				1, 1, 0, 0,
		};
		//#SHAPE 1
		
		
		//#SHAPE 2
		SHAPES[2][0] = HorizontalFlip(SHAPES[1][0]);
		SHAPES[2][1] = HorizontalFlip(SHAPES[1][1]);
		SHAPES[2][2] = HorizontalFlip(SHAPES[1][2]);
		SHAPES[2][3] = HorizontalFlip(SHAPES[1][3]);
		//#SHAPE 2
		
		
		//#SHAPE 3
		SHAPES[3][0] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 1, 1, 0,
				0, 1, 1, 0,
		};
		//#SHAPE 3
		
		
		//#SHAPE 4
		SHAPES[4][0] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 1, 1, 0,
				1, 1, 0, 0,
		};
		
		SHAPES[4][1] = new byte[] {
				0, 0, 0, 0,
				1, 0, 0, 0,
				1, 1, 0, 0,
				0, 1, 0, 0,
		};
		//#SHAPE 4
		
		
		//#SHAPE 5
		SHAPES[5][0] = HorizontalFlip(SHAPES[4][0]);
		SHAPES[5][1] = HorizontalFlip(SHAPES[4][1]);
		//#SHAPE 5
		
		
		//#SHAPE 6
		SHAPES[6][0] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 1, 0, 0,
				1, 1, 1, 0,
		};
		
		SHAPES[6][1] = new byte[] {
				0, 0, 0, 0,
				1, 0, 0, 0,
				1, 1, 0, 0,
				1, 0, 0, 0,
		};
		
		SHAPES[6][2] = new byte[] {
				0, 0, 0, 0,
				0, 0, 0, 0,
				1, 1, 1, 0,
				0, 1, 0, 0,
		};
		
		SHAPES[6][3] = new byte[] {
				0, 0, 0, 0,
				0, 1, 0, 0,
				1, 1, 0, 0,
				0, 1, 0, 0,
		};
		//#SHAPE 6
	}
	
	public static int GetRandomShapeNumber()
	{
		return ThreadLocalRandom.current().nextInt(0, SHAPES.length);
	}

	/**
	 * Gets a shape
	 * @param index
	 * @return
	 */
	public static boolean[][] GetShape(int index)
	{
		return CreateShape(SHAPES[index][0]);
	}
	
	/**
	 * Returns a rotated shape
	 * @param index
	 * @param rotation
	 * @return
	 */
	public static boolean[][] GetShapeWithRotation(int index,int rotation)
	{	
		return CreateShape(SHAPES[index][rotation]);
	}
	
	/**
	 * Checks how many rotations a shape has
	 * @param shapeIndex
	 * @return
	 */
	public static int GetRotationLenght(int shapeIndex)
	{
		int lenght = 0;
		
		for(int i = 0; i < SHAPES[shapeIndex].length; i++)
		{
			byte[] bb = SHAPES[shapeIndex][i];
			int emptyCount = 0;
			
			for(byte b : bb)
			{
				if(b == 0)
					emptyCount++;
			}
			
			if(emptyCount != bb.length)
				lenght++;
		}
		
		return lenght;
	}
	
	
	private static byte[] HorizontalFlip(byte[] shape)
	{
		final byte[] index_zero = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_one = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_two = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_three = new byte[SHAPE_MATRIX_WIDTH];
		
		final byte[] f_index_zero = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] f_index_one = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] f_index_two = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] f_index_three = new byte[SHAPE_MATRIX_WIDTH];
		
		System.arraycopy(shape, 0, index_zero, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 4, index_one, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 8, index_two, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 12, index_three, 0, SHAPE_MATRIX_WIDTH);

		for(int i = 0; i < SHAPE_MATRIX_WIDTH; i++)
		{
			f_index_zero[i] = index_zero[(index_zero.length - 1) - i];
			f_index_one[i] = index_one[(index_zero.length - 1) - i];
			f_index_two[i] = index_two[(index_zero.length - 1) - i];
			f_index_three[i] = index_three[(index_zero.length - 1) - i];
		}
		
		final byte[] hf_shape = new byte [shape.length];
		
		System.arraycopy(f_index_zero, 0, hf_shape, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(f_index_one, 0, hf_shape, 4, SHAPE_MATRIX_WIDTH);
		System.arraycopy(f_index_two, 0, hf_shape, 8, SHAPE_MATRIX_WIDTH);
		System.arraycopy(f_index_three, 0, hf_shape, 12, SHAPE_MATRIX_WIDTH);
		
		return hf_shape;
	}

	/**
	 * Creates a shape from a byte array
	 * @param shape
	 * @return
	 */
	private static boolean[][] CreateShape(byte[] shape)
	{	
		final boolean[][] shape_matrix = new boolean[SHAPE_MATRIX_WIDTH][SHAPE_MATRIX_HEIGHT];
		final byte[] index_zero = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_one = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_two = new byte[SHAPE_MATRIX_WIDTH];
		final byte[] index_three = new byte[SHAPE_MATRIX_WIDTH];
		
		System.arraycopy(shape, 0, index_zero, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 4, index_one, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 8, index_two, 0, SHAPE_MATRIX_WIDTH);
		System.arraycopy(shape, 12, index_three, 0, SHAPE_MATRIX_WIDTH);
		
		for(int r = 0; r < SHAPE_MATRIX_WIDTH; r++)
		{
			shape_matrix[0][r] = index_zero[r] == 1;
			shape_matrix[1][r] = index_one[r] == 1;
			shape_matrix[2][r] = index_two[r] == 1;
			shape_matrix[3][r] = index_three[r] == 1;
		}
		
		return shape_matrix;
	}
}
